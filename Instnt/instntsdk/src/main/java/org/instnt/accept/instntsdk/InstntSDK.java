package org.instnt.accept.instntsdk;

import android.content.Context;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DeviceHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.implementations.InstntSDKImpl;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;

import java.util.Map;

public interface InstntSDK {

    InstntSDK instance = new InstntSDKImpl();

    static InstntSDK init(String formKey, String serverUrl, CallbackHandler callbackHandler) {
        instance.initTransaction(formKey, serverUrl, callbackHandler);
        return instance;
    }

    void initTransaction(String formKey, String serverUrl, CallbackHandler callbackHandler);
    String getInstnttxnid();
    boolean isOTPverificationEnabled();
    boolean isDocumentVerificationEnabled();

    void scanDocument(boolean ifFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey);
    void uploadAttachment(boolean ifFront);
    void verifyDocuments(String documentType);

    void submitData(Context context, WindowManager windowManager, Map<String, Object> body);

    void sendOTP(String mobileNumber);
    void verifyOTP(String mobileNumber, String otpCode);
}
