package org.instnt.accept.instntsdk;

import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.implementations.InstntSDKImpl;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;

public interface InstntSDK extends DocumentHandler, OTPHandler, FormHandler {

    void initTransaction();

    void setupWorkflowDetail();

    String getTransactionID();

    boolean isOTPverificationEnable();

    boolean isDocumentVerificationEnable();

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
