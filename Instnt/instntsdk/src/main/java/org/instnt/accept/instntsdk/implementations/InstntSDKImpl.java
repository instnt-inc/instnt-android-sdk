package org.instnt.accept.instntsdk.implementations;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.InstntSDK;
import org.instnt.accept.instntsdk.enums.CallbackType;
import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DeviceHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntSDKImpl implements InstntSDK {

    private static final String TAG = "InstntSDKImpl";
    private DocumentHandler documentHandler;
    private OTPHandler otpHandler;
    private FormHandler formHandler;
    private DeviceHandler deviceHandler;
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
        deviceHandler = new DeviceHandlerImpl();
    }

    private void setServerURL(String serverURL) {
        networkModule.setServerUrl(serverURL);
    }

    private void setFormKey(String formKey) {
        this.formKey = formKey;
        this.documentHandler.setFormKey(formKey);
    }

    private void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
        this.documentHandler.setInstnttxnid(instnttxnid);
        this.otpHandler.setInstnttxnid(instnttxnid);
        this.formHandler.setInstnttxnid(instnttxnid);
    }

    private void setWorkFlowDetail(FormCodes formCodes) {
        this.formCodes = formCodes;
        this.formHandler.setWorkFlowDetail(formCodes);
    }

    private void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
        this.documentHandler.setCallbackHandler(callbackHandler);
        this.formHandler.setCallbackHandler(callbackHandler);
        this.otpHandler.setCallbackHandler(callbackHandler);
    }

    @Override
    public void initTransaction(String formKey, String serverUrl, CallbackHandler callbackHandler) {

        this.setServerURL(serverUrl);
        this.setFormKey(formKey);
        this.setCallbackHandler(callbackHandler);

        Log.i(TAG, "Calling getTransactionID");
        networkModule.getTransactionID(this.formKey).subscribe(response->{
            Log.i(TAG, "Calling getTransactionID returns with success response");
            this.setInstnttxnid(response.getInstnttxnid());
            this.setWorkFlowDetail(response);
            this.formCodes = response;
            this.callbackHandler.successCallBack(null, "Transaction initialization successfully", CallbackType.SUCCESS_INIT_TRANSACTION);
        }, throwable -> {
            Log.e(TAG, "Calling getTransactionID returns with error response", throwable);
            this.callbackHandler.errorCallBack("Transaction initialization failed", CallbackType.ERROR_INIT_TRANSACTION);
        });
    }

    @Override
    public void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey) {
        documentHandler.scanDocument(isFront, isAutoUpload, documentType, context, documentVerifyLicenseKey);
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
    public void submitForm(Context context, WindowManager windowManager, Map<String, Object> body) {
        body.put("mobileDeviceInfo", deviceHandler.getDeviceInfo(context, windowManager));
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
    public String getInstnttxnid() {
        return this.instnttxnid;
    }

    @Override
    public boolean isOTPverificationEnable() {
        return this.formCodes.isOtp_verification();
    }

    @Override
    public boolean isDocumentVerificationEnable() {
        return this.formCodes.isDocumentVerification();
    }
}
