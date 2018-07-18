package com.norestlabs.restlesswallet.models.response;

public class ShiftResponse {

    private String orderId;
    private String deposit;
    private String depositType;
    private String withdrawal;
    private String withdrawalType;
//    private String public;
    private String apiPubKey;
    private String returnAddress;
    private String returnAddressType;

    public String getOrderId() {
        return orderId;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getDepositType() {
        return depositType;
    }

    public String getWithdrawal() {
        return withdrawal;
    }

    public String getWithdrawalType() {
        return withdrawalType;
    }

    public String getApiPubKey() {
        return apiPubKey;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public String getReturnAddressType() {
        return returnAddressType;
    }
}
