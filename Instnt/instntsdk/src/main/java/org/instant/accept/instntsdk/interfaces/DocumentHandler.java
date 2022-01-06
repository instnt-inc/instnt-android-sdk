package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface DocumentHandler {
    void scanDocument(boolean ifFront, boolean isAutoUpload, String documentType);
    void uploadAttachment(boolean ifFront);
    void verifyDocuments(String documentType);

    void setCallbackHandler(CallbackHandler callbackHandler);
    void setFormKey(String formKey);
    void setInstnttxnid(String instnttxnid);
    void setContext(Context context);
}
