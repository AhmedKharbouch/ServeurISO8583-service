package com.example.isoserveurservice.web;

import com.example.isoserveurservice.entities.Isomsg;
import com.example.isoserveurservice.services.IsomsgService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class IsomsgRestController {

    private IsomsgService isomsgService;

    @GetMapping()
    public Isomsg getIsoMsg(){

        return isomsgService.getIsomsg("123456");

    }



}
