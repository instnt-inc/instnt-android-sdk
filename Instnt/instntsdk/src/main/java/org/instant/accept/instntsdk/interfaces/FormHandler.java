package org.instant.accept.instntsdk.interfaces;

import android.app.Activity;

import java.util.Map;

public interface FormHandler {
    void setCallbackHandler(CallbackHandler callbackHandler);
    void setup(String formId);
    void submitForm(Map<String, Object> body);
}
