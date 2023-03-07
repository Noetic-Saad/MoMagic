package com.example.moreceiverapp.services.impl;

import com.example.moreceiverapp.models.ChargingEntity;
import com.example.moreceiverapp.repos.ChargingRepository;
import com.example.moreceiverapp.services.ChargingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ChargingServiceImpl implements ChargingService {
    private final ChargingRepository chargingRepository;
    private static final Logger log = LoggerFactory.getLogger(ChargingServiceImpl.class.getName());

    public ChargingServiceImpl(ChargingRepository chargingRepository) {
        this.chargingRepository = chargingRepository;
    }

    @Override
    public void insertRecord(ChargingEntity chargingEntity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        chargingEntity.setOriginTimeStamp(Timestamp.valueOf(dateFormat.format(date)));
        chargingRepository.save(chargingEntity);
        log.info("Record Inserted in tbl_charging for MSISDN | " +chargingEntity.getSubscriberNumber());
    }
    @Override
    public ChargingEntity selectBysubscriberNumber(String subscriberNumber) {
        return chargingRepository.selectBysubscriberNumber(subscriberNumber);
    }

    @Override
    public Integer getDotTodaysChargedCount() {
        return chargingRepository.getDotTodaysChargedCount();
    }
}