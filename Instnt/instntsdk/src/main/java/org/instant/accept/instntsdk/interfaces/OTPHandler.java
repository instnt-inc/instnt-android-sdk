package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface OTPHandler {
    void sendOTP(String mobileNumber);
    void verifyOTP(String mobileNumber, String otpCode);
    void setContext(Context context);
}
