package org.instant.accept.instntsdk.authentication;

import com.idmetrics.dc.utils.DSResult;

import org.instant.accept.instntsdk.interfaces.InstntWrapper;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.util.Map;

public class InstntWrapperImpl implements InstntWrapper {

    private NetworkUtil networkModule;

    public InstntWrapperImpl() {
        this.networkModule = new NetworkUtil();
    }

    @Override
    public void initTransaction() {

    }

    @Override
    public void documentUpload(DSResult dsResult) {

        String instnttxnid = "51dc77ad-c65e-4f68-8296-8396bd207a84";
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

        networkModule.uploadDocument(presignedS3Url, imageData, isSandbox).subscribe(response2->{

            System.out.println("Response2");
        }, throwable -> {
            //CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            System.out.println(CommonUtils.getErrorMessage(throwable));
        });
    }

}
