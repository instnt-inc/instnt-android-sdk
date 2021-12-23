package org.instant.accept.instntsdk;

import android.app.Activity;
import android.content.Context;

import org.instant.accept.instntsdk.implementations.DocumentHandlerImpl;
import org.instant.accept.instntsdk.implementations.FormHandlerImpl;
import org.instant.accept.instntsdk.implementations.OTPHandlerImpl;
import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.interfaces.Instnt;
import org.instant.accept.instntsdk.interfaces.OTPHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntSDK implements Instnt {

    private static InstntSDK instance;
    private DocumentHandler documentHandler;
    private OTPHandler otpHandler;
    private FormHandler formHandler;
    private NetworkUtil networkModule;
    //TODO passing it true because for testing purpose need to fire dev url, it should be false after the final testing
    private boolean isSandbox = true;
    private String instnttxnid;
    private String serverUrl;
    private String formKey;

    public static InstntSDK getInstance(String formKey, String serverUrl, Context context) {
        if (instance == null) {
            instance = new InstntSDK(serverUrl);
        }

        instance.formKey = formKey;
        instance.serverUrl = serverUrl;
        //first api to call
        instance.initTransaction(context);

        return instance;
    }

    private InstntSDK(String serverUrl) {

        networkModule = new NetworkUtil(serverUrl);
        documentHandler = new DocumentHandlerImpl(networkModule);
        otpHandler = new OTPHandlerImpl(networkModule);
        formHandler = new FormHandlerImpl(networkModule);
    }

    private void initTransaction(Context context) {

        networkModule.getTransactionID(this.formKey).subscribe(response->{

            System.out.println("Response");
            this.instnttxnid = (String) response.get("instnttxnid");
        }, throwable -> {
            CommonUtils.showToast(context, CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

    @Override
    public void scanAndUploadDocument(Context context, String instnttxnid) {
        documentHandler.scanAndUploadDocument(context, instnttxnid);
    }

    @Override
    public void setCallback(SubmitCallback callback) {
        formHandler.setCallback(callback);
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
    public void submitForm(Map<String, Object> body, SubmitCallback callback) {
        formHandler.submitForm(body, callback);
    }

    @Override
    public void showForm(Activity activity, SubmitCallback callback) {
        formHandler.showForm(activity, callback);
    }

    @Override
    public void sendOTP(String mobileNumber, Context context) {
        otpHandler.sendOTP(mobileNumber, context);
    }

    @Override
    public void verifyOTP(String mobileNumber, String otpCode, Context context) {
        otpHandler.verifyOTP(mobileNumber, otpCode, context);
    }

    @Override
    public String getTransactionID() {
        return this.instnttxnid;
    }

}
