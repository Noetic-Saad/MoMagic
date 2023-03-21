package com.example.moreceiverapp.controllers;

import com.example.moreceiverapp.dtos.JmsMessage;
import com.example.moreceiverapp.services.ProcessSMSService;
import com.example.moreceiverapp.services.impl.ProcessSMSServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class MoController {

    private final ProcessSMSService processSMSService;
    private static final Logger log = LoggerFactory.getLogger(MoController.class);


    public MoController(ProcessSMSService processSMSRequest) {
        this.processSMSService = processSMSRequest;
    }

    @RequestMapping(
            path = "interface/moreceiver",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public @ResponseBody ResponseEntity<?> getObject(@RequestBody MultiValueMap<Object,Object> object) {
        System.out.println("Start");
        JmsMessage moDTO = new JmsMessage();
        moDTO.setSmsText((String) object.get("data").get(0));
        moDTO.setSmsId((Integer.parseInt((String)object.get("transId").get(0))));
        moDTO.setConnectionPointName((String)object.get("connPointName").get(0));
        moDTO.setMsisdn((String)object.get("msisdn").get(0));
        moDTO.setShortcode(((String) object.get("shortcode").get(0)));
        moDTO.setConnectivityPointId((Integer.parseInt((String) object.get("operatorId").get(0))));
        log.info("------------------Request Received ---" + moDTO.toString());

        log.info("========Processing Request for | " + moDTO.getMsisdn()+"========");

        JmsMessage moResponse2=   processSMSService.processSMSRequest(moDTO);

        return new ResponseEntity<>("isaccpeted="+moResponse2.getIsAccepted().toString(), HttpStatus.OK);
    }
}