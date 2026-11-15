package com.example.isoserveurservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Même table que l'entité Isomsg du client : les types doivent rester identiques
 * (expireDate en String "yyyy-MM") sinon les deux services se battent sur le schéma.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Isomsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String cardNumber;
    String expireDate;
    double amount;
    String code_Currency;
    String date_transaction;
    String point_de_service;
}
