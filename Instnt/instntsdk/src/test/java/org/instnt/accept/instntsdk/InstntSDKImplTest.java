package org.instnt.accept.instntsdk;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.WindowManager;

import org.easymock.EasyMock;
import org.instnt.accept.instntsdk.enums.DocumentType;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.exceptions.InstntSDKValidationException;
import org.instnt.accept.instntsdk.implementations.InstntSDKImpl;
import org.instnt.accept.instntsdk.interfaces.DeviceHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.interfaces.FormHandler;
import org.instnt.accept.instntsdk.interfaces.OTPHandler;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.model.FormSubmitData;
import org.instnt.accept.instntsdk.model.InstntImageData;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class InstntSDKImplTest implements InstntCallbackHandler {

    private static final String FORM_KEY = "v1633477069641729";
    private static final String SERVER_URL = "https://dev2-api.instnt.org/public/";
    private static final String INSTNTXNID = "ea5b85d2-775a-4432-9551-9e9e79218a7c";

    private InstntSDKImpl instntSDKImpl;
    private NetworkUtil networkUtilEasy;
    private DocumentHandler documentHandler;
    private FormHandler formHandler;
    private DeviceHandler deviceHandler;
    private OTPHandler otpHandler;
    private FormCodes formCodes;
    private Context context;
    private WindowManager windowManager;
    private Map<String, Object> body;

    @Before
    public void doBfore() {

        this.networkUtilEasy = EasyMock.createNiceMock(NetworkUtil.class);
        this.documentHandler = EasyMock.createNiceMock(DocumentHandler.class);
        this.context = EasyMock.createNiceMock(Context.class);
        this.deviceHandler = EasyMock.createNiceMock(DeviceHandler.class);
        this.windowManager = EasyMock.createNiceMock(WindowManager.class);
        this.formHandler = EasyMock.createNiceMock(FormHandler.class);
        this.otpHandler = EasyMock.createNiceMock(OTPHandler.class);

        formCodes = new FormCodes();
        formCodes.setId("v1633477069641729");
        formCodes.setFingerprint("uC2jNKwTbd1PbA22aLDr");
        formCodes.setBackendServiceURL("https://dev2-api.instnt.org");
        formCodes.setSubmitURL("https://dev2-api.instnt.org/public/transactions/ea5b85d2-775a-4432-9551-9e9e79218a7c/");
        formCodes.setFieldIds("4,11,10,3,2,1,14");
        formCodes.setUserId("26");
        formCodes.setIframe(false);
        formCodes.setOnlyGetHtml(false);
        formCodes.setHostURL("https://dev.instnt.org");
        formCodes.setDocumentVerification(true);
        formCodes.setHideFormFields(true);
        formCodes.setIdmetricsVersion("4.5.0.5");
        formCodes.setOtpVerification(false);
        formCodes.setInstnttxnid(InstntSDKImplTest.INSTNTXNID);

        this.body = new HashMap<>();
        this.body.put("email", "donald@duck.com");
    }

    @Override
    public void uploadAttachmentSuccessCallback(InstntImageData imageData) {

    }

    @Override
    public void scanDocumentSuccessCallback(InstntImageData imageData) {

    }

    @Override
    public void submitDataSuccessCallback(FormSubmitData formSubmitData) {

    }

    @Override
    public void initTransactionSuccessCallback(String instnttxnid) {
        Assert.assertEquals(instnttxnid, this.formCodes.getInstnttxnid());
    }

    @Override
    public void sendOTPSuccessCallback(String message) {

    }

    @Override
    public void verifyOTPSuccessCallback(String message) {

    }

    @Override
    public void verifyDocumentsInitiationCallback(String message) {

    }

    @Override
    public void instntErrorCallback(String message, ErrorCallbackType errorCallbackType) {

    }

    @Test(expected = InstntSDKValidationException.class)
    public void initTransactionNullFormKeyTest() {
        this.instntSDKImpl = new InstntSDKImpl(null, InstntSDKImplTest.SERVER_URL, this);
        expect(this.networkUtilEasy.getTransactionID(isA(String.class))).andReturn(Observable.just(this.formCodes)).anyTimes();
        this.instntSDKImpl.setNetworkModule(this.networkUtilEasy);
        replay(this.networkUtilEasy);
        this.instntSDKImpl.initTransaction();
    }

    @Test(expected = InstntSDKValidationException.class)
    public void initTransactionNullServerUrlTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, null, this);
        expect(this.networkUtilEasy.getTransactionID(isA(String.class))).andReturn(Observable.just(this.formCodes)).anyTimes();
        this.instntSDKImpl.setNetworkModule(this.networkUtilEasy);
        replay(this.networkUtilEasy);
        this.instntSDKImpl.initTransaction();
    }

    @Test(expected = InstntSDKValidationException.class)
    public void initTransactionNullCallbackHandlerTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, null);
        expect(this.networkUtilEasy.getTransactionID(isA(String.class))).andReturn(Observable.just(this.formCodes)).anyTimes();
        this.instntSDKImpl.setNetworkModule(this.networkUtilEasy);
        replay(this.networkUtilEasy);
        this.instntSDKImpl.initTransaction();
    }

    @Test
    public void initTransactionSuccessTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        expect(this.networkUtilEasy.getTransactionID(isA(String.class))).andReturn(Observable.just(this.formCodes)).anyTimes();
        this.instntSDKImpl.setNetworkModule(this.networkUtilEasy);
        replay(this.networkUtilEasy);
        this.instntSDKImpl.initTransaction();
    }

    @Test(expected = InstntSDKValidationException.class)
    public void scanDocumentDocumentTypeNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.scanDocument(false, true, null, this.context, "TestLicenceKey", INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void scanDocumentContextNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.scanDocument(false, true, DocumentType.License, null, "TestLicenceKey", INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void scanDocumentDocumentVerifyLicenseKeyNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.scanDocument(false, true, DocumentType.License, this.context, null, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void scanDocumentInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.scanDocument(false, true, DocumentType.License, this.context, "TestLicenceKey", null);
    }

    @Test
    public void uploadAttachmentTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        byte[] imageData = { (byte) 204, 29, (byte) 207, (byte) 217 };
        this.instntSDKImpl.uploadAttachment(imageData, true, false, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void uploadAttachmentImageDataNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.uploadAttachment(null, true, false, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void uploadAttachmentInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        byte[] imageData = { (byte) 204, 29, (byte) 207, (byte) 217 };
        this.instntSDKImpl.uploadAttachment(imageData, true, false, null);
    }

    @Test
    public void verifyDocumentsTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.verifyDocuments(DocumentType.License, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void verifyDocumentsDocumentTypeNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.verifyDocuments(null, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void verifyDocumentsInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setDocumentHandler(this.documentHandler);
        replay(this.documentHandler);
        this.instntSDKImpl.verifyDocuments(DocumentType.License, null);
    }

    @Test
    public void submitDataTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setFormHandler(this.formHandler);
        this.instntSDKImpl.setDeviceHandler(this.deviceHandler);
        replay(this.formHandler);
        this.instntSDKImpl.submitData(this.context, this.windowManager, this.body, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void submitDataContextNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setFormHandler(this.formHandler);
        this.instntSDKImpl.setDeviceHandler(this.deviceHandler);
        replay(this.formHandler);
        this.instntSDKImpl.submitData(null, this.windowManager, this.body, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void submitDataWindowManagerNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setFormHandler(this.formHandler);
        this.instntSDKImpl.setDeviceHandler(this.deviceHandler);
        replay(this.formHandler);
        this.instntSDKImpl.submitData(this.context, null, this.body, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void submitDataBodyNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setFormHandler(this.formHandler);
        this.instntSDKImpl.setDeviceHandler(this.deviceHandler);
        replay(this.formHandler);
        this.instntSDKImpl.submitData(this.context, this.windowManager, null, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void submitDataInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setFormHandler(this.formHandler);
        this.instntSDKImpl.setDeviceHandler(this.deviceHandler);
        replay(this.formHandler);
        this.instntSDKImpl.submitData(this.context, this.windowManager, this.body, null);
    }

    @Test
    public void sendOTPTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.sendOTP("+11234567890", INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void sendOTPMobileNumberNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.sendOTP(null, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void sendOTPInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.sendOTP("+11234567890", null);
    }

    @Test
    public void verifyOTPTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.verifyOTP("+11234567890", "1234", INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void verifyOTPMobileNumberNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.verifyOTP(null, "1234", INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void verifyOTPOtpCodeNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.verifyOTP("+11234567890", null, INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void verifyOTPInstnttxnidNullTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.setOTPHandler(this.otpHandler);
        replay(this.otpHandler);
        this.instntSDKImpl.verifyOTP("+11234567890", "1234", null);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void getInstnttxnidTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.getInstnttxnid();
    }

    @Test(expected = InstntSDKValidationException.class)
    public void isOTPverificationEnabledTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.isOTPverificationEnabled();
    }

    @Test(expected = InstntSDKValidationException.class)
    public void isDocumentVerificationEnabledTest() {
        this.instntSDKImpl = new InstntSDKImpl(InstntSDKImplTest.FORM_KEY, InstntSDKImplTest.SERVER_URL, this);
        this.instntSDKImpl.isDocumentVerificationEnabled();
    }
}