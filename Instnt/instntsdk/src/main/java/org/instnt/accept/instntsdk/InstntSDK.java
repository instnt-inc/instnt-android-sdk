package org.instnt.accept.instntsdk;

import android.content.Context;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.implementations.InstntSDKImpl;

import java.util.Map;

public interface InstntSDK {

    /**
     * Initialize instntSDK
     * @param formKey
     * @param serverUrl
     * @param instntCallbackHandler
     * @return
     */
    static InstntSDK init(String formKey, String serverUrl, InstntCallbackHandler instntCallbackHandler) {
        InstntSDK instance = new InstntSDKImpl(formKey, serverUrl, instntCallbackHandler);
        instance.initTransaction(formKey, serverUrl, instntCallbackHandler);
        return instance;
    }

    /**
     * Initialize transaction
     */
    void initTransaction(String formKey, String serverUrl, InstntCallbackHandler instntCallbackHandler);

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
    void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey, String instnttxnid);

    /**
     * Upload attachment
     * @param isFront
     */
    void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie, String instnttxnid);

    /**
     * Verify documents
     * @param documentType
     */
    void verifyDocuments(String documentType, String instnttxnid);

    /**
     * Submit form
     * @param context
     * @param windowManager
     * @param body
     */
    void submitData(Context context, WindowManager windowManager, Map<String, Object> body, String instnttxnid);

    /**
     * Send OTP
     * @param mobileNumber
     */
    void sendOTP(String mobileNumber, String instnttxnid);

    /**
     * Verify OTP
     * @param mobileNumber
     * @param otpCode
     */
    void verifyOTP(String mobileNumber, String otpCode, String instnttxnid);
}
