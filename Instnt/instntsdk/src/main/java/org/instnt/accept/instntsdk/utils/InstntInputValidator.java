package org.instnt.accept.instntsdk.utils;

import android.content.Context;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.DocumentType;
import org.instnt.accept.instntsdk.exceptions.InstntSDKValidationException;
import org.instnt.accept.instntsdk.model.FormCodes;

import java.util.Map;

public class InstntInputValidator {

    public static void validateFormKey(String formKey) {
        if(formKey == null || formKey.isEmpty()) throw new InstntSDKValidationException("Please pass valid form key");
    }

    public static void validateServerUrl(String serverUrl) {
        if(serverUrl == null || serverUrl.isEmpty()) throw new InstntSDKValidationException("Please pass valid server URL");
    }

    public static void validateInstntCallbackHandler(InstntCallbackHandler instntCallbackHandler) {
        if(instntCallbackHandler == null) throw new InstntSDKValidationException("Please implement InstntCallbackHandler and pass this object to instntCallbackHandler");
    }

    public static void validateDocumentType(String documentType) {
        if(documentType == null || documentType.isEmpty()) throw new InstntSDKValidationException("Please pass valid documentType");
        try {
            DocumentType.valueOf(documentType);
        } catch (Exception e) {
            throw new InstntSDKValidationException("Please pass valid documentType. Possible values are [" + DocumentType.getAllNames() + "]");
        }
    }

    public static void validateContext(Context context) {
        if(context == null) throw new InstntSDKValidationException("Please pass valid context");
    }

    public static void validateDocumentVerifyLicenseKey(String documentVerifyLicenseKey) {
        if(documentVerifyLicenseKey == null || documentVerifyLicenseKey.isEmpty()) throw new InstntSDKValidationException("Please pass valid documentVerifyLicenseKey");
    }

    public static void validateInstnttxnid(String instnttxnid) {
        if(instnttxnid == null || instnttxnid.isEmpty()) throw new InstntSDKValidationException("Please pass valid instnttxnid");
    }

    public static void validateImageData(byte[] imageData) {
        if(imageData == null || imageData.length < 1) throw new InstntSDKValidationException("Please pass valid image data");
    }

    public static void validateWindowManager(WindowManager windowManager) {
        if(windowManager == null) throw new InstntSDKValidationException("Please pass valid windowManager");
    }

    public static void validateBody(Map<String, Object> body) {
        if(body == null) throw new InstntSDKValidationException("Please pass valid body");
    }

    public static void validateMobileNumber(String mobileNumber) {
        if(mobileNumber == null || mobileNumber.isEmpty()) throw new InstntSDKValidationException("Please pass valid mobileNumber");
    }

    public static void validateOtpCode(String otpCode) {
        if(otpCode == null || otpCode.isEmpty()) throw new InstntSDKValidationException("Please pass valid mobileNumber");
    }

    public static void validateFormCodes(FormCodes formCodes) {
        if(formCodes == null || formCodes.getInstnttxnid() == null || formCodes.getInstnttxnid().isEmpty()) throw new InstntSDKValidationException("Please call InstntSDK.init() or initTransaction() method to properly initialize the SDK");
    }
}
