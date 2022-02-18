package org.instnt.accept.instntsdk.implementations;

import android.content.Context;
import android.util.Log;

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

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.model.InstntImageData;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class DocumentHandlerImpl implements DocumentHandler {

    private NetworkUtil networkModule;
    private InstntCallbackHandler instntCallbackHandler;
    private String formKey;
    private String instnttxnid;
    private DSResult dsResult;

    public DocumentHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    private void uploadAttachment(byte[] imageData, String instnttxnid, boolean isFront, boolean isSelfie) {

        Log.i(CommonUtils.LOG_TAG, "Calling UploadAttachment");
        String docSuffix = "F";

        if(!isFront)
            docSuffix = "B";

        if(isSelfie)
            docSuffix = "S";

        getUploadUrlAndUploadDoc(imageData, docSuffix, instnttxnid);
    }

    private void getUploadUrlAndUploadDoc(byte[] imageData, String docSuffix, String instnttxnid) {

        Log.i(CommonUtils.LOG_TAG, "Calling Get document upload url");
        //Get presigned s3 url method on which file should upload
        networkModule.getUploadUrl(instnttxnid, docSuffix).subscribe(response->{

            Log.i(CommonUtils.LOG_TAG, "Get document upload URL successfully");
            uploadDocumentAfterGetUrl(imageData, response, docSuffix, instnttxnid);
        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Have error when call get document upload URL", throwable);
        });
    }

    private void uploadDocumentAfterGetUrl(byte[] imageData, Map<String, Object> response, String docSuffix, String instnttxnid) {

        Log.i(CommonUtils.LOG_TAG, "Upload document to the fetched presigned URL");
        networkModule.uploadDocument(instnttxnid + docSuffix + ".jpg", (String) response.get("s3_key"), imageData);
        Log.i(CommonUtils.LOG_TAG, "Document uploaded successfully");
        InstntImageData instntImageData = new InstntImageData();
        instntImageData.setImageData(imageData);
        if(docSuffix.equalsIgnoreCase("S")) {
            instntImageData.setType("Selfie");
        } else {
            instntImageData.setType("License");
        }
        instntImageData.setDocumentSide(docSuffix);
        this.instntCallbackHandler.uploadAttachmentSuccessCallback(instntImageData);
    }

    private DSOptions getOptionsByDocumentType(boolean isFront, String documentType, boolean isAutoUpload) {

        Log.i(CommonUtils.LOG_TAG, "Going to get options by document type, DocumentType : " + documentType + ", IsFrontDocument : " + isFront);
        DSID1Type dsid1Type = null;

        try {
            dsid1Type = DSID1Type.valueOf(documentType);
        } catch (Exception e) {
            Log.e(CommonUtils.LOG_TAG, "Document type mismatch with allowed types, passed document type : " + documentType + ", Auto taken document type : " + DSID1Type.License.toString(), e);
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
                dsid1Options.showReviewScreen = !isAutoUpload;
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

        Log.i(CommonUtils.LOG_TAG, "Calling Scan Document");
        DocumentHandlerImpl documentHandler = this;
        boolean isSelfie = false;
        DSOptions dsOptions = getOptionsByDocumentType(isFront, documentType, isAutoUpload);

        DSHandler dsHandler = DSHandler.getInstance(context);
        DSHandler.staticLicenseKey = documentVerifyLicenseKey;
        dsHandler.options = dsOptions;

        dsHandler.init(DSCaptureMode.Manual, new DSHandlerListener() {
            @Override
            public void handleScan(DSResult dsResult) {

                Log.i(CommonUtils.LOG_TAG, "Document scanned successfully");
                documentHandler.dsResult = dsResult;

                //if(isAutoUpload)
                uploadAttachment(dsResult.image, documentHandler.instnttxnid, isFront, isSelfie);
                InstntImageData instntImageData = new InstntImageData();
                instntImageData.setImageData(dsResult.image);
                if(documentType.equalsIgnoreCase("license")) {
                    instntImageData.setType("License");
                } else {
                    instntImageData.setType("Selfie");
                }
                if(isFront) {
                    instntImageData.setDocumentSide("F");
                } else {
                    instntImageData.setDocumentSide("B");
                }
                documentHandler.instntCallbackHandler.scanDocumentSuccessCallback(instntImageData);
            }

            @Override
            public void scanWasCancelled() {

                Log.w(CommonUtils.LOG_TAG, "Scan document was cancelled");
                documentHandler.instntCallbackHandler.instntErrorCallback("Please approve scan", ErrorCallbackType.SCAN_DOCUMENT_CANCELLED_ERROR);
            }

            @Override
            public void captureError(DSError dsError) {

                Log.e(CommonUtils.LOG_TAG, "Scan document has return with error : " + dsError.message);
                documentHandler.instntCallbackHandler.instntErrorCallback(dsError.message, ErrorCallbackType.SCAN_DOCUMENT_CAPTURE_ERROR);
            }
        });

        dsHandler.start();
    }

    /**
     * Upload attachment
     * @param isFront
     */
    @Override
    public void uploadAttachment(byte[] imageData, boolean isFront, boolean isSelfie) {

        uploadAttachment(imageData,this.instnttxnid, isFront, isSelfie);
    }

    /**
     * Verify documents
     * @param documentType
     */
    @Override
    public void verifyDocuments(String documentType) {

        Log.i(CommonUtils.LOG_TAG, "Calling verify documents");
        networkModule.verifyDocuments(documentType, this.formKey, this.instnttxnid).subscribe(response->{
            String message = "Verify documents initiated successfully";
            Log.i(CommonUtils.LOG_TAG, message);
            this.instntCallbackHandler.verifyDocumentsInitiationCallback(message);

        }, throwable -> {
            Log.e(CommonUtils.LOG_TAG, "Verify documents returns with error", throwable);
        });

    }

    /**
     * Set form key
     * @param formKey
     */
    @Override
    public void setFormKey(String formKey) {
        Log.i(CommonUtils.LOG_TAG, "Set form key");
        this.formKey = formKey;
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
     * Set callback handler
     * @param instntCallbackHandler
     */
    @Override
    public void setCallbackHandler(InstntCallbackHandler instntCallbackHandler) {
        Log.i(CommonUtils.LOG_TAG, "Set callbackHandler");
        this.instntCallbackHandler = instntCallbackHandler;
    }
}
