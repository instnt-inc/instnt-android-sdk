package org.instnt.accept.instntsdk.interfaces;

import com.idmetrics.dc.utils.DSOptions;

import org.instnt.accept.instntsdk.model.FormSubmitData;

public interface CallbackHandler {
	//SUCCESS
    void uploadAttachmentSuccessCallback(byte[] imageData);
    void scanDocumentSuccessCallback(byte[] imageData);
    void submitDataSuccessCallback(FormSubmitData formSubmitData);
    void initTransactionSuccessCallback(String instnttxnid);
    void sendOTPSuccessCallback(String message);
    void verifyOTPSuccessCallback(String message);

    //ERROR
    void scanDocumentCancelledErrorCallback(String message);
    void scanDocumentCaptureErrorCallback(String message);
    void submitDataErrorCallback(String message);
    void initTransactionErrorCallback(String message);
    void sendOTPErrorCallback(String message);
    void verifyOTPErrorCallback(String message);
}
