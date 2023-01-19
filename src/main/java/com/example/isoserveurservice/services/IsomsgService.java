package com.example.isoserveurservice.services;

import com.example.isoserveurservice.entities.Isomsg;

import java.util.List;

public interface IsomsgService {
    List<Isomsg> listmsgs();
    Isomsg saveIsomsg(Isomsg isomsg);
    Isomsg getIsomsg(String card);
}
