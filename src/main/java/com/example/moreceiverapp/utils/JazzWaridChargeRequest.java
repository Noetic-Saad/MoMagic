package com.example.moreceiverapp.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.example.moreceiverapp.dtos.ChargeRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import okhttp3.*;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class JazzWaridChargeRequest {

    private String methodName ="UpdateBalanceAndDate";
    //private String transactionCurrency="PKR";
    //private String originNodeType="test";
    //private String originHostName="test";
    //private String transactionType="test";
    //private String transactionCode="test";
    //private String externalData1="test_VAS";
    //private String externalData2="test_VAS";

    private String transactionCurrency="PKR";
    private String originNodeType="EXT";
    private String originHostName="Gameinapp";
    private	String transactionType="Gameinapp";
    private String transactionCode="Gameinapp";
    private String externalData1="Gameinapp_VAS";
    private String externalData2="Gameinapp_VAS";

    private String subscriberNumber;
    private double adjustmentAmountRelative;
    private String originTransactionID;
    private String originTimeStamp;
    private static HttpClientConnection conn;
    private static PoolingHttpClientConnectionManager connectionManager;
    private static ConnectionKeepAliveStrategy keepAliveStrategy;
    CloseableHttpClient httpclient;

    private static final Logger log = LoggerFactory.getLogger(JazzWaridChargeRequest.class.getName());
    public static final String JazzWarid_URL = "http://10.13.32.156:10010/Air";
    public JazzWaridChargeRequest(String subscriberNumber, double adjustmentAmountRelative2, String originTransactionID){

        this.subscriberNumber = subscriberNumber;
        this.adjustmentAmountRelative = adjustmentAmountRelative2;
        this.originTransactionID = originTransactionID;
    }
    public JazzWaridChargeRequest(PoolingHttpClientConnectionManager httpClientConnectionManager,HttpClientConnection conn,ConnectionKeepAliveStrategy keepAliveStrategy){
        this.connectionManager = httpClientConnectionManager;
        this.conn = conn;
        this.keepAliveStrategy = keepAliveStrategy;
    }

    public JazzWaridChargeRequest() {
    }

    public Response sendChargingRequest(ChargeRequestDTO chargeRequestDTO) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String requestBody = gson.toJson(chargeRequestDTO, ChargeRequestDTO.class);
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("http://192.168.127.57:9070/charge")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public HttpResponse sendRequest(boolean requiresAuthorization) throws ClientProtocolException, IOException, ExecutionException, InterruptedException {
		/*//#################################################
		PostMethod postMethod = new PostMethod();
		//#################################################*/
        HttpResponse httpResponse=null;
        httpclient = HttpClients.custom()
                .setConnectionManager(this.connectionManager)
                .setKeepAliveStrategy(keepAliveStrategy)
                .setConnectionTimeToLive(5,TimeUnit.MINUTES)
                .build();
        HttpPost httpRequest = getHttpRequest(requiresAuthorization);
        try {
            httpResponse = httpclient.execute(httpRequest);
        }catch (IllegalStateException e){
            log.info("EXCEPTION "+e.getMessage());
            //Initializer.chargingRequest();
            sendRequest(false);
        }
        return httpResponse;
    }

    private HttpPost getHttpRequest(boolean requiresAuthorization) throws UnsupportedEncodingException {

        Date date = new Date(System.currentTimeMillis());

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        TimeZone PKT = TimeZone.getTimeZone("Asia/Karachi");
        dateFormat.setTimeZone(PKT);
        this.originTimeStamp = dateFormat.format(date);

        //String originDateTime = dateFormat.format(date)+"+0500";

        //log.info(originDateTime);

        HttpPost httpRequest=null;
        httpRequest = new HttpPost(JazzWarid_URL);

        //Authorization

        String Authorization = "Basic SU5UTk9FVElDOk1vYmkjOTEx";

        httpRequest.addHeader("Content-Type", "text/xml");
        httpRequest.addHeader("User-Agent", "UGw Server/4.3/1.0");
        httpRequest.addHeader("Authorization",Authorization);
        httpRequest.addHeader("Cache-Control", "no-cache");
        httpRequest.addHeader("Pragma", "no-cache");
        httpRequest.addHeader("Host", "10.13.32.156:10010");
        httpRequest.addHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        httpRequest.addHeader("Connection", "keep-alive");
        httpRequest.addHeader("Keep-Alive", "20");
        //httpRequest.addHeader("Content-Length", "1300");

        int adjustmentamount = (int)this.adjustmentAmountRelative;

        String inputXML = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
                "<methodCall>\n" +
                "<methodName>" + this.methodName + "</methodName>\n" +
                "<params>\n" +
                "<param>\n" +
                "<value>\n" +
                "<struct>\n" +
                "<member>\n" +
                "<name>originNodeType</name>\n" +
                "<value><string>" + this.originNodeType + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>originHostName</name>\n" +
                "<value><string>" + this.originHostName + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>originTransactionID</name>\n" +
                "<value><string>" + this.originTransactionID + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>transactionType</name>\n" +
                "<value><string>" + this.transactionType + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>transactionCode</name>\n" +
                "<value><string>" + this.transactionCode + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>externalData1</name>\n" +
                "<value><string>" + this.externalData1 + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>externalData2</name>\n" +
                "<value><string>" + this.externalData2 + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>originTimeStamp</name>\n" +
                "<value><dateTime.iso8601>" + this.originTimeStamp + "+0500</dateTime.iso8601></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>transactionCurrency</name>\n" +
                "<value><string>" + this.transactionCurrency + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>subscriberNumber</name>\n" +
                //"<value><string>3015166666</string></value>\n" +
                "<value><string>" + this.subscriberNumber + "</string></value>\n" +
                "</member>\n" +
                "<member>\n" +
                "<name>adjustmentAmountRelative</name>\n" +
                "<value><string>-" + adjustmentamount + "</string></value>\n" +
                "</member>\n" +
                "</struct>\n" +
                "</value>\n" +
                "</param>\n" +
                "</params>\n" +
                "</methodCall>";

        //"<value><string>3015166666</string></value>\n" +
        //"<value><string>" + this.subscriberNumber + "</string></value>\n" +

        //log.info(inputXML);

        httpRequest.setEntity(new StringEntity(inputXML));

        //log.info(httpRequest);

        return httpRequest;

    }
}
