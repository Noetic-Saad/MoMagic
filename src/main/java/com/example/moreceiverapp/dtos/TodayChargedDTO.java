package com.example.moreceiverapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayChargedDTO {
    private long original_sms_id;
    private String subscriberNumber;
    private String originTransactionID;
    private Timestamp originTimeStamp;
    private int statuscode;
    private String shortcode;
    private String keyword;
    private int ischarged;
    private int partnerid;
    private int responsecode;
    private int attempt;
    private double adjustmentAmountRelative;
    private int operatorid;
    private String smstext;
    private int chargingMechanism;
    private int isPostPaid;
}