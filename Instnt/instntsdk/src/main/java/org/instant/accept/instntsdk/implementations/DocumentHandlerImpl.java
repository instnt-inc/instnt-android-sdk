package org.instant.accept.instntsdk.implementations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.idmetrics.dc.DSHandler;
import com.idmetrics.dc.utils.DSCaptureMode;
import com.idmetrics.dc.utils.DSError;
import com.idmetrics.dc.utils.DSHandlerListener;
import com.idmetrics.dc.utils.DSID1Options;
import com.idmetrics.dc.utils.DSID1Type;
import com.idmetrics.dc.utils.DSResult;

import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DocumentHandlerImpl implements DocumentHandler {

    private NetworkUtil networkModule;
    private CallbackHandler callbackHandler;
    private String formKey;
    private String instnttxnid;

    public DocumentHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    private void uploadAttachment(DSResult dsResult, String instnttxnid) {

        boolean isSelfie = false;
        //TODO passing it true because for testing purpose need to fire dev url, it should be false after the final testing
        boolean isSandbox = true;
        String documentSide = "Back";
        String docSuffix = "F";

        if(documentSide != null && documentSide.equalsIgnoreCase("back"))
            docSuffix = "B";

        if(isSelfie)
            docSuffix = "S";

        getUploadUrlAndUploadDoc(dsResult, docSuffix, instnttxnid, isSandbox);
    }

    private void getUploadUrlAndUploadDoc(DSResult dsResult, String docSuffix, String instnttxnid, boolean isSandbox) {

        //Get presigned s3 url method on which file should upload
        //First get upload presigned s3 url
        networkModule.getUploadUrl(instnttxnid, docSuffix, isSandbox).subscribe(response->{

            System.out.println("Response");
            uploadDocumentAfterGetUrl(dsResult, response, docSuffix, instnttxnid, isSandbox);
        }, throwable -> {
            //CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

    private void uploadDocumentAfterGetUrl(DSResult dsResult, Map<String, Object> response, String docSuffix, String instnttxnid, boolean isSandbox) {

        String fileName = instnttxnid + docSuffix + ".jpg";
        byte[] imageData = dsResult.image;
        String presignedS3Url = (String) response.get("s3_key");

        networkModule.uploadDocument(fileName, presignedS3Url, imageData, isSandbox);
        this.callbackHandler.successCallBack(imageData);
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void uploadAttachment(Context context, String instnttxnid) {

        DSID1Options dsOptions = new DSID1Options();
        dsOptions.licensingKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";
        //dsOptions.type = DSID1Type.License;

        DSHandler dsHandler = DSHandler.getInstance(context);
        DSHandler.staticLicenseKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";
        dsHandler.options = dsOptions;
        dsHandler.init(DSCaptureMode.Manual, new DSHandlerListener() {
            @Override
            public void handleScan(DSResult dsResult) {
                uploadAttachment(dsResult, instnttxnid);
            }

            @Override
            public void scanWasCancelled() {
                System.out.println("test2");
            }

            @Override
            public void captureError(DSError dsError) {
                System.out.println("test3 : " + dsError.message);
            }
        });

        dsHandler.start();
    }

    @Override
    public void verifyDocuments(String documentType) {

        networkModule.verifyDocuments(documentType, this.formKey, this.instnttxnid).subscribe(response->{

            System.out.println("Response");
        }, throwable -> {
            //CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

    @Override
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    @Override
    public void setInstnttxnid(String instnttxnid) {
        this.instnttxnid = instnttxnid;
    }
}
