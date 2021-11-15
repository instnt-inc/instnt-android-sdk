package com.instnt.instntsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

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
    private SubmitCallback submitCallback;
    private FormCodes formCodes;

    private static InstntSDK instance;

    public static InstntSDK getInstance() {

        String _OSVERSION = System.getProperty("os.version");
        String _RELEASE = android.os.Build.VERSION.RELEASE;
        String _DEVICE = android.os.Build.DEVICE;
        String _MODEL = android.os.Build.MODEL;
        String _PRODUCT = android.os.Build.PRODUCT;
        String _BRAND = android.os.Build.BRAND;
        String _DISPLAY = android.os.Build.DISPLAY;
        String _CPU_ABI = android.os.Build.CPU_ABI;
        String _CPU_ABI2 = android.os.Build.CPU_ABI2;
        String _UNKNOWN = android.os.Build.UNKNOWN;
        String _HARDWARE = android.os.Build.HARDWARE;
        String _ID = android.os.Build.ID;
        String _MANUFACTURER = android.os.Build.MANUFACTURER;
        String _SERIAL = android.os.Build.SERIAL;
        String _USER = android.os.Build.USER;
        String _HOST = android.os.Build.HOST;

        System.out.println("_OSVERSION: " + _OSVERSION);
        System.out.println("_RELEASE: " + _RELEASE);
        System.out.println("_DEVICE: " + _DEVICE);
        System.out.println("_MODEL: " + _MODEL);
        System.out.println("_PRODUCT: " + _PRODUCT);
        System.out.println("_BRAND: " + _BRAND);
        System.out.println("_DISPLAY: " + _DISPLAY);
        System.out.println("_CPU_ABI: " + _CPU_ABI);
        System.out.println("_CPU_ABI2: " + _CPU_ABI2);
        System.out.println("_UNKNOWN: " + _UNKNOWN);
        System.out.println("_HARDWARE: " + _HARDWARE);
        System.out.println("_ID: " + _ID);
        System.out.println("_MANUFACTURER: " + _MANUFACTURER);
        System.out.println("_SERIAL: " + _SERIAL);
        System.out.println("_USER: " + _USER);
        System.out.println("_HOST: " + _HOST);

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
