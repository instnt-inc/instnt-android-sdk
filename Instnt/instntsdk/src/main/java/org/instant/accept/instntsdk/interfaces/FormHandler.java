package org.instant.accept.instntsdk.interfaces;

import android.app.Activity;

import java.util.Map;

public interface FormHandler {

    void setCallback(SubmitCallback callback);
    SubmitCallback getSubmitCallback();
    void setup(String formId);
    void submitData(Map<String, Object> body, SubmitCallback callback);
    void showForm(Activity activity, SubmitCallback callback);
}
