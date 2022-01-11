package org.instnt.accept.instntsdk.interfaces;

import android.content.Context;

public interface DocumentHandler {
    void scanDocument(boolean ifFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey);
    void uploadAttachment(boolean ifFront);
    void verifyDocuments(String documentType);

    void setCallbackHandler(CallbackHandler callbackHandler);
    void setFormKey(String formKey);
    void setInstnttxnid(String instnttxnid);
}
