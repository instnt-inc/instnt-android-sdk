package org.instant.accept.instntsdk.implementations;

import android.content.Context;

import com.idmetrics.dc.DSHandler;
import com.idmetrics.dc.utils.DSCaptureMode;
import com.idmetrics.dc.utils.DSError;
import com.idmetrics.dc.utils.DSHandlerListener;
import com.idmetrics.dc.utils.DSID1Options;
import com.idmetrics.dc.utils.DSID1Type;
import com.idmetrics.dc.utils.DSOptions;
import com.idmetrics.dc.utils.DSPassportOptions;
import com.idmetrics.dc.utils.DSResult;
import com.idmetrics.dc.utils.DSSide;
import com.idmetrics.dc.utils.FlashCapture;

import org.instant.accept.instntsdk.enums.CallbackType;
import org.instant.accept.instntsdk.interfaces.CallbackHandler;
import org.instant.accept.instntsdk.interfaces.DocumentHandler;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class DocumentHandlerImpl implements DocumentHandler {

    private NetworkUtil networkModule;
    private CallbackHandler callbackHandler;
    private String formKey;
    private String instnttxnid;
    private Context context;
    private DSResult dsResult;

    public DocumentHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    private void uploadAttachment(DSResult dsResult, String instnttxnid, boolean ifFront) {

        boolean isSelfie = false;
        String docSuffix = "F";

        if(!ifFront)
            docSuffix = "B";

        if(isSelfie)
            docSuffix = "S";

        getUploadUrlAndUploadDoc(dsResult, docSuffix, instnttxnid);
    }

    private void getUploadUrlAndUploadDoc(DSResult dsResult, String docSuffix, String instnttxnid) {

        //Get presigned s3 url method on which file should upload
        networkModule.getUploadUrl(instnttxnid, docSuffix).subscribe(response->{

            uploadDocumentAfterGetUrl(dsResult, response, docSuffix, instnttxnid);
        }, throwable -> {
            //CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

    private void uploadDocumentAfterGetUrl(DSResult dsResult, Map<String, Object> response, String docSuffix, String instnttxnid) {

        networkModule.uploadDocument(instnttxnid + docSuffix + ".jpg", (String) response.get("s3_key"), dsResult.image);
        this.callbackHandler.successCallBack(dsResult.image, "", CallbackType.SUCCESS_IMAGE_UPLOAD);
    }

    private DSOptions getOptionsByDocumentType(boolean isFront, String documentType) {

        DSID1Type dsid1Type = null;

        try {
            dsid1Type = DSID1Type.valueOf(documentType);
        } catch (Exception e) {
            dsid1Type = DSID1Type.License;
        }

        switch (dsid1Type) {

            case License: {

                DSID1Options dsid1Options = new DSID1Options();
                dsid1Options.type = DSID1Type.License;
                dsid1Options.side = (isFront ? DSSide.Front : DSSide.Back);
                dsid1Options.detectFace = isFront;
                //dsid1Options.enableFlashCapture = if(applicationSettings.flashCaptureEnabled) FlashCapture.Both else FlashCapture.None
                dsid1Options.enableFlashCapture = FlashCapture.None;
                //dsid1Options.imageCompressionQuality = applicationSettings.imageCompression / 100.0;
                dsid1Options.showReviewScreen = true;
                //dsid1Options.targetDPI = (isBarcodeRetake ? 900 : 600);
                dsid1Options.targetDPI = 600;
                //dsid1Options.detectBarcodeOrMRZ = !applicationSettings.ignoreBarcode
                return dsid1Options;
            }

            case PassportCard: {

                DSPassportOptions options = new DSPassportOptions();
                //options.enableFlashCapture = if(applicationSettings.flashCaptureEnabled) FlashCapture.Both else FlashCapture.None
                options.enableFlashCapture = FlashCapture.None;
                //options.imageCompressionQuality = applicationSettings.imageCompression / 100.0
                options.showReviewScreen = true;
                //options.detectMRZ = applicationSettings.mrzCheckEnabled
                options.targetDPI = 600;
                return options;
            }
        }

        return new DSOptions();
    }

    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void scanDocument(boolean ifFront, boolean isAutoUpload, String documentType) {

        DocumentHandlerImpl documentHandler = this;
        DSOptions dsOptions = getOptionsByDocumentType(ifFront, documentType);
        dsOptions.licensingKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";

        DSHandler dsHandler = DSHandler.getInstance(this.context);
        DSHandler.staticLicenseKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";
        dsHandler.options = dsOptions;

        dsHandler.init(DSCaptureMode.Manual, new DSHandlerListener() {
            @Override
            public void handleScan(DSResult dsResult) {

                documentHandler.dsResult = dsResult;

                if(isAutoUpload)
                    uploadAttachment(dsResult, documentHandler.instnttxnid, ifFront);

                documentHandler.callbackHandler.successCallBack(null, "Document scanned successfully", CallbackType.SUCCESS_DOC_SCAN);
            }

            @Override
            public void scanWasCancelled() {

                documentHandler.callbackHandler.errorCallBack("Please approve scan", CallbackType.ERROR_DOC_SCAN_CANCELLED);
            }

            @Override
            public void captureError(DSError dsError) {
                documentHandler.callbackHandler.errorCallBack(dsError.message, CallbackType.ERROR_DOC_SCAN_NOT_CAPTURED);
            }
        });

        dsHandler.start();
    }

    @Override
    public void uploadAttachment(boolean ifFront) {

        uploadAttachment(this.dsResult, this.instnttxnid, ifFront);
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

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
