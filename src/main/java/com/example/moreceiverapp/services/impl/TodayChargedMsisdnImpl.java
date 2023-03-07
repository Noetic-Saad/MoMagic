package com.example.moreceiverapp.services.impl;

import com.example.moreceiverapp.dtos.TodayChargedDTO;
import com.example.moreceiverapp.exceptions.ResourceNotFoundException;
import com.example.moreceiverapp.models.TodayChargedEntity;
import com.example.moreceiverapp.repos.TodayChargedMsisdnRepository;
import com.example.moreceiverapp.services.TodayChargedMsisdnService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TodayChargedMsisdnImpl implements TodayChargedMsisdnService {
    private final TodayChargedMsisdnRepository todayChargedMsisdnRepository;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(TodayChargedMsisdnImpl.class.getName());

    public TodayChargedMsisdnImpl(TodayChargedMsisdnRepository todayChargedMsisdnRepository, ModelMapper modelMapper) {
        this.todayChargedMsisdnRepository = todayChargedMsisdnRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TodayChargedEntity findMsisdnInTodayTblBilling(String subscriberNumber) {
        return todayChargedMsisdnRepository.findbyMsisdn(subscriberNumber);
    }
    @Override
    public Long insertRecord(TodayChargedEntity todayChargedEntity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        todayChargedEntity.setOriginTimeStamp(Timestamp.valueOf(dateFormat.format(date)));
     Long id = todayChargedMsisdnRepository.save(todayChargedEntity).getId();
        log.info("Record Inserted in today_tbl_billing for MSISDN | " +todayChargedEntity.getSubscriberNumber());
        return id;
    }
    @Override
    public void insertInTodayCharging(long original_sms_id, String subscriberNumber, String originTransactionID, int statuscode, String shortcode, String keyword, int ischarged, int partnerid, int responsecode, int attempt, double adjustmentAmountRelative2, int operatorid, String smstext, int chargingMechanism, int isPostPaid, Timestamp timestamp) {

    }
    @Override
    public void updateRecord(TodayChargedDTO todayChargedDTO, Long todaytableID) {
//        TodayChargedEntity entity = todayChargedMsisdnRepository.findById(todaytableID).orElseThrow(() ->
//                new ResourceNotFoundException("User", "Msisdn", todaytableID));
//        entity.setResponsecode(todayChargedDTO.getResponsecode());
//        entity.setAttempt(todayChargedDTO.getAttempt()+1);

        todayChargedMsisdnRepository.updateRecordById(todaytableID,todayChargedDTO.getResponsecode());
    }

    @Override
    public Integer findMsisdn(String subscriberNumber) {
        return todayChargedMsisdnRepository.findMsisdn(subscriberNumber);
    }

    private TodayChargedEntity dtoToTodayChargedEntity(TodayChargedDTO todayChargedDTO) {
        return modelMapper.map(todayChargedDTO, TodayChargedEntity.class);
    }

    public TodayChargedDTO todayChargedEntityToDTO(TodayChargedEntity todayChargedEntity) {
        return modelMapper.map(todayChargedEntity, TodayChargedDTO.class);
    }
}