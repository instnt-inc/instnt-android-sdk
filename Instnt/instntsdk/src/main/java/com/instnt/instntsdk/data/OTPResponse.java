package com.instnt.instntsdk.data;

import java.io.Serializable;

public class OTPResponse implements Serializable {

    private OTPVerificationResult response;

    public OTPVerificationResult getResponse() {
        return response;
    }

    public void setResponse(OTPVerificationResult response) {
        this.response = response;
    }
}
