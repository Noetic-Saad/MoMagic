package com.example.moreceiverapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JmsMessage {
    private String msisdn;
    private String shortcode;
    private String smsText;
    private Integer connectivityPointId;
    private Integer smsId;
    private String connectionPointName;
    private Boolean isAccepted = true;
}