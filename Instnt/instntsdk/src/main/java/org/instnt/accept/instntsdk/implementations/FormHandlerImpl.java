package org.instnt.accept.instntsdk.implementations;

import android.util.Log;

import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

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

    /**
     * Submit form
     * @param body
     */
    @Override
    public void submitData(Map<String, Object> body) {

        Log.i(CommonUtils.LOG_TAG, "Calling Submit form");
        try {
            body.put("mobileNumber", URLEncoder.encode((String) body.get("mobileNumber"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(CommonUtils.LOG_TAG, "Encode mobile number " + (String) body.get("mobileNumber") + " have error", e);
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
            Log.e(CommonUtils.LOG_TAG, "Add client referer host url having error, setting client_referer_host to blank", e);
            body.put("client_referer_host", "");
        }

        Log.i(CommonUtils.LOG_TAG, "Calling submit form API");
        networkModule.submit(formCodes.getSubmitURL(), body).subscribe( success-> {
            Log.i(CommonUtils.LOG_TAG, "Submit form called successfully");
            this.callbackHandler.submitDataSuccessCallBack(success.getData());
        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Submit form returns with error", throwable);
            this.callbackHandler.submitDataErrorCallBack(CommonUtils.getErrorMessage(throwable));
        });
    }

    /**
     * Set callback handler
     * @param callbackHandler
     */
    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        Log.i(CommonUtils.LOG_TAG, "Set callbackHandler");
        this.callbackHandler = callbackHandler;
    }

    /**
     * Set instnt transaction id
     * @param instnttxnid
     */
    @Override
    public void setInstnttxnid(String instnttxnid) {
        Log.i(CommonUtils.LOG_TAG, "Set instnttxnid");
        this.instnttxnid = instnttxnid;
    }

    /**
     * Set workflow detail
     * @param formCodes
     */
    @Override
    public void setWorkFlowDetail(FormCodes formCodes) {
        Log.i(CommonUtils.LOG_TAG, "Set formCodes");
        this.formCodes = formCodes;
    }
}
