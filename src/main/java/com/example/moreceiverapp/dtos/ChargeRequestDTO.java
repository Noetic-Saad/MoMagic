package com.example.moreceiverapp.dtos;

public class ChargeRequestDTO {

    private String msisdn;
    private String amount;
    private int operatorId;
    private String transactionId;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "ChargeRequestDTO{" +
                "msisdn='" + msisdn + '\'' +
                ", amount='" + amount + '\'' +
                ", operatorId=" + operatorId +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
