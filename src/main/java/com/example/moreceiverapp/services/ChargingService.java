package com.example.moreceiverapp.services;

import com.example.moreceiverapp.models.ChargingEntity;

public interface ChargingService {
    void insertRecord(ChargingEntity chargingEntity);
    ChargingEntity selectBysubscriberNumber(String subscriberNumber);
    Integer getDotTodaysChargedCount();
}