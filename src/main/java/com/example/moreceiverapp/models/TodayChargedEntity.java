package com.example.moreceiverapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "today_tbl_billing", schema = "public", catalog = "ucip_db1")
public class TodayChargedEntity {

    //DB Attributes
//    @Id
//    @Column(name="id")
//    @SequenceGenerator(name = "tdy_tbl_charging_seq_gen", sequenceName = "tbl_charging_id_seq")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "today_tbl_billing_id_seq")
//    private Long id;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "today_tbl_billing_id_seq", sequenceName = "tbl_charging_id_seq", allocationSize = 1)
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

    public TodayChargedEntity(long original_sms_id, String subscriberNumber, String originTransactionID, int statuscode, String shortcode, String keyword, int ischarged, int partnerid, int responsecode, int attempt, double adjustmentAmountRelative2, int operatorid, String smstext,int chargingMechanism,int isPostPaid) {
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