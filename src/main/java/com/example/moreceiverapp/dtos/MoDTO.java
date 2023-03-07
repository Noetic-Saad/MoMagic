package com.example.moreceiverapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoDTO {
    private String refNo;
    private String password;
    private String data;
    private Integer transId;
    private String connPointName;
    private String msisdn;
    private Integer shortcode;
    private Integer operatorId;
    private String username;
}