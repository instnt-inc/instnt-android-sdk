package org.instnt.accept.instntsdk.interfaces;

import android.content.Context;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.DocumentType;

public interface DocumentHandler {
    void scanDocument(boolean isFront, boolean isAutoUpload, DocumentType documentType, Context context, String documentVerifyLicenseKey, String instnttxnid);
    void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie, String instnttxnid);
    void verifyDocuments(DocumentType documentType, String instnttxnid);

    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
    void setFormKey(String formKey);
}
