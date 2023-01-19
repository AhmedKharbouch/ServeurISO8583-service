package com.example.isoserveurservice.repositories;

import com.example.isoserveurservice.entities.Isomsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IsomsgRepository extends JpaRepository<Isomsg,Long > {

    Isomsg findByCardNumber(String card);
}
