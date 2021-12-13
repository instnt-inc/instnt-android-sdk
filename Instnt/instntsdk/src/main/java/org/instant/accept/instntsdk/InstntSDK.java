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

    public static InstntSDK getInstance() {
        if (instance == null) {
            instance = new InstntSDK();
        }

        return instance;
    }

    private InstntSDK() {
        //first api to call
        initTransaction();
        networkModule = new NetworkUtil();
        documentHandler = new DocumentHandlerImpl();
        otpHandler = new OTPHandlerImpl();
        formHandler = new FormHandlerImpl();
    }

    private void initTransaction() {

        networkModule.getTransactionID(this.isSandbox, "v1633477069641729").subscribe(response->{

            System.out.println("Response");
            this.instnttxnid = (String) response.get("instnttxnid");
        }, throwable -> {
            //CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
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
    public void setup(String formId, boolean isSandbox) {
        formHandler.setup(formId, isSandbox);
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
    public void send() {
        otpHandler.send();
    }

    @Override
    public void verify() {
        otpHandler.verify();
    }

    @Override
    public String getTransactionID() {
        return this.instnttxnid;
    }

}
