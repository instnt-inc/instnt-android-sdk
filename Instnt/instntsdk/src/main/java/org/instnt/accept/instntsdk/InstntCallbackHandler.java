package org.instnt.accept.instntsdk;

import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.model.FormSubmitData;

public interface InstntCallbackHandler {
	//SUCCESS
    void uploadAttachmentSuccessCallback(byte[] imageData);
    void scanDocumentSuccessCallback(byte[] imageData);
    void submitDataSuccessCallback(FormSubmitData formSubmitData);
    void initTransactionSuccessCallback(String instnttxnid);
    void sendOTPSuccessCallback(String message);
    void verifyOTPSuccessCallback(String message);

    //ERROR
    void instntErrorCallback(String message, ErrorCallbackType errorCallbackType);
}
