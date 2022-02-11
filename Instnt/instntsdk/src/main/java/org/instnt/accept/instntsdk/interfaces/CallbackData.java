package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.model.FormSubmitData;

public interface CallbackData {

    String SUCCESS_INIT_TRANSACTION_data();
    byte[] SUCCESS_IMAGE_UPLOAD_data();
    FormSubmitData SUCCESS_FORM_SUBMIT_data();
    void SUCCESS_SEND_OTP_data();
    void SUCCESS_VERIFY_OTP_data();
    void SUCCESS_DOC_SCAN_data();
}
