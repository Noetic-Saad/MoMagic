package com.example.moreceiverapp.services;

import com.example.moreceiverapp.models.BlacklistEntity;

public interface BlacklistService {
    BlacklistEntity checkMsisdn(String msisdn);
}
