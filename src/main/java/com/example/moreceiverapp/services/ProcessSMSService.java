package com.example.moreceiverapp.services;

import com.example.moreceiverapp.dtos.JmsMessage;

public interface ProcessSMSService {
    JmsMessage processSMSRequest(JmsMessage incomingPacket);
}
