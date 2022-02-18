package org.instnt.accept.instntsdk.implementations;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import org.instnt.accept.instntsdk.InstntSDK;
import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.interfaces.DeviceHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntSDKImpl implements InstntSDK {

    private DocumentHandler documentHandler;
    private OTPHandler otpHandler;
    private FormHandler formHandler;
    private DeviceHandler deviceHandler;
    private NetworkUtil networkModule;
    private String instnttxnid;
    private String formKey;
    private InstntCallbackHandler instntCallbackHandler;
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

    private void setCallbackHandler(InstntCallbackHandler instntCallbackHandler) {
        this.instntCallbackHandler = instntCallbackHandler;
        this.documentHandler.setCallbackHandler(instntCallbackHandler);
        this.formHandler.setCallbackHandler(instntCallbackHandler);
        this.otpHandler.setCallbackHandler(instntCallbackHandler);
    }

    /**
     * Initialize transaction
     * @param formKey
     * @param serverUrl
     * @param instntCallbackHandler
     */
    @Override
    public void initTransaction(String formKey, String serverUrl, InstntCallbackHandler instntCallbackHandler) {

        this.setServerURL(serverUrl);
        this.setFormKey(formKey);
        this.setCallbackHandler(instntCallbackHandler);

        Log.i(CommonUtils.LOG_TAG, "Calling getTransactionID");
        networkModule.getTransactionID(this.formKey).subscribe(response->{
            Log.i(CommonUtils.LOG_TAG, "Calling getTransactionID returns with success response");
            this.setInstnttxnid(response.getInstnttxnid());
            this.setWorkFlowDetail(response);
            this.formCodes = response;
            this.instntCallbackHandler.initTransactionSuccessCallback(response.getInstnttxnid());
        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Calling getTransactionID returns with error response", throwable);
            this.instntCallbackHandler.instntErrorCallback("Transaction initialization failed", ErrorCallbackType.INIT_TRANSACTION_ERROR);
        });
    }

    /**
     * Scan document
     * @param isFront
     * @param isAutoUpload
     * @param documentType
     * @param context
     * @param documentVerifyLicenseKey
     */
    @Override
    public void scanDocument(boolean isFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey) {
        documentHandler.scanDocument(isFront, isAutoUpload, documentType, context, documentVerifyLicenseKey);
    }

    /**
     * Upload attachment
     * @param isFront
     */
    @Override
    public void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie) {
        this.documentHandler.uploadAttachment(imageData, isFront, isSelfie);
    }

    /**
     * Verify documents
     * @param documentType
     */
    @Override
    public void verifyDocuments(String documentType) {
        this.documentHandler.verifyDocuments(documentType);
    }

    /**
     * Submit form
     * @param context
     * @param windowManager
     * @param body
     */
    @Override
    public void submitData(Context context, WindowManager windowManager, Map<String, Object> body) {
        body.put("mobileDeviceInfo", deviceHandler.getDeviceInfo(context, windowManager));
        this.formHandler.submitData(body);
    }

    /**
     * Send OTP
     * @param mobileNumber
     */
    @Override
    public void sendOTP(String mobileNumber) {
        otpHandler.sendOTP(mobileNumber);
    }

    /**
     * Verify OTP
     * @param mobileNumber
     * @param otpCode
     */
    @Override
    public void verifyOTP(String mobileNumber, String otpCode) {
        otpHandler.verifyOTP(mobileNumber, otpCode);
    }

    /**
     * Get instnt transaction id
     * @return
     */
    @Override
    public String getInstnttxnid() {
        return this.instnttxnid;
    }

    /**
     * Check is otp verification enabled
     * @return
     */
    @Override
    public boolean isOTPverificationEnabled() {
        return this.formCodes.isOtpVerification();
    }

    /**
     * Check is document verification enabled
     * @return
     */
    @Override
    public boolean isDocumentVerificationEnabled() {
        return this.formCodes.isDocumentVerification();
    }
}
