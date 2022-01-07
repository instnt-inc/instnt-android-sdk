package org.instant.accept.instntsdk;

import android.content.Context;

import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.implementations.InstntSDKImpl;
import org.instant.accept.instntsdk.interfaces.OTPHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;

public interface InstntSDK extends DocumentHandler, OTPHandler, FormHandler {

    void initTransaction();

    void setupWorkflowDetail();

    String getTransactionID();

    boolean isOTPverificationEnable();

    static InstntSDK instance = new InstntSDKImpl();

    void setServerURL(String serverURL);

    void setFormKey(String formKey);

    void setInstnttxnid(String instnttxnid);

    public static InstntSDK init(String formKey, String serverUrl, CallbackHandler callbackHandler) {
        instance.setServerURL(serverUrl);
        instance.setFormKey(formKey);
        instance.setCallbackHandler(callbackHandler);
        instance.initTransaction();
        instance.setupWorkflowDetail();
        return instance;
    }
}
