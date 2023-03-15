package com.example.moreceiverapp.utils;

import com.example.moreceiverapp.dtos.JmsMessage;
import com.example.moreceiverapp.dtos.MoResponse;
import com.example.moreceiverapp.dtos.TodayChargedDTO;
import com.example.moreceiverapp.models.BlacklistEntity;
import com.example.moreceiverapp.models.ChargingEntity;
import com.example.moreceiverapp.models.TodayChargedEntity;
import com.example.moreceiverapp.repos.TodayChargedMsisdnRepository;
import com.example.moreceiverapp.services.BlacklistService;
import com.example.moreceiverapp.services.ChargingService;
import com.example.moreceiverapp.services.TodayChargedMsisdnService;
import com.example.moreceiverapp.services.impl.ProcessSMSServiceImpl;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class ZongSecondRequestProcessing {
    private static final Logger log = LoggerFactory.getLogger(ProcessSMSServiceImpl.class.getName());

    @Autowired
    private BlacklistService blacklist;
    @Autowired
    private TodayChargedMsisdnRepository repository;
    @Autowired
    private ChargingService chargingService;
    @Autowired
    private TodayChargedMsisdnService todayChargedMsisdnService;
    @Autowired
    private ModelMapper modelMapper;

    private static final int UFONE_ID = 30;
    private static final int TELENOR_ID = 20;
    private static final int ZONG_ID = 50;
    private static final int MOBILINK_ID = 10;
    private static final int WARID_ID = 40;
    private static ChargingEntity mydao = null;
    private static TodayChargedEntity mydao2 = null;
    private TodayChargedDTO todayChargedDTO = new TodayChargedDTO();

    public ZongSecondRequestProcessing() {}
    public ZongSecondRequestProcessing(BlacklistService blacklist, TodayChargedMsisdnRepository repository, ChargingService chargingService, TodayChargedMsisdnService todayChargedMsisdnService, ModelMapper modelMapper) {
        this.blacklist = blacklist;
        this.repository = repository;
        this.chargingService = chargingService;
        this.todayChargedMsisdnService = todayChargedMsisdnService;
        this.modelMapper = modelMapper;
    }


    public void processZongRequest(JmsMessage incomingPacket,TodayChargedEntity todayChargedEntity) throws IOException {
        JmsMessage moMessageObj = new JmsMessage();
        String msisdn = incomingPacket.getMsisdn();
        int connectivityPointId = incomingPacket.getConnectivityPointId().intValue();
        String scKeyword2 = null;
        int charging_mechanism = 1;
        String[] splited2 = incomingPacket.getSmsText().split("\\s+");
        scKeyword2 = splited2[0];
        log.info("Starting Process for Charging ");
        String new_subscriberNumber2 = incomingPacket.getMsisdn().substring(2);
        HttpRequestHandler requestHandler = null; //Set to Partner Request
        Response response = null; //Get Partner Response
        JazzWaridChargeRequest dot_handler = null;
        if((connectivityPointId == 10) || (connectivityPointId == 30) || (connectivityPointId == 142)){
            connectivityPointId=ZONG_ID;
        }


        long blid = 0;
        int status = 0;
        BlacklistEntity bl = null;
        String serviceId = null;
        bl = blacklist.checkMsisdn(incomingPacket.getMsisdn());

        if (bl != null) {
            blid = bl.getId();
            status = bl.getStatuscode();
            log.info(incomingPacket.getMsisdn() + " is Blacklisted. Exiting Application");
        } else {
            //-------------------insert record in today_tbl_charging----------------------
            log.info(incomingPacket.getMsisdn() + " is not Blacklisted. Sending Charging Request");
            // log.info("status = "+ status + ", Blacklist ID = "+blid);
            if (status == 0 && blid == 0) {
                String new_subscriberNumber = incomingPacket.getMsisdn().substring(2);
                String shortcode = String.valueOf(incomingPacket.getShortcode());
                Integer adjustmentAmountRelative = 0;
                Integer amount = 0;

                Integer shortcode1 = Integer.valueOf(shortcode);

                if (shortcode1 == 3441) {
                    adjustmentAmountRelative = 200;
                    amount = 239;
                    serviceId = "Noet01";
                } else if (shortcode1 == 3443) {
                    adjustmentAmountRelative = 500;
                    serviceId = "Noet05";
                    amount = 598;
                } else if (shortcode1 == 3444) {
                    adjustmentAmountRelative = 1000;
                    serviceId = "Noet10";
                    amount = 1159;
                } else if (shortcode1 == 3445) {
                    adjustmentAmountRelative = 2500;
                    serviceId = "Noet25";
                    amount = 2988;
                }
                String number = "";
                if (msisdn.startsWith("92")) {
                    number = msisdn;
                } else if (msisdn.startsWith("03")) {
                    number = msisdn.replaceFirst("03", "92");
                } else if (msisdn.startsWith("3")) {
                    number = "92" + msisdn;
                }


                String scKeyword = null;
                String[] splited = incomingPacket.getSmsText().split("\\s+");
                scKeyword = splited[0];
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n    \"msisdn\":" + number + "," +
                        "\n    \"amount\":" + adjustmentAmountRelative + ",\n    \"operatorId\":" + connectivityPointId + ",\n    \"transactionid\":\"+serviceId+\"\n}");
                Request request = new Request.Builder()
                        .url("http://192.168.127.57:9080/chargezong")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Response resp = client.newCall(request).execute();
                String resStr = resp.body().string();
                JSONObject json = new JSONObject(resStr);
                MoResponse data = new Gson().fromJson(String.valueOf(json), MoResponse.class);

                String code = Integer.toString(data.getCode());
                log.info("CHARGING | ZONGCHARGING CLASS | ZONG MML RESPONSE CODE | " + code);

                if (code.equalsIgnoreCase("0000") || code.equalsIgnoreCase("0") || code.equalsIgnoreCase("000")) {
                    // Insert into Charging DB
                    // For TPay
                    mydao = new ChargingEntity(
                            incomingPacket.getSmsId(),
                            new_subscriberNumber,
                            String.valueOf(incomingPacket.getSmsId()),
                            200,
                            incomingPacket.getShortcode(),
                            scKeyword,
                            1,
                            Constants.PARTNER_ID,
                            0,
                            1,
                            amount,
                            incomingPacket.getConnectivityPointId(),
                            incomingPacket.getSmsText(),
                            charging_mechanism, 0);
                    chargingService.insertRecord(mydao);
//                    todayChargedDTO = modelMapper.map(mydao, TodayChargedDTO.class);
                    todayChargedMsisdnService.updateRecord(todayChargedDTO, todayChargedEntity.getId());
                    log.info("UPDATED RECORD in today_tbl_billing FOR MSISDN | " + todayChargedDTO.getSubscriberNumber());

                    // Insert into Today TBL Charging DB
//                                mydao2= new TodayChargedEntity(incomingPacket.getSmsId(),
//                                        new_subscriberNumber, String.valueOf(incomingPacket.getSmsId()),
//                                        200, incomingPacket.getShortcode(),
//                                        scKeyword, 1, Constants.PARTNER_ID, 0, 0, amount,
//                                        incomingPacket.getConnectivityPointId(), incomingPacket.getSmsText(), charging_mechanism, 0);
//
//                                todayChargedMsisdnService.insertRecord(mydao2);


                    requestHandler = new HttpRequestHandler(incomingPacket.getMsisdn(),
                            incomingPacket.getShortcode(), incomingPacket.getSmsText(),
                            String.valueOf(connectivityPointId), String.valueOf(incomingPacket.getSmsId()),
                            String.valueOf(incomingPacket.getConnectivityPointId()));
                    response = requestHandler.sendRequest(false);

                    requestHandler = null;

                    log.info("SENDING MT FOR MSISDN | " + incomingPacket.getMsisdn() + " OperatorId: " + connectivityPointId);

                    if (response.code() == 200 || response.isSuccessful()) {
                        //HttpEntity entity = httpResponse.getEntity();
                        //String xmlResponse = EntityUtils.toString(entity);

                        //String[] resp1 = xmlResponse1.split("&");
                        // String replymt = xmlResponse; // resp1[2].substring(5);
                        String replymt = response.body().string().replace("{", "").replace("}", "").replace("[", "").replace("]", "");

                        HTTPSDPMT mthttp = new HTTPSDPMT(incomingPacket.getMsisdn(),
                                incomingPacket.getShortcode(),
                                replymt.replaceAll("^\"|\"$", ""));
                        HttpResponse mtResponse = null;
                        mtResponse = mthttp.sendRequest(false);
                        mthttp = null;
                        log.info("The SMS Delivery Status | " + mtResponse.getStatusLine().getStatusCode() +" | "+ mtResponse.getStatusLine().getReasonPhrase());
                        log.info("SMS-ID| " + incomingPacket.getSmsId() + " | ACCEPTED");
                        moMessageObj.setIsAccepted(Boolean.valueOf(true));

                    }
                } else {
                    mydao = new ChargingEntity(incomingPacket.getSmsId(), new_subscriberNumber, String.valueOf(incomingPacket.getSmsId()),
                            500, incomingPacket.getShortcode(),
                            scKeyword, 0, Constants.PARTNER_ID, Integer.parseInt(code), 1, amount,
                            incomingPacket.getConnectivityPointId(), incomingPacket.getSmsText(), charging_mechanism, 0);
                    chargingService.insertRecord(mydao);
                    todayChargedDTO = modelMapper.map(mydao, TodayChargedDTO.class);
                    todayChargedMsisdnService.updateRecord(todayChargedDTO, todayChargedEntity.getId());
                    log.info("UPDATED RECORD in today_tbl_billing FOR MSISDN | " + todayChargedDTO.getSubscriberNumber());
                    if (code == null)
                        log.info("Null Value Received in Zong Response");
                    else {}
//                        log.info("The Zong Response=" + code);
                }

                dot_handler = null;
                mydao = null;
            }
        }
    }
}



