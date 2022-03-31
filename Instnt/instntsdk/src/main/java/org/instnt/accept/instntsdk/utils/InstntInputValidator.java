package org.instnt.accept.instntsdk.utils;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.DocumentType;
import org.instnt.accept.instntsdk.exceptions.InstntSDKValidationException;
import org.instnt.accept.instntsdk.model.FormCodes;

import java.util.Map;

public class InstntInputValidator {

    public static void validateFormKey(String formKey) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateFormKey");
        if(formKey == null || formKey.isEmpty()) throw new InstntSDKValidationException("Please pass valid form key");
    }

    public static void validateServerUrl(String serverUrl) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateServerUrl");
        if(serverUrl == null || serverUrl.isEmpty()) throw new InstntSDKValidationException("Please pass valid server URL");
    }

    public static void validateInstntCallbackHandler(InstntCallbackHandler instntCallbackHandler) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateInstntCallbackHandler");
        if(instntCallbackHandler == null) throw new InstntSDKValidationException("Please implement InstntCallbackHandler and pass this object to instntCallbackHandler");
    }

    public static void validateDocumentType(String documentType) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateDocumentType");
        if(documentType == null || documentType.isEmpty()) throw new InstntSDKValidationException("Please pass valid documentType");
        try {
            DocumentType.valueOf(documentType);
        } catch (Exception e) {
            throw new InstntSDKValidationException("Please pass valid documentType. Possible values are [" + DocumentType.getAllNames() + "]");
        }
    }

    public static void validateContext(Context context) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateContext");
        if(context == null) throw new InstntSDKValidationException("Please pass valid context");
    }

    public static void validateDocumentVerifyLicenseKey(String documentVerifyLicenseKey) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateDocumentVerifyLicenseKey");
        if(documentVerifyLicenseKey == null || documentVerifyLicenseKey.isEmpty()) throw new InstntSDKValidationException("Please pass valid documentVerifyLicenseKey");
    }

    public static void validateInstnttxnid(String instnttxnid) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateInstnttxnid");
        if(instnttxnid == null || instnttxnid.isEmpty()) throw new InstntSDKValidationException("Please pass valid instnttxnid");
    }

    public static void validateImageData(byte[] imageData) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateImageData");
        if(imageData == null || imageData.length < 1) throw new InstntSDKValidationException("Please pass valid image data");
    }

    public static void validateWindowManager(WindowManager windowManager) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateWindowManager");
        if(windowManager == null) throw new InstntSDKValidationException("Please pass valid windowManager");
    }

    public static void validateBody(Map<String, Object> body) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateBody");
        if(body == null) throw new InstntSDKValidationException("Please pass valid body");
    }

    public static void validateMobileNumber(String mobileNumber) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateMobileNumber");
        if(mobileNumber == null || mobileNumber.isEmpty()) throw new InstntSDKValidationException("Please pass valid mobileNumber");
    }

    public static void validateOtpCode(String otpCode) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateOtpCode");
        if(otpCode == null || otpCode.isEmpty()) throw new InstntSDKValidationException("Please pass valid mobileNumber");
    }

    public static void validateFormCodes(FormCodes formCodes) {
        Log.i(CommonUtils.LOG_TAG, "Calling validateFormCodes");
        if(formCodes == null || formCodes.getInstnttxnid() == null || formCodes.getInstnttxnid().isEmpty()) throw new InstntSDKValidationException("Please call InstntSDK.init() or initTransaction() method to properly initialize the SDK");
    }
}
