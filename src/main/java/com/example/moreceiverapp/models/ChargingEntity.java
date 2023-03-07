package com.example.moreceiverapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_charging", schema = "public", catalog = "ucip_db1")
public class ChargingEntity {

    //DB Attributes
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "original_sms_id")
    private long original_sms_id;
    @Column(name = "subscribernumber")
    private String subscriberNumber;
    @Column(name = "origintransactionid")
    private String originTransactionID;
    @Column(name = "origintimestamp")
    private Timestamp originTimeStamp;
    @Column(name = "statuscode")

    private int statuscode;
    @Column(name = "shortcode")
    private String shortcode;
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "ischarged")
    private int ischarged;
    @Column(name = "partnerid")
    private int partnerid;
    @Column(name = "responsecode")
    private int responsecode;
    @Column(name = "attempt")
    private int attempt;
    @Column(name = "adjustmentamountrelative")
    private double adjustmentAmountRelative;
    @Column(name = "operatorid")
    private int operatorid;
    @Column(name = "smstext")
    private String smstext;
    @Column(name = "charging_mechanism")
    private int chargingMechanism;
    @Column(name = "is_postpaid")
    private int isPostPaid;

    public ChargingEntity(long original_sms_id,
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
                          int isPostPaid) {
        this.original_sms_id = original_sms_id;
        this.subscriberNumber = subscriberNumber;
        this.originTransactionID = originTransactionID;
        this.statuscode = statuscode;
        this.shortcode = shortcode;
        this.keyword = keyword;
        this.ischarged = ischarged;
        this.partnerid = partnerid;
        this.responsecode = responsecode;
        this.attempt = attempt;
        this.adjustmentAmountRelative = adjustmentAmountRelative2;
        this.operatorid = operatorid;
        this.smstext = smstext;
        this.chargingMechanism=chargingMechanism;
        this.isPostPaid = isPostPaid;
    }
}