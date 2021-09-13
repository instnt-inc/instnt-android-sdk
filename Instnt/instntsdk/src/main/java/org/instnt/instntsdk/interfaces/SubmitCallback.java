package org.instnt.instntsdk.interfaces;

import org.instnt.instntsdk.data.FormSubmitData;

public interface SubmitCallback {
    void didCancel();
    void didSubmit(FormSubmitData submitData, String errMessage);
}
