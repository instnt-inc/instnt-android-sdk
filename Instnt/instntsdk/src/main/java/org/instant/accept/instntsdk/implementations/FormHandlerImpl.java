package org.instant.accept.instntsdk.implementations;

import android.app.Activity;
import android.content.Intent;

import org.instant.accept.instntsdk.model.FormCodes;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;
import org.instant.accept.instntsdk.view.FormActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FormHandlerImpl implements FormHandler {

    private NetworkUtil networkModule;
    private FormCodes formCodes;
    private SubmitCallback submitCallback;

    public FormHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public void setCallback(SubmitCallback callback) {
        this.submitCallback = callback;
    }

    @Override
    public SubmitCallback getSubmitCallback() {
        return submitCallback;
    }

    @Override
    public void setup(String formId) {

        networkModule.getFormFields(formId).subscribe(
                success -> {
                    this.formCodes = success;
                }, throwable -> {
                    this.formCodes = null;
                }
        );
    }

    @Override
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

    @Override
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
