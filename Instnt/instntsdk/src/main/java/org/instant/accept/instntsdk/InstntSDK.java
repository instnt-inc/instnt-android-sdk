package org.instant.accept.instntsdk;

import android.content.Context;

import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.implementations.InstntSDKImpl;
import org.instant.accept.instntsdk.interfaces.OTPHandler;

public interface InstntSDK extends DocumentHandler, OTPHandler, FormHandler {
    String getTransactionID();

    static InstntSDK instance = new InstntSDKImpl();

    void setServerURL(String serverURL);

    void setFormKey(String formKey);

    void setContext(Context context);

    public static InstntSDK setup(String formKey, String serverUrl, Context context) {
//        if (instance == null) {
//            instance = new InstntSDKImpl(serverUrl);
//        }
        instance.setServerURL(serverUrl);
        instance.setFormKey(formKey);
        instance.setContext(context);

        return instance;
    }
}
