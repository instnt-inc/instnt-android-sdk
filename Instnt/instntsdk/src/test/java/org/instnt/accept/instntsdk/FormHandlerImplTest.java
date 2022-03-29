package org.instnt.accept.instntsdk;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import org.easymock.EasyMock;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.exceptions.InstntSDKValidationException;
import org.instnt.accept.instntsdk.implementations.FormHandlerImpl;
import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.model.FormSubmitData;
import org.instnt.accept.instntsdk.model.FormSubmitResponse;
import org.instnt.accept.instntsdk.model.InstntImageData;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class FormHandlerImplTest implements InstntCallbackHandler {

    private static final String FORM_KEY = "v1633477069641729";
    private static final String SERVER_URL = "https://dev2-api.instnt.org/public/";
    private static final String INSTNTXNID = "ea5b85d2-775a-4432-9551-9e9e79218a7c";

    private FormHandlerImpl formHandler;
    private FormCodes formCodes;
    private Map<String, Object> body;
    private NetworkUtil networkUtilEasy;
    private FormSubmitData formSubmitData;

    @Before
    public void doBfore() {

        networkUtilEasy = EasyMock.createNiceMock(NetworkUtil.class);

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
        formCodes.setInstnttxnid(FormHandlerImplTest.INSTNTXNID);

        body = new HashMap<>();
        body.put("zip", "10001");
        body.put("firstName", "parth");
        body.put("country", "usa");
        body.put("surName", "solanki");
        body.put("city", "newyork");
        body.put("mobileNumber", "+918866986290");
        body.put("physicalAddress", "newyork,usa");
        body.put("state", "newyork");
        body.put("email", "donald@duck.com");

        formSubmitData = new FormSubmitData();
        formSubmitData.setDecision("test decision");
        formSubmitData.setJwt("testjwttoken");
        formSubmitData.setStatus("200");
        formSubmitData.setUrl("https://dev2-api.instnt.org");
    }

    @Override
    public void uploadAttachmentSuccessCallback(InstntImageData imageData) {

    }

    @Override
    public void scanDocumentSuccessCallback(InstntImageData imageData) {

    }

    @Override
    public void submitDataSuccessCallback(FormSubmitData formSubmitData) {
        Assert.assertEquals(this.formSubmitData.getDecision(), formSubmitData.getDecision());
        Assert.assertEquals(this.formSubmitData.getJwt(), formSubmitData.getJwt());
        Assert.assertEquals(this.formSubmitData.getStatus(), formSubmitData.getStatus());
        Assert.assertEquals(this.formSubmitData.getUrl(), formSubmitData.getUrl());
    }

    @Override
    public void initTransactionSuccessCallback(String instnttxnid) {

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
        System.out.println("Error message: " + message);
        System.out.println("Error Type : " + errorCallbackType.name());
    }

    @Test
    public void formSubmit200SuccessNotNullTest() {

        networkUtilEasy.setServerUrl(SERVER_URL);

        FormSubmitResponse formSubmitResponse = new FormSubmitResponse();
        formSubmitResponse.setData(this.formSubmitData);

        expect(networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.just(formSubmitResponse)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        formHandler.setCallbackHandler(this);
        replay(networkUtilEasy);

        this.formHandler.setServerUrl(FormHandlerImplTest.SERVER_URL);
        this.formHandler.setWorkFlowDetail(this.formCodes);

        formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test
    public void formSubmit200SuccessNullTest() {

        FormSubmitResponse formSubmitResponse = EasyMock.createNiceMock(FormSubmitResponse.class);

        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.just(formSubmitResponse)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setCallbackHandler(this);
        this.formHandler.setWorkFlowDetail(this.formCodes);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test
    public void formSubmitThrowableSuccessTest() {

        String errorContent = "{\"formKey\":\"v390156100000\",\"stackTrace\":[\"filesubmit-form.py\",\"functionsubmitFormWithNewData()\",\"Email 'kyrewilder98gmail.com' is invalid.\"],\"errorType\":\"Exception\",\"errorMessage\":\"Email 'kyrewilder98gmail.com' is invalid.\"}";
        Response response = Response.error(422, ResponseBody.create(MediaType.parse("application/json"), errorContent));
        Throwable throwable = new HttpException(response);

        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.error(throwable)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setCallbackHandler(this);
        this.formHandler.setWorkFlowDetail(this.formCodes);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test
    public void formSubmitThrowableWithoutMessageTest() {

        Throwable throwable = new Exception();
        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.error(throwable)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setCallbackHandler(this);
        this.formHandler.setWorkFlowDetail(this.formCodes);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void formSubmitNullCallbackTest() {

        FormSubmitResponse formSubmitResponse = EasyMock.createNiceMock(FormSubmitResponse.class);

        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.just(formSubmitResponse)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setWorkFlowDetail(this.formCodes);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test
    public void formSubmitWithNullWorkflowDetailTest() {

        FormSubmitResponse formSubmitResponse = new FormSubmitResponse();
        formSubmitResponse.setData(this.formSubmitData);

        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.just(formSubmitResponse)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setCallbackHandler(this);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, FormHandlerImplTest.INSTNTXNID);
    }

    @Test(expected = InstntSDKValidationException.class)
    public void formSubmitWithNullInstntxnidTest() {

        FormSubmitResponse formSubmitResponse = new FormSubmitResponse();
        formSubmitResponse.setData(this.formSubmitData);

        expect(this.networkUtilEasy.submit(isA(String.class), isA(Map.class))).andReturn(Observable.just(formSubmitResponse)).anyTimes();
        this.formHandler = new FormHandlerImpl(this.networkUtilEasy);
        this.formHandler.setCallbackHandler(this);
        replay(this.networkUtilEasy);

        this.formHandler.submitData(body, null);
    }
}