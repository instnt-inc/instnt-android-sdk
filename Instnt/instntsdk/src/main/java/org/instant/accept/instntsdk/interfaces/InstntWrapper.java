package org.instant.accept.instntsdk.interfaces;

import com.idmetrics.dc.utils.DSResult;

import org.instant.accept.instntsdk.data.FormSubmitData;

public interface InstntWrapper {
    //first api call api method
    public void initTransaction();
    //Upload document api method
    public void documentUpload(DSResult dsResult);
}
