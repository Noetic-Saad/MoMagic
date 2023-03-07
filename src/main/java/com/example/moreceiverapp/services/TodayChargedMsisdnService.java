package com.example.moreceiverapp.services;

import com.example.moreceiverapp.dtos.TodayChargedDTO;
import com.example.moreceiverapp.models.TodayChargedEntity;

import java.sql.Timestamp;

public interface TodayChargedMsisdnService {
    TodayChargedEntity findMsisdnInTodayTblBilling(String msisdn);
    Long insertRecord(TodayChargedEntity todayChargedEntity);
    void insertInTodayCharging(long original_sms_id,
                               String subscriberNumber,
                               String originTransactionID,
                               int statuscode,
                               String shortcode,
                               String keyword,
                               int ischarged,
                               int partnerid,
                               int responsecode,
                               int attempt,
                               double adjustmentAmountRelative2,
                               int operatorid,
                               String smstext,
                               int chargingMechanism,
                               int isPostPaid,
                               Timestamp timestamp);
    void updateRecord(TodayChargedDTO todayChargedDTO, Long id);
    Integer findMsisdn(String subscriberNumber);
}
