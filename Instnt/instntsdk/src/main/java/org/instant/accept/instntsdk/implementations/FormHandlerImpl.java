package org.instant.accept.instntsdk.implementations;

import android.app.Activity;
import android.content.Intent;

import org.instant.accept.instntsdk.enums.CallbackType;
import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.model.FormCodes;
import org.instant.accept.instntsdk.interfaces.FormHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FormHandlerImpl implements FormHandler {

    private NetworkUtil networkModule;
    private FormCodes formCodes;
    private CallbackHandler callbackHandler;
    private String instnttxnid;

    public FormHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
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

    public void submitForm(Map<String, Object> body) {

        try {
            body.put("mobileNumber", URLEncoder.encode((String) body.get("mobileNumber"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        body.put("signature", this.instnttxnid);
        body.put("OTPSignature", this.instnttxnid);
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

        networkModule.submit(formCodes.getSubmitURL(), body).subscribe( success-> {
            this.callbackHandler.successCallBack(success.getData(), "", CallbackType.SUCCESS_FORM_SUBMIT);
        }, throwable -> {
            this.callbackHandler.errorCallBack("Some problem occurred when try to submit form", CallbackType.ERROR_FORM_SUBMIT);
        });
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
    }
}
