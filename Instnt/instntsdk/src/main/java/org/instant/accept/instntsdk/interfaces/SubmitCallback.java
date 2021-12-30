package org.instant.accept.instntsdk.interfaces;

import org.instant.accept.instntsdk.model.FormSubmitData;

public interface SubmitCallback {
    void didCancel();
    void didSubmit(FormSubmitData submitData, String errMessage);
}
