package com.example.isoserveurservice.config;

import com.example.isoserveurservice.entities.Isomsg;
import com.example.isoserveurservice.iso.SwitchRequestListener;
import com.example.isoserveurservice.services.IsomsgService;
import org.jpos.q2.Q2;
import org.jpos.util.NameRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class JposConfig {

    /**
     * Démarre Q2 (QServers + MUX de deploy/) et l'arrête proprement avec Spring.
     * <p>
     * jPOS instancie lui-même les request listeners (hors Spring, et avec un autre
     * classloader que devtools), donc pas d'@Autowired possible. On publie la
     * recherche de solde dans le NameRegistrar de jPOS, en n'exposant que des
     * types JDK (Function&lt;String, Double&gt;) pour rester indépendant du classloader.
     */
    @Bean(destroyMethod = "shutdown")
    public Q2 q2(IsomsgService isomsgService) {
        Function<String, Double> balanceLookup = pan -> {
            Isomsg card = isomsgService.getIsomsg(pan);
            return card == null ? null : card.getAmount();
        };
        NameRegistrar.register(SwitchRequestListener.BALANCE_LOOKUP, balanceLookup);

        Q2 q2 = new Q2("deploy");
        q2.start();
        return q2;
    }
}
