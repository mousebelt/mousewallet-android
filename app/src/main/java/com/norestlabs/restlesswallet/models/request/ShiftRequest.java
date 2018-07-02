package com.norestlabs.restlesswallet.models.request;

public class ShiftRequest {

    private String withdrawal;
    private String pair;
    private String returnAddress;
    private String apiPubKey;

    public ShiftRequest(String withdrawal, String pair, String returnAddress) {
        this.withdrawal = withdrawal;
        this.pair = pair;
        this.returnAddress = returnAddress;
        this.apiPubKey = "4a82a5f1610f1675fcbb54f8f3f64517687b6d8c2200411884ed601d8ef1874536cfbe5262543b1ae0c98e80ac16d4c94ff7c8ced0918101d56932b9b361b254";
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public void setApiPubKey(String apiPubKey) {
        this.apiPubKey = apiPubKey;
    }
}
