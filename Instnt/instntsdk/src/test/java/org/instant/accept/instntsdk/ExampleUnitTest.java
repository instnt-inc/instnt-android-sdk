package org.instant.accept.instntsdk;

import org.easymock.EasyMock;
import org.instant.accept.instntsdk.enums.CallbackType;
import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.model.FormCodes;
import org.instant.accept.instntsdk.model.FormSubmitData;
import org.instant.accept.instntsdk.model.FormSubmitResponse;
import org.instant.accept.instntsdk.model.OTPResponse;
import org.instant.accept.instntsdk.model.OTPVerificationResult;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String FORM_KEY = "v1633477069641729";
    private static final String SERVER_URL = "https://dev2-api.instnt.org/public/";
    private static final String INSTNTXNID = "https://dev2-api.instnt.org/public/";
    private FormCodes formCodes;
    private NetworkUtil networkUtil;

    @Before
    public void setup() {

        networkUtil = EasyMock.createNiceMock(NetworkUtil.class);

        formCodes = new FormCodes();
        formCodes.setClient_ip("49.34.164.172");
        formCodes.setBackendServiceURL("https://dev2-api.instnt.org");
        formCodes.setDocumentVerification(true);
        formCodes.setExpires_on(1641550253);
        formCodes.setId(FORM_KEY);
        formCodes.setOtp_verification(true);
        formCodes.setIdmetricsVersion("4.5.0.5");
    }

    @Test
    public void testGetFormFields() {

        expect(networkUtil.getFormFields(FORM_KEY)).andReturn(Observable.just(this.formCodes)).anyTimes();
        replay(networkUtil);

        networkUtil.getFormFields(FORM_KEY).subscribe(formCodes -> {
            assertNotNull(formCodes);
            assertEquals(this.formCodes.getClient_ip(), formCodes.getClient_ip());
            assertEquals(this.formCodes.getBackendServiceURL(), formCodes.getBackendServiceURL());
            assertEquals(this.formCodes.isDocumentVerification(), formCodes.isDocumentVerification());
            assertEquals(this.formCodes.getExpires_on(), formCodes.getExpires_on());
            assertEquals(this.formCodes.getId(), formCodes.getId());
            assertEquals(this.formCodes.isOtp_verification(), formCodes.isOtp_verification());
            assertEquals(this.formCodes.getIdmetricsVersion(), formCodes.getIdmetricsVersion());
        });
    }

    @Test
    public void testSubmit() {

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "test first name");
        body.put("surName", "test sure name");
        body.put("mobileNumber", "+16102458140");
        body.put("email", "donald@duck.com");
        body.put("physicalAddress", "newyork,usa");
        body.put("city", "newyork");
        body.put("state", "newyork");
        body.put("zip", "10001");
        body.put("country", "USA");

        FormSubmitData formSubmitData = new FormSubmitData();
        formSubmitData.setDecision("test decision");
        formSubmitData.setJwt("testjwttoken");
        formSubmitData.setStatus("200");
        formSubmitData.setUrl("https://dev2-api.instnt.org");

        FormSubmitResponse mockFormSubmitResponse = new FormSubmitResponse();
        mockFormSubmitResponse.setData(formSubmitData);

        expect(networkUtil.submit(SERVER_URL, body)).andReturn(Observable.just(mockFormSubmitResponse)).anyTimes();
        replay(networkUtil);

        networkUtil.submit(SERVER_URL, body).subscribe(formSubmitResponse -> {
            assertNotNull(formSubmitResponse);
            assertEquals(mockFormSubmitResponse.getData().getDecision(), formSubmitResponse.getData().getDecision());
            assertEquals(mockFormSubmitResponse.getData().getJwt(), formSubmitResponse.getData().getJwt());
            assertEquals(mockFormSubmitResponse.getData().getStatus(), formSubmitResponse.getData().getStatus());
            assertEquals(mockFormSubmitResponse.getData().getUrl(), formSubmitResponse.getData().getUrl());
        });
    }

    @Test
    public void testSendOTP() {

        OTPVerificationResult otpVerificationResult = new OTPVerificationResult();
        otpVerificationResult.setId("VE141b7ed3f2610424998a662a8fd23149");
        String[] errors = {};
        otpVerificationResult.setErrors(errors);
        otpVerificationResult.setValid(true);

        OTPResponse mockOtpResponse = new OTPResponse();
        mockOtpResponse.setResponse(otpVerificationResult);

        expect(networkUtil.sendOTP("+16102458140", INSTNTXNID)).andReturn(Observable.just(mockOtpResponse)).anyTimes();
        replay(networkUtil);

        networkUtil.sendOTP("+16102458140", INSTNTXNID).subscribe(otpResponse -> {
            assertNotNull(otpResponse);
            assertEquals(mockOtpResponse.getResponse().getErrors().length, otpResponse.getResponse().getErrors().length);
            assertEquals(mockOtpResponse.getResponse().getId(), otpResponse.getResponse().getId());
            assertEquals(mockOtpResponse.getResponse().isValid(), otpResponse.getResponse().isValid());
        });
    }
}