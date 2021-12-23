package org.instant.accept.instntsdk.implementations;

import android.content.Context;
import android.view.View;

import org.instant.accept.instntsdk.interfaces.OTPHandler;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

public class OTPHandlerImpl implements OTPHandler {

    private NetworkUtil networkModule;

    public OTPHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public void sendOTP(String mobileNumber, Context context) {

        networkModule.sendOTP(mobileNumber).subscribe(otpResponse->{

            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                CommonUtils.showToast(context, otpResponse.getResponse().getErrors()[0]);
                return;
            }
        }, throwable -> {
            CommonUtils.showToast(context, CommonUtils.getErrorMessage(throwable));
        });
    }

    @Override
    public void verifyOTP(String mobileNumber, String otpCode, Context context) {

        networkModule.verifyOTP(mobileNumber, otpCode).subscribe(otpResponse->{

            if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                CommonUtils.showToast(context, otpResponse.getResponse().getErrors()[0]);
                return;
            }

            CommonUtils.showToast(context, "OTP verified successfully");

        }, throwable -> {
            CommonUtils.showToast(context, CommonUtils.getErrorMessage(throwable));
        });
    }
}
