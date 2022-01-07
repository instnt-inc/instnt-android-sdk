package org.instnt.accept.instntsdk.implementations;

import org.instnt.accept.instntsdk.enums.CallbackType;
import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

public class OTPHandlerImpl implements OTPHandler {

    private NetworkUtil networkModule;
    private String instnttxnid;
    private CallbackHandler callbackHandler;

    public OTPHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public void sendOTP(String mobileNumber) {

        networkModule.sendOTP(mobileNumber, this.instnttxnid).subscribe(otpResponse->{
            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                this.callbackHandler.errorCallBack(otpResponse.getResponse().getErrors()[0], CallbackType.ERROR_SEND_OTP);
                return;
            }
            this.callbackHandler.successCallBack(null, "OTP sent successfully", CallbackType.SUCCESS_SEND_OTP);
        }, throwable -> {
            this.callbackHandler.errorCallBack("Failed to send OTP", CallbackType.ERROR_SEND_OTP);
        });
    }

    @Override
    public void verifyOTP(String mobileNumber, String otpCode) {

        networkModule.verifyOTP(mobileNumber, otpCode, this.instnttxnid).subscribe(otpResponse->{
            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                this.callbackHandler.errorCallBack(otpResponse.getResponse().getErrors()[0], CallbackType.ERROR_VERIFY_OTP);
                return;
            }
            this.callbackHandler.successCallBack(null, "OTP verified successfully", CallbackType.SUCCESS_VERIFY_OTP);
        }, throwable -> {
            this.callbackHandler.errorCallBack(CommonUtils.getErrorMessage(throwable), CallbackType.ERROR_VERIFY_OTP);
        });
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }
}
