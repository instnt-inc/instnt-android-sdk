package org.instant.accept.instntsdk.interfaces;

import android.app.Activity;

import org.instant.accept.instntsdk.model.FormCodes;

import java.util.Map;

public interface FormHandler {
    void submitForm(Map<String, Object> body);

    void setCallbackHandler(CallbackHandler callbackHandler);
    void setInstnttxnid(String instnttxnid);
    void setWorkFlowDetail(FormCodes formCodes);
}
