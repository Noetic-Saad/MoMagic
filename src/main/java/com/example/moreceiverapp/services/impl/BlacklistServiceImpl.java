package com.example.moreceiverapp.services.impl;

import com.example.moreceiverapp.models.BlacklistEntity;
import com.example.moreceiverapp.repos.BlacklistRepository;
import com.example.moreceiverapp.services.BlacklistService;
import org.springframework.stereotype.Service;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    private final BlacklistRepository blacklistRepository;

    public BlacklistServiceImpl(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public BlacklistEntity checkMsisdn(String msisdn) {
        return blacklistRepository.selectByMsisdn(msisdn);
    }
}