package org.instnt.accept.instntsdk.interfaces;

import android.content.Context;

import org.instnt.accept.instntsdk.InstntCallbackHandler;

public interface DocumentHandler {
    void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey, String instnttxnid);
    void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie, String instnttxnid);
    void verifyDocuments(String documentType, String instnttxnid);

    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
    void setFormKey(String formKey);
}
