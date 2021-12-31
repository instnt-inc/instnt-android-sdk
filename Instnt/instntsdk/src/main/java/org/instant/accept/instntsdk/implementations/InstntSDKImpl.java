package org.instant.accept.instntsdk.implementations;

import android.app.Activity;
import android.content.Context;

import org.instant.accept.instntsdk.InstntSDK;
import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.interfaces.OTPHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntSDKImpl implements InstntSDK {

    private DocumentHandler documentHandler;
    private OTPHandler otpHandler;
    private FormHandler formHandler;
    private NetworkUtil networkModule;
    private String instnttxnid;
    private String formKey;
    private Context context;

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
    public void setContext(Context context) {
        this.context = context;
        this.documentHandler.setContext(context);
        this.otpHandler.setContext(context);
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
        this.documentHandler.setInstnttxnid(instnttxnid);
    }

    @Override
    public void initTransaction() {

        networkModule.getTransactionID(this.formKey).subscribe(response->{

            System.out.println("Response");
            this.setInstnttxnid((String) response.get("instnttxnid"));
        }, throwable -> {
            CommonUtils.showToast(this.context, CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

    @Override
    public void uploadAttachment(boolean isFront, String documentType) {
        documentHandler.uploadAttachment(isFront, documentType);
    }

    @Override
    public void verifyDocuments(String documentType) {
        this.documentHandler.verifyDocuments(documentType);
    }

    @Override
    public void setCallback(SubmitCallback callback) {
        formHandler.setCallback(callback);
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        documentHandler.setCallbackHandler(callbackHandler);
    }

    @Override
    public SubmitCallback getSubmitCallback() {
        return formHandler.getSubmitCallback();
    }

    @Override
    public void setup(String formId) {
        formHandler.setup(formId);
    }

    @Override
    public void submitData(Map<String, Object> body, SubmitCallback callback) {
        formHandler.submitData(body, callback);
    }

    @Override
    public void showForm(Activity activity, SubmitCallback callback) {
        formHandler.showForm(activity, callback);
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
}
