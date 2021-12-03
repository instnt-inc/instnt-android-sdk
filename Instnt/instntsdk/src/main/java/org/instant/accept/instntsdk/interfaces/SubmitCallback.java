package org.instant.accept.instntsdk.interfaces;

import org.instant.accept.instntsdk.data.FormSubmitData;

public interface SubmitCallback {
    void didCancel();
    void didSubmit(FormSubmitData submitData, String errMessage);
}
