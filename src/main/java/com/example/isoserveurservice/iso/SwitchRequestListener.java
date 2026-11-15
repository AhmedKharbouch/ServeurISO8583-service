package com.example.isoserveurservice.iso;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.iso.ISOUtil;
import org.jpos.util.NameRegistrar;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Autorise les demandes 0200 : recherche la carte, compare le montant demandé
 * au solde et répond 0210 avec le code réponse (champ 39) et l'état (champ 125).
 */
@Slf4j
public class SwitchRequestListener implements ISORequestListener {

    /** Nom sous lequel JposConfig publie la recherche de solde (PAN -> solde, null si inconnue). */
    public static final String BALANCE_LOOKUP = "card-balance-lookup";

    static final String RC_APPROVED = "00";
    static final String RC_INVALID_CARD = "14";
    static final String RC_INSUFFICIENT_FUNDS = "51";
    static final String RC_FORMAT_ERROR = "30";
    static final String RC_SYSTEM_ERROR = "96";

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        try {
            if (!"0200".equals(isoMsg.getMTI())) {
                return false; // laisse d'autres listeners / la file "unhandled" s'en occuper
            }

            ISOMsg reply = (ISOMsg) isoMsg.clone();
            reply.setResponseMTI();
            authorize(isoMsg, reply);

            log.info("0200 STAN={} PAN={} -> 39={} 125={}",
                    isoMsg.getString(11), ISOUtil.protect(isoMsg.getString(2)),
                    reply.getString(39), reply.getString(125));

            isoSource.send(reply);
            return true;
        } catch (ISOException | IOException e) {
            log.error("Erreur lors du traitement du message ISO", e);
            return false;
        }
    }

    private void authorize(ISOMsg request, ISOMsg reply) throws ISOException {
        String pan = request.getString(2);
        String amountField = request.getString(4);

        if (pan == null || amountField == null || !amountField.matches("\\d{1,12}")) {
            setResult(reply, RC_FORMAT_ERROR, "format_invalide");
            return;
        }

        Double cardBalance;
        try {
            @SuppressWarnings("unchecked")
            Function<String, Double> lookup = (Function<String, Double>) NameRegistrar.get(BALANCE_LOOKUP);
            cardBalance = lookup.apply(pan);
        } catch (NameRegistrar.NotFoundException | RuntimeException e) {
            log.error("Erreur d'accès à la base pour PAN={}", ISOUtil.protect(pan), e);
            setResult(reply, RC_SYSTEM_ERROR, "erreur_systeme");
            return;
        }

        if (cardBalance == null) {
            setResult(reply, RC_INVALID_CARD, "introuvable");
            return;
        }

        // Champ 4 = montant en unités mineures (centimes) sur 12 chiffres
        BigDecimal requested = new BigDecimal(amountField).movePointLeft(2);
        BigDecimal balance = BigDecimal.valueOf(cardBalance);

        if (requested.compareTo(balance) <= 0) {
            setResult(reply, RC_APPROVED, "success");
        } else {
            setResult(reply, RC_INSUFFICIENT_FUNDS, "insuffisant");
        }
    }

    private static void setResult(ISOMsg reply, String responseCode, String state) throws ISOException {
        reply.set(39, responseCode);
        reply.set(125, state);
    }
}
