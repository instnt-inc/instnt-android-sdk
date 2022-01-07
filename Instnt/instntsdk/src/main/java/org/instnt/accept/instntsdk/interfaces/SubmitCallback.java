package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.model.FormSubmitData;

public interface SubmitCallback {
    void didCancel();
    void didSubmit(FormSubmitData submitData, String errMessage);
}
