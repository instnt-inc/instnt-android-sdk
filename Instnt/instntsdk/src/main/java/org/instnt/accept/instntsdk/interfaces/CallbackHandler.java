package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.model.FormSubmitData;

public interface CallbackHandler {
	//SUCCESS
    void uploadAttachmentSuccessCallBack(byte[] imageData);
    void scanDocumentSuccessCallBack(String message);
    void submitDataSuccessCallBack(FormSubmitData formSubmitData);
    void getTransactionIDSuccessCallBack(String instnttxnid);
    void sendOTPSuccessCallBack(String message);
    void verifyOTPSuccessCallBack(String message);

    //ERROR
    void scanDocumentCancelledErrorCallBack(String message);
    void scanDocumentCaptureErrorCallBack(String message);
    void submitDataErrorCallBack(String message);
    void getTransactionIDErrorCallBack(String message);
    void sendOTPErrorCallBack(String message);
    void verifyOTPErrorCallBack(String message);
}
