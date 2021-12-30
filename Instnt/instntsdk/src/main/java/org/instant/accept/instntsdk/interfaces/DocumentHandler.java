package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface DocumentHandler {
    void setCallbackHandler(CallbackHandler callbackHandler);
    void uploadAttachment(Context context, String instnttxnid);
    void verifyDocuments(String documentType);
    void setFormKey(String formKey);
    void setInstnttxnid(String instnttxnid);
}
