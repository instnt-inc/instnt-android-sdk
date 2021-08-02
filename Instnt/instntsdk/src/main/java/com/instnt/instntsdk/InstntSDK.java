package com.instnt.instntsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.interfaces.GetFormCallback;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.network.NetworkUtil;
import com.instnt.instntsdk.utils.CommonUtils;
import com.instnt.instntsdk.view.FormActivity;

import java.util.Map;

public class InstntSDK {

    private NetworkUtil networkModule;
    private SubmitCallback submitCallback;
    private String formId;
    private boolean isSandbox;

    private static InstntSDK instance;

    public static InstntSDK getInstance() {
        if (instance == null) {
            instance = new InstntSDK();
        }

        return instance;
    }

    public InstntSDK() {
        networkModule = new NetworkUtil();
    }

    public void setCallback(SubmitCallback callback) {
        this.submitCallback = callback;
    }

    public SubmitCallback getSubmitCallback() {
        return submitCallback;
    }

    public void setup(String formId, boolean isSandbox) {
        this.formId = formId;
        this.isSandbox = isSandbox;
    }

    @SuppressLint("CheckResult")
    public void getFormData(GetFormCallback callback) {

        if (TextUtils.isEmpty(formId)){
            callback.onResult(false, null, "Empty Form Id!");
            return;
        }

        networkModule.getFormFields(formId, isSandbox).subscribe(
                success->{
                    if (callback != null)
                        callback.onResult(true, success, "Success");
                }, throwable -> {
                    if (callback != null)
                        callback.onResult(false, null, CommonUtils.getErrorMessage(throwable));
                }
        );
    }

    @SuppressLint("CheckResult")
    public void submitForm(String url, Map<String, Object> body, SubmitCallback callback) {
        networkModule.submit(url, body, false).subscribe(
                success->{
                    if (callback != null) {
                        callback.didSubmit(success.getData(), "");
                    }
                }, throwable -> {
                    if (callback != null) {
                        callback.didSubmit(null, CommonUtils.getErrorMessage(throwable));
                    }
                }
        );
    }

    public void showForm(Activity activity, FormCodes formCodes, SubmitCallback callback) {
        this.submitCallback = callback;

        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(FormActivity.FORM_CORDS, formCodes);
        activity.startActivity(intent);
    }
}
