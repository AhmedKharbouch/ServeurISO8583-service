package com.example.isoserveurservice.iso;

import lombok.extern.slf4j.Slf4j;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.iso.*;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@Slf4j
@CrossOrigin("*")
public class SwitchRequestListener implements ISORequestListener , Configurable{

    @Bean
    public Q2 q2(){
        Q2 q2 = new Q2();
        q2.start();
        return q2;

        }

    private Map<String,String> routeTable;


    @Override
    public void setConfiguration(Configuration configuration) {
        routeTable= new HashMap<>();
        routeTable.put("8001", "server_1");
        routeTable.put("8002", "server_2");
    }

    public String getIsoMsg(String s){
       String var="";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Iso_BASE","root","");
            PreparedStatement pstm  = conn.prepareStatement( "SELECT card_number, amount, code_currency " +
                    "FROM isomsg WHERE card_number=?");

            pstm.setString(1,s);

            ResultSet rs    = pstm.executeQuery();


            while (rs.next()) {
                System.out.println(rs.getString("amount"));
                var= rs.getString("amount");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return var;
    }
  private MUX mux;
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        try {
            mux = QMUX.getMUX("server_1-mux");
        } catch (NameRegistrar.NotFoundException e) {
            throw new RuntimeException(e);
        }
        /*try {
            if (isoMsg.getMTI().equals("0200")) {*/
                ISOMsg reply = (ISOMsg) isoMsg.clone();
                reply.set(39, "te");
                System.out.println(reply.getString(11));
        try {
            reply.setResponseMTI();
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("isoMsg = " + isoMsg);

                System.out.println(isoSource.isConnected());
        ISOMsg reply1 = null;
        try {
            reply1 = mux.request(reply, 100);
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("reply1 = " + reply1);
           /* }
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }*/
        return false;
    }

 /*   @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {


        *//*try {
            if(isoMsg.getMTI().equals("0200")){
                Thread t = new Thread(new Processor(isoSource,isoMsg));
                t.start();
                return true;
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
        return false;*//*



        try {

            if (isoMsg.getMTI().equals("0200")) {
                ISOMsg reply = (ISOMsg) isoMsg.clone();
                reply.setResponseMTI();

                String card_num=reply.getString(2);
                String expirydate=reply.getString(14);
                //function to get amount

                reply.set(39, "00");

                if(getIsoMsg(card_num).equals("")){
                    reply.set(125,"introuvable");
                }else {
                    String s1=reply.getString(4);
                    String s2=getIsoMsg(card_num);

                    double v = Double.parseDouble(s1);
                    double v1 = Double.parseDouble(s2);
                    System.out.println("value of amount iso*******: "+v);
                    System.out.println("value of amount database *******: "+v1);


                    if (v<=v1) {
                        reply.set(125, "success");
                    } else {
                        reply.set(125, "insuffisant");
                    }
                }




                isoSource.send(reply);
                return true;
            }
            // send request to server B
            MUX mux = (MUX) NameRegistrar.getIfExists("server_1-mux");
            ISOMsg reply = mux.request(isoMsg, 10 * 1000);
            if (reply != null) {
                System.out.println(new String(reply.pack()));
                reply.set(125, "RESPONSE FROM SERVER ");
                isoSource.send(reply);
            }
        } catch (ISOException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

}





