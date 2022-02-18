package org.instnt.accept.instntsdk;

import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.model.FormSubmitData;
import org.instnt.accept.instntsdk.model.InstntImageData;

public interface InstntCallbackHandler {
	//SUCCESS
    void uploadAttachmentSuccessCallback(InstntImageData imageData);
    void scanDocumentSuccessCallback(InstntImageData imageData);
    void submitDataSuccessCallback(FormSubmitData formSubmitData);
    void initTransactionSuccessCallback(String instnttxnid);
    void sendOTPSuccessCallback(String message);
    void verifyOTPSuccessCallback(String message);
    void verifyDocumentsInitiationCallback(String message);

    //ERROR
    void instntErrorCallback(String message, ErrorCallbackType errorCallbackType);
}
