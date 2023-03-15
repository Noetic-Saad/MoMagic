package com.example.moreceiverapp.utils;

import okhttp3.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HttpRequestHandler {

    private String msisdn;
    private String shortcode;
    private String data;
    private String operatorId;
    private String transId;
    private String connectionPointId;
    private SecretKeySpec secretKey;
    private byte[] key = null;
    private String secret = "po90ki8u76gt";


    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class.getName());

    //private static HttpClient httpclient = null;
    private final String CHARSET = "utf-8";

    private HashMap<String, String> parameterMap = new HashMap<String, String>();
    private HashMap<String, String> postParametersMap = new HashMap<String, String>();

    public HttpRequestHandler(String msisdn, String shortcode, String data,
                              String operatorId, String transId) {

        this.msisdn = msisdn;
        this.shortcode = shortcode;
        this.data = data;
        this.operatorId = operatorId;
        this.transId = transId;
    }

    public HttpRequestHandler(String msisdn, String shortcode, String data,
                              String operatorId, String transId, String connPointId) {

        this.msisdn = msisdn;
        this.shortcode = shortcode;
        this.data = data;
        this.operatorId = operatorId;
        this.transId = transId;
        this.connectionPointId = connPointId;
    }

    public HttpRequestHandler() {
    }

    public Response sendRequest(boolean requiresAuthorization) throws ClientProtocolException, IOException {
        String actualmsisdn = this.msisdn;
        this.msisdn = encryptMsisdn(this.msisdn);
        log.info("Encrypted msisdn For | " + actualmsisdn + " | " + msisdn);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"transId\":\"" + this.transId + "\",\n\t\"shortcode\":\"" + this.shortcode + "\",\n\t\"data\": \"" + this.data + "\",\n\t\"operatorId\" : \"" + this.operatorId + "\",\n\t\"msisdn\" : \"" + this.msisdn + "\",\n\t\"connectionPointId\" : \"" + this.connectionPointId + "\"\n}");
        if (this.data.startsWith("MMBD25")) {
            log.info("postback-url | FOR | MMBD25 : " + Constants.PARTNER_MO_URL_FOR_MMBD25);
            Request request = new Request.Builder()
                    .url(Constants.PARTNER_MO_URL_FOR_MMBD25)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response;
        } else {
            log.info("postback-url: " + Constants.PARTNER_MO_URL);
            Request request = new Request.Builder()
                    .url(Constants.PARTNER_MO_URL)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response;
        }
    }

	/*public HttpResponse sendRequest(boolean requiresAuthorization) throws ClientProtocolException, IOException {
		/#################################################
		PostMethod postMethod = new PostMethod();
		//#################################################*//*
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpRequest = getHttpRequest(requiresAuthorization);
		// execute the post request
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		httpclient.getConnectionManager().shutdown();  //must close the connection resources
		return httpResponse;
	}*/

    private void addPostParameters() {
        this.parameterMap.put("transId", this.transId);
        this.parameterMap.put("shortcode", this.shortcode);
        this.parameterMap.put("data", this.data);
        this.parameterMap.put("operatorId", this.operatorId);
        this.parameterMap.put("msisdn", this.msisdn);
        this.parameterMap.put("connectionPointId", connectionPointId);
    }

    private HttpPost getHttpRequest(boolean requiresAuthorization) throws UnsupportedEncodingException {
        addPostParameters();
        String queryString = "";
        // This is the entry set retrieved from parameter map
        Set<Map.Entry<String, String>> getPairs = this.parameterMap.entrySet();

        // add the name-value pairs to the queryString
        for (Map.Entry<String, String> entry : getPairs) {
            queryString += URLEncoder.encode(entry.getKey(), this.CHARSET) + "=" + URLEncoder.encode(entry.getValue(), this.CHARSET) + "&";
        }

        // add ? at the start of the query string
        if (!queryString.isEmpty()) {
            queryString = "?" + queryString;
        }

        //log.info("QUERY GENERATED| "+queryString +"| FOR URL | "+ Constants.TIMWE_MO_URL);
        HttpPost httpRequest = new HttpPost(Constants.PARTNER_MO_URL + queryString);

        // log.debug("The queryString is " + queryString);

        // This represents the list of nameValuePairs that will be send through
        // the request
        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

        // This is the entry set retrieved from parameter map
        Set<Map.Entry<String, String>> postPairs = this.postParametersMap.entrySet();

        for (Map.Entry<String, String> entry : postPairs) {
            // Each name value pair being added to nameValuePairs ArrayList
            BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(),
                    entry.getValue());
            nameValuePairs.add(pair);

        }

        // Attach the nameValuePairs with the HttpPost request
        UrlEncodedFormEntity parameters = new UrlEncodedFormEntity(
                nameValuePairs, this.CHARSET);
        httpRequest.setEntity(parameters);

        return httpRequest;
    }

    public String encryptMsisdn(String msisdn) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(msisdn.getBytes("UTF-8")));
        } catch (Exception e) {
            //System.out.println("Error while encrypting: " + e.toString());
            return null;
        }
    }

    public void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}