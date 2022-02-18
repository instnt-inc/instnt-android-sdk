package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.InstntCallbackHandler;

public interface OTPHandler {
    void sendOTP(String mobileNumber);
    void verifyOTP(String mobileNumber, String otpCode);

    void setInstnttxnid(String instnttxnid);
    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
}
