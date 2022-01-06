package org.instant.accept.instntsdk.interfaces;

import android.app.Activity;

import java.util.Map;

public interface FormHandler {
    void submitForm(Map<String, Object> body);

    void setCallbackHandler(CallbackHandler callbackHandler);
    void setup(String formId);
    void setInstnttxnid(String instnttxnid);
}
