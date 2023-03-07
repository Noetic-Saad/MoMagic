package com.example.moreceiverapp.services.impl;

import com.example.moreceiverapp.dtos.ChargeRequestDTO;
import com.example.moreceiverapp.dtos.JmsMessage;
import com.example.moreceiverapp.dtos.TodayChargedDTO;
import com.example.moreceiverapp.models.BlacklistEntity;
import com.example.moreceiverapp.models.ChargingEntity;
import com.example.moreceiverapp.models.TodayChargedEntity;
import com.example.moreceiverapp.repos.TodayChargedMsisdnRepository;
import com.example.moreceiverapp.responses.AppResponse;
import com.example.moreceiverapp.services.BlacklistService;
import com.example.moreceiverapp.services.ChargingService;
import com.example.moreceiverapp.services.ProcessSMSService;
import com.example.moreceiverapp.services.TodayChargedMsisdnService;
import com.example.moreceiverapp.utils.Constants;
import com.example.moreceiverapp.utils.HTTPSDPMT;
import com.example.moreceiverapp.utils.HttpRequestHandler;
import com.example.moreceiverapp.utils.JazzWaridChargeRequest;
import com.example.moreceiverapp.utils.ZongMMLRequest;
import com.example.moreceiverapp.utils.ZongRequestProcessing;
import com.example.moreceiverapp.utils.ZongSecondRequestProcessing;
import com.google.gson.Gson;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProcessSMSServiceImpl implements ProcessSMSService {

    private static final Logger log = LoggerFactory.getLogger(ProcessSMSServiceImpl.class.getName());

    private final BlacklistService blacklist;
    @Autowired
    private final TodayChargedMsisdnRepository repository;
    private final ChargingService chargingService;
    private final TodayChargedMsisdnService todayChargedMsisdnService;
    private final ModelMapper modelMapper;

    private static final int UFONE_ID = 30;
    private static final int TELENOR_ID = 20;
    private static final int ZONG_ID = 50;
    private static final int MOBILINK_ID = 10;
    private static final int WARID_ID = 40;
    private static ChargingEntity mydao = null;
    private static TodayChargedEntity mydao2 = null;
    private TodayChargedDTO todayChargedDTO = new TodayChargedDTO();
    private final ZongRequestProcessing zongRequestProcessing;
    private final ZongSecondRequestProcessing zongSecondRequestProcessing;

    @Autowired
    public ProcessSMSServiceImpl(BlacklistService blacklistService, TodayChargedMsisdnRepository repository, ChargingService chargingService, TodayChargedMsisdnService todayChargedMsisdnService, ModelMapper modelMapper, ZongRequestProcessing zongRequestProcessing, ZongSecondRequestProcessing zongSecondRequestProcessing) {
        this.blacklist = blacklistService;
        this.repository = repository;
        this.chargingService = chargingService;
        this.todayChargedMsisdnService = todayChargedMsisdnService;
        this.modelMapper = modelMapper;
        this.zongRequestProcessing = zongRequestProcessing;
        this.zongSecondRequestProcessing = zongSecondRequestProcessing;
    }
    @Override
    //........................................................................................................................

    public JmsMessage processSMSRequest(JmsMessage incomingPacket) {
        JmsMessage moMessageObj = new JmsMessage();

        int charging_mechanism = 1;
        ZongMMLRequest zongMMLRequest = new ZongMMLRequest();
        JazzWaridChargeRequest dot_handler = null;
        HttpResponse httpDotRes = null; //Get Response from Dot Request
        ZongRequestProcessing zongRequestProcessing = new ZongRequestProcessing(blacklist, repository, chargingService, todayChargedMsisdnService, modelMapper);

        HttpRequestHandler requestHandler = null; //Set to Partner Request
        Response response = null; //Get Partner Response

        try {
            int connectivityPointId = incomingPacket.getConnectivityPointId().intValue();
//---------------------------------------------------Ufone Start Code------------------------------------------------------

            if ((connectivityPointId == 71) || (connectivityPointId == 5) || (connectivityPointId == 6) || (connectivityPointId == 74)) {
                connectivityPointId = this.UFONE_ID;
                log.info("PROCESS FOR UFONE REQUEST | "+incomingPacket.getMsisdn());

                requestHandler = new HttpRequestHandler(incomingPacket.getMsisdn(),
                        incomingPacket.getShortcode(),
                        incomingPacket.getSmsText(),
                        String.valueOf(connectivityPointId),
                        String.valueOf(incomingPacket.getSmsId()), //Used as Transaction ID
                        String.valueOf(incomingPacket.getConnectivityPointId()));


                response = requestHandler.sendRequest(false);

                requestHandler = null;

                log.info("MSISDN: " + incomingPacket.getMsisdn() + " OperatorId: " + connectivityPointId);

                if (response.isSuccessful()) {
                    String replymt = response.body().string();
                    HTTPSDPMT mthttp = new HTTPSDPMT(incomingPacket.getMsisdn(),
                            incomingPacket.getShortcode(), replymt);
                    HttpResponse mtResponse = null;
                    mtResponse = mthttp.sendRequest(false);
                    // System.out.println(replymt);

                    mthttp = null;
//                    log.info("The SMS Delivery Status" + mtResponse.getStatusLine().getStatusCode()
//                            + mtResponse.getStatusLine().getReasonPhrase());
                    log.info("The SMS Delivery Status | " + mtResponse.getStatusLine().getStatusCode() +" | "+ mtResponse.getStatusLine().getReasonPhrase());
                    log.info("SMS-ID| " + incomingPacket.getSmsId() + " | ACCEPTED");
                    moMessageObj.setIsAccepted(Boolean.valueOf(true));
                }
            }
            //---------------------------------------------------Telenor Start Code------------------------------------------------------

            else if ((connectivityPointId == 77) || (connectivityPointId == 78) || (connectivityPointId == 79) || (connectivityPointId == 106)) {
                connectivityPointId = this.TELENOR_ID;
                log.info("PROCESS FOR TELENOR REQUEST | "+incomingPacket.getMsisdn());

                requestHandler = new HttpRequestHandler(incomingPacket.getMsisdn(),
                        incomingPacket.getShortcode(),
                        incomingPacket.getSmsText(),
                        String.valueOf(connectivityPointId),
                        String.valueOf(incomingPacket.getSmsId()),
                        String.valueOf(incomingPacket.getConnectivityPointId()));


                response = requestHandler.sendRequest(false);

                requestHandler = null;

                log.info("MSISDN: " + incomingPacket.getMsisdn() + " OperatorId: " + connectivityPointId);

                if (response.isSuccessful()) {
                    String replymt = response.body().string();
                    HTTPSDPMT mthttp = new HTTPSDPMT(incomingPacket.getMsisdn(),
                            incomingPacket.getShortcode(), replymt);
                    HttpResponse mtResponse = null;
                    mtResponse = mthttp.sendRequest(false);
                    //   System.out.println(replymt);

                    mthttp = null;
//                    log.info("The SMS Delivery Status" + mtResponse.getStatusLine().getStatusCode()
//                            + mtResponse.getStatusLine().getReasonPhrase());
                    log.info("The SMS Delivery Status | " + mtResponse.getStatusLine().getStatusCode() +" | "+ mtResponse.getStatusLine().getReasonPhrase());
                    log.info("SMS-ID| " + incomingPacket.getSmsId() + " | ACCEPTED");
                    moMessageObj.setIsAccepted(Boolean.valueOf(true));

                }
            }
            else if ((connectivityPointId == 10) || (connectivityPointId == 30) || (connectivityPointId == 142)) {
                //----------------------------------------Zong Code Start -------------------------------------------

                connectivityPointId = this.ZONG_ID;
                requestHandler = new HttpRequestHandler(incomingPacket.getMsisdn(),
                        incomingPacket.getShortcode(),
                        incomingPacket.getSmsText(),
                        String.valueOf(connectivityPointId),
                        String.valueOf(incomingPacket.getSmsId()),
                        String.valueOf(incomingPacket.getConnectivityPointId()));

                String msisdn = incomingPacket.getMsisdn();
                String new_subscriberNumber2 = incomingPacket.getMsisdn().substring(2);
                log.info("PROCESS FOR ZONG REQUEST | "+new_subscriberNumber2);
                log.info("Checking MSISDN in Today Billing for | " + new_subscriberNumber2);
                TodayChargedEntity isNumberChargedToday = todayChargedMsisdnService.findMsisdnInTodayTblBilling(new_subscriberNumber2);
                if(isNumberChargedToday == null){
                    log.info("MSISDN | "+new_subscriberNumber2+" | NOT RECORD FOUND FIRST ATTEMPT | ");
                    zongRequestProcessing.processZongRequest(incomingPacket);
                } else{
                    log.info("MSISDN | "+new_subscriberNumber2+" | RECORD FOUND IN TODAYTABLE | RESPONSECODE="+isNumberChargedToday.getResponsecode());
                    if(isNumberChargedToday.getAttempt() < 5 && isNumberChargedToday.getResponsecode() ==  0  ) {
                        log.info("MSISDN | "+new_subscriberNumber2+" | NEXT ATTEMPT | NUMOFATTEMPT "+isNumberChargedToday.getAttempt());
                        zongSecondRequestProcessing.processZongRequest(incomingPacket,isNumberChargedToday);
                    }
                    else if(isNumberChargedToday.getResponsecode() == 124) {
                        log.info("MSISDN | "+ new_subscriberNumber2 + " | BILLING WILL NOT BE ATTEMPTED " +
                                "BECAUSE RESPONSECODE IS 124");

                    } else if (isNumberChargedToday.getAttempt() >= 5 ) {
                        log.info("MSISDN | "+ new_subscriberNumber2 + " | BILLING ATTEMPTS EXCEEDED");
                    }
                }
                //----------------------------------------Zong Code end -------------------------------------------

//---------------------------------------------------Jazz Start Code------------------------------------------------------


            } else if (connectivityPointId == 100 || connectivityPointId == 101 || connectivityPointId == 94 || connectivityPointId == 140 || connectivityPointId == 141 || connectivityPointId == 146) {

                if (connectivityPointId == 140 || connectivityPointId == 141 || connectivityPointId == 146) {
                    connectivityPointId = this.WARID_ID;
                } else {
                    connectivityPointId = this.MOBILINK_ID;
                }
                log.info("PROCESS FOR JAZZ REQUEST | "+incomingPacket.getMsisdn());
                long blid = 0;
                int status = 0;

                BlacklistEntity bl = blacklist.checkMsisdn(incomingPacket.getMsisdn());

                if (bl != null) {
                    blid = bl.getId();
                    status = bl.getStatuscode();
                    log.info(incomingPacket.getMsisdn() + " is Blacklisted. Exiting Application");
                } else {
                    log.info( "" + incomingPacket.getMsisdn() + " is not Blacklisted. Sending Charging Request");
                    // log.info("status = "+ status + ", Blacklist ID = "+blid);
                    if (status == 0 && blid == 0) {
                        String new_subscriberNumber = incomingPacket.getMsisdn().substring(2);
                        String shortcode = String.valueOf(incomingPacket.getShortcode());
                        double adjustmentAmountRelative = 0;

                        Integer shortcode1 = Integer.valueOf(shortcode);

                        if (shortcode1 == 3441)
                            adjustmentAmountRelative = 239;
                        if (shortcode1 == 3443)
                            adjustmentAmountRelative = 598;
                        if (shortcode1 == 3444)
                            adjustmentAmountRelative = 1195;
                        if (shortcode1 == 3445)
                            adjustmentAmountRelative = 2988;

                        if(new_subscriberNumber.equalsIgnoreCase("3033889888")){
                            adjustmentAmountRelative = 100;
                        }

                        // log.info("Deducted Amt = " + adjustmentAmountRelative);
                        if(new_subscriberNumber.equalsIgnoreCase("3020527300")){
                            adjustmentAmountRelative = 1195;
                        }

                        long startTime = System.nanoTime();// this line before you call UCIP API

                        log.info("Start Time before UCIP Call - "+ startTime + " Thread Name " + Thread.currentThread().getName());

                        dot_handler = new JazzWaridChargeRequest();
                        ChargeRequestDTO chargeRequestDTO = getChargeRequestBody(adjustmentAmountRelative,new_subscriberNumber,incomingPacket.getSmsId(),connectivityPointId);
                        Response ucipResponse = dot_handler.sendChargingRequest(chargeRequestDTO);

                        long endTime = System.nanoTime();// this after you receive any response whether success failure etc


                        Gson gson = new Gson();
                        String jsonResponse = ucipResponse.body().string();
                        //  System.out.println("ucipResponse = " + jsonResponse);
                        AppResponse appResponse = gson.fromJson(jsonResponse,AppResponse.class);

                        log.info("End Time After UCIP Call - "+ endTime + " Thread Name " + Thread.currentThread().getName());
                        if(appResponse == null){
                            //System.out.println("chargeRequestDTO = " + gson.toJson(chargeRequestDTO));
                        }

                        log.info("Running Time Of  UCIP Call - "+ ((endTime-startTime)/1000) + " Thread Name " + Thread.currentThread().getName());

                        if(appResponse.getCode()== Constants.IS_POSTPAID && ucipResponse.isSuccessful()) {

                            String[] splited = incomingPacket.getSmsText().split("\\s+");
                            String SCkeyword = splited[0];

                            mydao = new ChargingEntity(incomingPacket.getSmsId(), new_subscriberNumber, String.valueOf(incomingPacket.getSmsId()),
                                    7000, incomingPacket.getShortcode(),
                                    SCkeyword, 0, Constants.PARTNER_ID, 7000, 0, adjustmentAmountRelative,
                                    incomingPacket.getConnectivityPointId(), incomingPacket.getSmsText(), charging_mechanism, 1);

                            chargingService.insertRecord(mydao);

                            if (ucipResponse == null)
                                log.info("Null Value Received in UCIP Response");
                            else
                                log.info("The UCIP Response=" + appResponse.getCode());
                        } else {

                            int responseCode = -1;
                            String TransID = "";
                            if (appResponse != null) {
                                responseCode = appResponse.getCode(); // ResponseCode
                                TransID = appResponse.getTransID();
                            }

                            log.info("subscribeNumber : " + new_subscriberNumber + ",  SC : " + shortcode + ", amount : "
                                    + adjustmentAmountRelative + ", StatusCode : "
                                    + appResponse.getCode() + ", TransactionID : " + TransID
                                    + ", ResponseCode : " + responseCode);

                            String[] splited = incomingPacket.getSmsText().split("\\s+");
                            String SCkeyword = splited[0];


                            if (responseCode == 0 && ucipResponse.isSuccessful()) {
                                // Insert into Charging DB
                                // For PCom
                                mydao = new ChargingEntity(incomingPacket.getSmsId(), new_subscriberNumber, TransID,
                                        responseCode, incomingPacket.getShortcode(),
                                        SCkeyword, 1, Constants.PARTNER_ID, responseCode, 0, adjustmentAmountRelative,
                                        incomingPacket.getConnectivityPointId(), incomingPacket.getSmsText(), charging_mechanism,0);
                                // For Centili
                                // mydao = new charging(incomingPacket.getSmsId(),new_subscriberNumber,TransID,
                                // httpDotRes.getStatusLine().getStatusCode(),incomingPacket.getShortcode(),
                                // SCkeyword,1,2,responseCode,0,adjustmentAmountRelative,incomingPacket.getConnectivityPointId(),incomingPacket.getSmsText());
                                chargingService.insertRecord(mydao);

                                log.info("SMS-ID:  " + incomingPacket.getSmsId() + ", MSISDN: " + incomingPacket.getMsisdn()
                                        + " UCIP Transaction of " + (adjustmentAmountRelative / 100) + " is Charged");
                                requestHandler = new HttpRequestHandler(incomingPacket.getMsisdn(),
                                        incomingPacket.getShortcode(), incomingPacket.getSmsText(),
                                        String.valueOf(connectivityPointId), String.valueOf(incomingPacket.getSmsId()),
                                        String.valueOf(incomingPacket.getConnectivityPointId()));
                                response = requestHandler.sendRequest(false);
                                requestHandler = null;

                                if (response.isSuccessful()) {
                                    String replymt = response.body().string();
                                    HTTPSDPMT mthttp = new HTTPSDPMT(incomingPacket.getMsisdn(),
                                            incomingPacket.getShortcode(), replymt);
                                    HttpResponse mtResponse = null;
                                    mtResponse = mthttp.sendRequest(false);
                                    // System.out.println(replymt);

                                    mthttp = null;
//                                    log.info("The SMS Delivery Status" + mtResponse.getStatusLine().getStatusCode()
//                                            + mtResponse.getStatusLine().getReasonPhrase());
                                    log.info("The SMS Delivery Status | " + mtResponse.getStatusLine().getStatusCode() +" | "+ mtResponse.getStatusLine().getReasonPhrase());

                                    log.info("SMS-ID| " + incomingPacket.getSmsId() + " | ACCEPTED");
                                    moMessageObj.setIsAccepted(Boolean.valueOf(true));

                                }
                            } else {
                                // Insert into Charging DB
                                // For TPay
                                mydao = new ChargingEntity(incomingPacket.getSmsId(), new_subscriberNumber, TransID,
                                        responseCode, incomingPacket.getShortcode(),
                                        SCkeyword, 1, Constants.PARTNER_ID, responseCode, 0, adjustmentAmountRelative,
                                        incomingPacket.getConnectivityPointId(), incomingPacket.getSmsText(), charging_mechanism, 0);
                                // For Centili
                                // mydao = new charging(incomingPacket.getSmsId(),new_subscriberNumber,TransID,
                                // httpDotRes.getStatusLine().getStatusCode(),incomingPacket.getShortcode(),
                                // SCkeyword,1,2,responseCode,0,adjustmentAmountRelative,incomingPacket.getConnectivityPointId(),incomingPacket.getSmsText());
                                chargingService.insertRecord(mydao);
                                if (appResponse == null)
                                    log.info("Null Value Received in UCIP Response");
                                else
                                    log.info("The UCIP Response=" + appResponse.getCode());
                            }
                        }


                        dot_handler = null;
                        mydao = null;

                    }
                }
            }

            else {
                connectivityPointId = incomingPacket.getConnectivityPointId().intValue();
                log.info("Invalid Connection Point ID");
            }

            log.info("======== Request Processed for MSISDN | " + incomingPacket.getMsisdn() + "========");
            return moMessageObj;

        } catch (Throwable e) {
            log.error(String.valueOf(e), e);

        }

        moMessageObj.setIsAccepted(Boolean.valueOf(false));
        return moMessageObj;
    }

    //.........................................................................................................................


    private static ChargeRequestDTO getChargeRequestBody(double adjustmentAmountRelative, String new_subscriberNumber, Integer smsId, int connectivityPointId) {
        ChargeRequestDTO chargeRequestDTO = new ChargeRequestDTO();
        chargeRequestDTO.setAmount(String.valueOf(adjustmentAmountRelative));
        chargeRequestDTO.setMsisdn(new_subscriberNumber);
        chargeRequestDTO.setOperatorId(connectivityPointId);
        chargeRequestDTO.setTransactionId(String.valueOf(smsId));
        return chargeRequestDTO;
    }
}