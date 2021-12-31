package org.instant.accept.instntsdk.interfaces;

import android.content.Context;

public interface DocumentHandler {
    void setCallbackHandler(CallbackHandler callbackHandler);
    void uploadAttachment(boolean ifFront, String documentType);
    void verifyDocuments(String documentType);
    void setFormKey(String formKey);
    void setInstnttxnid(String instnttxnid);
    void setContext(Context context);
}
