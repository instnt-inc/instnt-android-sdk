package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface OTPHandler {
    void sendOTP(String mobileNumber, Context context);
    void verifyOTP(String mobileNumber, String otpCode, Context context);
}
