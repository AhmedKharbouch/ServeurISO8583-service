package com.example.isoserveurservice.services;

import com.example.isoserveurservice.entities.Isomsg;
import com.example.isoserveurservice.repositories.IsomsgRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IsomsgServiceImpl implements IsomsgService {

    @Autowired
    private IsomsgRepository isomsgRepository;


    @Override
    public List<Isomsg> listmsgs() {
        return isomsgRepository.findAll();
    }

    @Override
    public Isomsg saveIsomsg(Isomsg isomsg) {
        return isomsgRepository.save(isomsg);
    }

    @Override
    public Isomsg getIsomsg(String c) {
        return isomsgRepository.findByCardNumber(c);
    }
}
