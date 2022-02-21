package org.instnt.accept.instntsdk.implementations;

import android.util.Log;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

public class OTPHandlerImpl implements OTPHandler {

    private NetworkUtil networkModule;
    private InstntCallbackHandler instntCallbackHandler;

    public OTPHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    /**
     * Send OTP
     * @param mobileNumber
     */
    @Override
    public void sendOTP(String mobileNumber, String instnttxnid) {

        Log.i(CommonUtils.LOG_TAG, "Calling Send OTP");
        networkModule.sendOTP(mobileNumber, instnttxnid).subscribe(otpResponse->{
            Log.i(CommonUtils.LOG_TAG, "Send OTP called successfully");        
            if(otpResponse != null && otpResponse.getResponse().getErrors() != null && otpResponse.getResponse().getErrors().length > 0) {
                Log.e(CommonUtils.LOG_TAG, "Send OTP called successfully but returns with error : " + otpResponse.getResponse().getErrors()[0]);
                this.instntCallbackHandler.instntErrorCallback(otpResponse.getResponse().getErrors()[0], ErrorCallbackType.SEND_OTP_ERROR);
                return;
            }
            this.instntCallbackHandler.sendOTPSuccessCallback("OTP sent successfully");
        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Send OTP returns with error", throwable);
            this.instntCallbackHandler.instntErrorCallback("Failed to send OTP", ErrorCallbackType.SEND_OTP_ERROR);
        });
    }

    /**
     * Verify OTP
     * @param mobileNumber
     * @param otpCode
     */
    @Override
    public void verifyOTP(String mobileNumber, String otpCode, String instnttxnid) {

        Log.i(CommonUtils.LOG_TAG, "Calling verify OTP");
        networkModule.verifyOTP(mobileNumber, otpCode, instnttxnid).subscribe(otpResponse->{
            Log.i(CommonUtils.LOG_TAG, "Verify OTP called successfully");
            if(otpResponse != null && otpResponse.getResponse().getErrors() != null && otpResponse.getResponse().getErrors().length > 0) {
                Log.e(CommonUtils.LOG_TAG, "Verify OTP called successfully but returns with error : " + otpResponse.getResponse().getErrors()[0]);
                this.instntCallbackHandler.instntErrorCallback(otpResponse.getResponse().getErrors()[0], ErrorCallbackType.VERIFY_OTP_ERROR);
                return;
            }
            this.instntCallbackHandler.verifyOTPSuccessCallback("OTP verified successfully");
        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Verify OTP returns with error", throwable);
            this.instntCallbackHandler.instntErrorCallback(CommonUtils.getErrorMessage(throwable), ErrorCallbackType.VERIFY_OTP_ERROR);
        });
    }

    /**
     * Set call back handler
     * @param instntCallbackHandler
     */
    @Override
    public void setCallbackHandler(InstntCallbackHandler instntCallbackHandler) {
        Log.i(CommonUtils.LOG_TAG, "Set callbackHandler");
        this.instntCallbackHandler = instntCallbackHandler;
    }
}
