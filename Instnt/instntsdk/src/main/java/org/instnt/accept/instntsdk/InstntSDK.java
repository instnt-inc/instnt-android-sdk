package org.instnt.accept.instntsdk;

import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DeviceHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.implementations.InstntSDKImpl;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;

public interface InstntSDK extends DocumentHandler, OTPHandler, FormHandler , DeviceHandler {

    InstntSDK instance = new InstntSDKImpl();

    static InstntSDK init(String formKey, String serverUrl, CallbackHandler callbackHandler) {
        instance.setServerURL(serverUrl);
        instance.setFormKey(formKey);
        instance.setCallbackHandler(callbackHandler);
        instance.initTransaction();
        return instance;
    }

    void initTransaction();

    String getTransactionID();

    boolean isOTPverificationEnable();

    boolean isDocumentVerificationEnable();

    void setServerURL(String serverURL);

    void setFormKey(String formKey);

    void setInstnttxnid(String instnttxnid);
}
