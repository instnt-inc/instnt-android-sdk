package org.instnt.accept.instntsdk.implementations;

import org.instnt.accept.instntsdk.interfaces.CallbackData;
import org.instnt.accept.instntsdk.model.FormSubmitData;

public class CallbackDataImpl implements CallbackData {

    private FormSubmitData formSubmitData;

    private byte[] imageBytes;

    private String instnttxnid;

    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setFormSubmitData(FormSubmitData formSubmitData) {
        this.formSubmitData = formSubmitData;
    }

    @Override
    public String SUCCESS_INIT_TRANSACTION_data() {
        return this.instnttxnid;
    }

    @Override
    public byte[] SUCCESS_IMAGE_UPLOAD_data() {
        return this.imageBytes;
    }

    @Override
    public FormSubmitData SUCCESS_FORM_SUBMIT_data() {
        return this.formSubmitData;
    }

    @Override
    public void SUCCESS_SEND_OTP_data() {

    }

    @Override
    public void SUCCESS_VERIFY_OTP_data() {

    }

    @Override
    public void SUCCESS_DOC_SCAN_data() {

    }
}
