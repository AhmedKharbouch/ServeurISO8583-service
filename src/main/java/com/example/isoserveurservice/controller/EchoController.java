package com.example.isoserveurservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.MUX;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.web.bind.annotation.PostMapping;

//@RestController
//@Slf4j
public class EchoController {


    @PostMapping("/echo")
    public void echo() throws NameRegistrar.NotFoundException, ISOException {
        MUX mux = QMUX.getMUX("s-mux");
        ISOMsg msg = new ISOMsg();
        msg.setMTI("0200");
        msg.set(11,"000001");
        msg.set(70,"301");
        ISOMsg respMsg = mux.request(msg, 2000);
        //log.info("RespMsg {}",respMsg);
    }

}
