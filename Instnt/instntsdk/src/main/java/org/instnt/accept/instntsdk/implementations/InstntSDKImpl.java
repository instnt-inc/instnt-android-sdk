package org.instnt.accept.instntsdk.implementations;

import android.content.Context;

import org.instnt.accept.instntsdk.InstntSDK;
import org.instnt.accept.instntsdk.enums.CallbackType;
import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntSDKImpl implements InstntSDK {

    private DocumentHandler documentHandler;
    private OTPHandler otpHandler;
    private FormHandler formHandler;
    private NetworkUtil networkModule;
    private String instnttxnid;
    private String formKey;
    private CallbackHandler callbackHandler;
    private FormCodes formCodes;

    public InstntSDKImpl() {
        networkModule = new NetworkUtil();
        documentHandler = new DocumentHandlerImpl(networkModule);
        otpHandler = new OTPHandlerImpl(networkModule);
        formHandler = new FormHandlerImpl(networkModule);
    }

    @Override
    public void setServerURL(String serverURL) {
        networkModule.setServerUrl(serverURL);
    }

    @Override
    public void setFormKey(String formKey) {
        this.formKey = formKey;
        this.documentHandler.setFormKey(formKey);
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
        this.documentHandler.setInstnttxnid(instnttxnid);
        this.otpHandler.setInstnttxnid(instnttxnid);
        this.formHandler.setInstnttxnid(instnttxnid);
    }

    @Override
    public void setWorkFlowDetail(FormCodes formCodes) {
        this.formCodes = formCodes;
        this.formHandler.setWorkFlowDetail(formCodes);
    }

    @Override
    public void initTransaction() {

        networkModule.getTransactionID(this.formKey).subscribe(response->{
            this.setInstnttxnid((String) response.get("instnttxnid"));
        }, throwable -> {
            this.callbackHandler.errorCallBack("Transaction initialization failed", CallbackType.ERROR_INIT_TRANSACTION);
        });
    }

    @Override
    public void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context) {
        documentHandler.scanDocument(isFront, isAutoUpload, documentType, context);
    }

    @Override
    public void uploadAttachment(boolean ifFront) {
        this.documentHandler.uploadAttachment(ifFront);
    }

    @Override
    public void verifyDocuments(String documentType) {
        this.documentHandler.verifyDocuments(documentType);
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
        this.documentHandler.setCallbackHandler(callbackHandler);
        this.formHandler.setCallbackHandler(callbackHandler);
        this.otpHandler.setCallbackHandler(callbackHandler);
    }

    @Override
    public void setupWorkflowDetail() {
        networkModule.getFormFields(this.formKey).subscribe(
            success -> {
                this.setWorkFlowDetail(success);
                this.formCodes = success;
                this.callbackHandler.successCallBack(null, "Workflow detail fetched successfully", CallbackType.SUCCESS_GET_WORKFLOW_DETAIL);
            }, throwable -> {
                this.callbackHandler.errorCallBack(CommonUtils.getErrorMessage(throwable), CallbackType.ERROR_GET_WORKFLOW_DETAIL);
            }
        );
    }

    @Override
    public void submitForm(Map<String, Object> body) {
        this.formHandler.submitForm(body);
    }

    @Override
    public void sendOTP(String mobileNumber) {
        otpHandler.sendOTP(mobileNumber);
    }

    @Override
    public void verifyOTP(String mobileNumber, String otpCode) {
        otpHandler.verifyOTP(mobileNumber, otpCode);
    }

    @Override
    public String getTransactionID() {
        return this.instnttxnid;
    }

    @Override
    public boolean isOTPverificationEnable() {
        return this.formCodes.isOtp_verification();
    }
}
