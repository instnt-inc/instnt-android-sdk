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

    /**
     * Initialize instntSDK
     * @param formKey
     * @param serverUrl
     * @param callbackHandler
     * @return
     */
    static InstntSDK init(String formKey, String serverUrl, CallbackHandler callbackHandler) {
        instance.initTransaction(formKey, serverUrl, callbackHandler);
        return instance;
    }

    /**
     * Initialize transaction
     * @param formKey
     * @param serverUrl
     * @param callbackHandler
     */
    void initTransaction(String formKey, String serverUrl, CallbackHandler callbackHandler);

    /**
     * Get instnt transaction id
     * @return
     */
    String getInstnttxnid();

    /**
     * Check is otp verification enabled
     * @return
     */
    boolean isOTPverificationEnabled();

    /**
     * Check is document verification enabled
     * @return
     */
    boolean isDocumentVerificationEnabled();

    /**
     * Scan document
     * @param isFront
     * @param isAutoUpload
     * @param documentType
     * @param context
     * @param documentVerifyLicenseKey
     */
    void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey);

    /**
     * Upload attachment
     * @param isFront
     */
    void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie);

    /**
     * Verify documents
     * @param documentType
     */
    void verifyDocuments(String documentType);

    /**
     * Submit form
     * @param context
     * @param windowManager
     * @param body
     */
    void submitData(Context context, WindowManager windowManager, Map<String, Object> body);

    /**
     * Send OTP
     * @param mobileNumber
     */
    void sendOTP(String mobileNumber);

    /**
     * Verify OTP
     * @param mobileNumber
     * @param otpCode
     */
    void verifyOTP(String mobileNumber, String otpCode);
}
