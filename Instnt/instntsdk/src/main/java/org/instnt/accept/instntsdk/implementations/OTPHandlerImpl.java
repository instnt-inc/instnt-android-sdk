package org.instnt.accept.instntsdk.implementations;

import android.util.Log;

import org.instnt.accept.instntsdk.enums.CallbackType;
import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

public class OTPHandlerImpl implements OTPHandler {

    private static final String TAG = "OTPHandlerImpl";
    private NetworkUtil networkModule;
    private String instnttxnid;
    private CallbackHandler callbackHandler;

    public OTPHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public void sendOTP(String mobileNumber) {

        Log.i(TAG, "Calling Send OTP");
        networkModule.sendOTP(mobileNumber, this.instnttxnid).subscribe(otpResponse->{
            Log.i(TAG, "Send OTP called successfully");
            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                Log.e(TAG, "Send OTP called successfully but returns with error : " + otpResponse.getResponse().getErrors()[0]);
                this.callbackHandler.errorCallBack(otpResponse.getResponse().getErrors()[0], CallbackType.ERROR_SEND_OTP);
                return;
            }
            this.callbackHandler.successCallBack(null, "OTP sent successfully", CallbackType.SUCCESS_SEND_OTP);
        }, throwable -> {
            Log.e(TAG, "Send OTP returns with error", throwable);
            this.callbackHandler.errorCallBack("Failed to send OTP", CallbackType.ERROR_SEND_OTP);
        });
    }

    @Override
    public void verifyOTP(String mobileNumber, String otpCode) {

        Log.i(TAG, "Calling verify OTP");
        networkModule.verifyOTP(mobileNumber, otpCode, this.instnttxnid).subscribe(otpResponse->{
            Log.i(TAG, "Verify OTP called successfully");
            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                Log.e(TAG, "Verify OTP called successfully but returns with error : " + otpResponse.getResponse().getErrors()[0]);
                this.callbackHandler.errorCallBack(otpResponse.getResponse().getErrors()[0], CallbackType.ERROR_VERIFY_OTP);
                return;
            }
            this.callbackHandler.successCallBack(null, "OTP verified successfully", CallbackType.SUCCESS_VERIFY_OTP);
        }, throwable -> {
            Log.e(TAG, "Verify OTP returns with error", throwable);
            this.callbackHandler.errorCallBack(CommonUtils.getErrorMessage(throwable), CallbackType.ERROR_VERIFY_OTP);
        });
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        Log.i(TAG, "Set instnttxnid");
        this.instnttxnid = instnttxnid;
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        Log.i(TAG, "Set callbackHandler");
        this.callbackHandler = callbackHandler;
    }
}
