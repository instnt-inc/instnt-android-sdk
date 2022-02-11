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

import org.instnt.accept.instntsdk.enums.CallbackType;
import org.instnt.accept.instntsdk.interfaces.CallbackData;
import org.instnt.accept.instntsdk.interfaces.CallbackHandler;
import org.instnt.accept.instntsdk.interfaces.DocumentHandler;
import org.instnt.accept.instntsdk.network.NetworkUtil;
import org.instnt.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class DocumentHandlerImpl implements DocumentHandler {

    private static final String TAG = "DocumentHandlerImpl";
    private NetworkUtil networkModule;
    private CallbackHandler callbackHandler;
    private String formKey;
    private String instnttxnid;
    private DSResult dsResult;

    public DocumentHandlerImpl(NetworkUtil networkModule) {
        this.networkModule = networkModule;
    }

    private void uploadAttachment(DSResult dsResult, String instnttxnid, boolean ifFront) {

        Log.i(TAG, "Calling UploadAttachment");
        boolean isSelfie = false;
        String docSuffix = "F";

        if(!ifFront)
            docSuffix = "B";

        if(isSelfie)
            docSuffix = "S";

        getUploadUrlAndUploadDoc(dsResult, docSuffix, instnttxnid);
    }

    private void getUploadUrlAndUploadDoc(DSResult dsResult, String docSuffix, String instnttxnid) {

        Log.i(TAG, "Calling Get document upload url");
        //Get presigned s3 url method on which file should upload
        networkModule.getUploadUrl(instnttxnid, docSuffix).subscribe(response->{

            Log.i(TAG, "Get document upload URL successfully");
            uploadDocumentAfterGetUrl(dsResult, response, docSuffix, instnttxnid);
        }, throwable -> {
            Log.e(TAG, "Have error when call get document upload URL", throwable);
        });
    }

    private void uploadDocumentAfterGetUrl(DSResult dsResult, Map<String, Object> response, String docSuffix, String instnttxnid) {

        Log.i(TAG, "Upload document to the fetched presigned URL");
        networkModule.uploadDocument(instnttxnid + docSuffix + ".jpg", (String) response.get("s3_key"), dsResult.image);
        Log.i(TAG, "Document uploaded successfully");
        CallbackDataImpl callbackDataImpl = new CallbackDataImpl();
        callbackDataImpl.setImageBytes(dsResult.image);
        this.callbackHandler.successCallBack(callbackDataImpl, "", CallbackType.SUCCESS_IMAGE_UPLOAD);
    }

    private DSOptions getOptionsByDocumentType(boolean isFront, String documentType) {

        Log.i(TAG, "Going to get options by document type, DocumentType : " + documentType + ", IsFrontDocument : " + isFront);
        DSID1Type dsid1Type = null;

        try {
            dsid1Type = DSID1Type.valueOf(documentType);
        } catch (Exception e) {
            Log.e(TAG, "Document type mismatch with allowed types, passed document type : " + documentType + ", Auto taken document type : " + DSID1Type.License.toString(), e);
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

    /**
     * Scan document
     * @param ifFront
     * @param isAutoUpload
     * @param documentType
     * @param context
     * @param documentVerifyLicenseKey
     */
    @Override
    public void scanDocument(boolean ifFront, boolean isAutoUpload, String documentType, Context context, String documentVerifyLicenseKey) {

        Log.i(TAG, "Calling Scan Document");
        DocumentHandlerImpl documentHandler = this;
        DSOptions dsOptions = getOptionsByDocumentType(ifFront, documentType);

        DSHandler dsHandler = DSHandler.getInstance(context);
        DSHandler.staticLicenseKey = documentVerifyLicenseKey;
        dsHandler.options = dsOptions;

        dsHandler.init(DSCaptureMode.Manual, new DSHandlerListener() {
            @Override
            public void handleScan(DSResult dsResult) {

                Log.i(TAG, "Document scanned successfully");
                documentHandler.dsResult = dsResult;

                if(isAutoUpload)
                    uploadAttachment(dsResult, documentHandler.instnttxnid, ifFront);

                documentHandler.callbackHandler.successCallBack(null, "Document scanned successfully", CallbackType.SUCCESS_DOC_SCAN);
            }

            @Override
            public void scanWasCancelled() {

                Log.w(TAG, "Scan document was cancelled");
                documentHandler.callbackHandler.errorCallBack("Please approve scan", CallbackType.ERROR_DOC_SCAN_CANCELLED);
            }

            @Override
            public void captureError(DSError dsError) {

                Log.e(TAG, "Scan document has return with error : " + dsError.message);
                documentHandler.callbackHandler.errorCallBack(dsError.message, CallbackType.ERROR_DOC_SCAN_NOT_CAPTURED);
            }
        });

        dsHandler.start();
    }

    /**
     * Upload attachment
     * @param ifFront
     */
    @Override
    public void uploadAttachment(boolean ifFront) {

        uploadAttachment(this.dsResult, this.instnttxnid, ifFront);
    }

    /**
     * Verify documents
     * @param documentType
     */
    @Override
    public void verifyDocuments(String documentType) {

        Log.i(TAG, "Calling verify documents");
        networkModule.verifyDocuments(documentType, this.formKey, this.instnttxnid).subscribe(response->{

            Log.i(TAG, "Verify documents called successfully");
        }, throwable -> {
            Log.e(TAG, "Verify documents returns with error", throwable);
        });
    }

    /**
     * Set form key
     * @param formKey
     */
    @Override
    public void setFormKey(String formKey) {
        Log.i(TAG, "Set form key");
        this.formKey = formKey;
    }

    /**
     * Set instnt transaction id
     * @param instnttxnid
     */
    @Override
    public void setInstnttxnid(String instnttxnid) {
        Log.i(TAG, "Set instnttxnid");
        this.instnttxnid = instnttxnid;
    }

    /**
     * Set callback handler
     * @param callbackHandler
     */
    @Override
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        Log.i(TAG, "Set callbackHandler");
        this.callbackHandler = callbackHandler;
    }
}
