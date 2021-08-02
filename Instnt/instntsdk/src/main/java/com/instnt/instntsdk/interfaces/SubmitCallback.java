package com.instnt.instntsdk.interfaces;

import com.instnt.instntsdk.data.FormSubmitData;

public interface SubmitCallback {
    void didCancel();
    void didSubmit(FormSubmitData submitData, String errMessage);
}
