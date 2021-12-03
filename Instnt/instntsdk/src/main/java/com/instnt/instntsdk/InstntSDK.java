package com.instnt.instntsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.instnt.instntsdk.authentication.AuthenticID;
import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.network.NetworkUtil;
import com.instnt.instntsdk.utils.CommonUtils;
import com.instnt.instntsdk.view.FormActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InstntSDK {

    private NetworkUtil networkModule;
    private AuthenticID authentication;
    private SubmitCallback submitCallback;
    private FormCodes formCodes;

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

    public void startAuthentication(Context baseContext) {
        authentication = new AuthenticID(baseContext);
    }

    @SuppressLint("CheckResult")
    public void setup(String formId, boolean isSandbox) {
        networkModule.getFormFields(formId, isSandbox).subscribe(
                success -> {
                    this.formCodes = success;
                }, throwable -> {
                    this.formCodes = null;
                }
        );
    }

    @SuppressLint("CheckResult")
    public void submitForm(Map<String, Object> body, SubmitCallback callback) {
        if (formCodes == null) {
            if (callback != null) {
                callback.didSubmit(null, "");
            }
            return;
        }

        body.put("form_key", formCodes.getId());

        Map <String, Object> fingerMap = new HashMap<>();
        fingerMap.put("requestId", formCodes.getFingerprint());
        fingerMap.put("visitorId", formCodes.getFingerprint());
        fingerMap.put("visitorFound", true);

        body.put("fingerprint", fingerMap);
        body.put("client_referer_url", formCodes.getBackendServiceURL());

        try {
            body.put("client_referer_host", new URL(formCodes.getBackendServiceURL()).getHost());
        } catch (MalformedURLException e) {
            body.put("client_referer_host", "");
        }

        networkModule.submit(formCodes.getSubmitURL(), body, false).subscribe(
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

    public void showForm(Activity activity, SubmitCallback callback) {
        this.submitCallback = callback;

        if (formCodes == null) {
            if (submitCallback != null) {
                submitCallback.didSubmit(null, "");
            }

            return;
        }

        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(FormActivity.FORM_CORDS, formCodes);
        activity.startActivity(intent);
    }
}
