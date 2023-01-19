package com.example.isoserveurservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Isomsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String cardNumber;
    @JsonFormat(pattern="yyyy-MM")
    Date expireDate ;
    double amount;
    String code_Currency;
    String date_transaction;
    String point_de_service;
}
