package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface OTPHandler {
    void sendOTP(String mobileNumber);
    void verifyOTP(String mobileNumber, String otpCode);

    void setInstnttxnid(String instnttxnid);
    void setCallbackHandler(CallbackHandler callbackHandler);
}
