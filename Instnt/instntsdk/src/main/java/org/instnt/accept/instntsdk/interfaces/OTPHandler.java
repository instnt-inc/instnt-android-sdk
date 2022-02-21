package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.InstntCallbackHandler;

public interface OTPHandler {
    void sendOTP(String mobileNumber, String instnttxnid);
    void verifyOTP(String mobileNumber, String otpCode, String instnttxnid);

    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
}
