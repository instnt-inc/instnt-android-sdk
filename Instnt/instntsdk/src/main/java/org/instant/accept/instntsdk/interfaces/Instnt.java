package org.instant.accept.instntsdk.interfaces;

import com.idmetrics.dc.utils.DSResult;

import org.instant.accept.instntsdk.data.FormSubmitData;

public interface Instnt extends DocumentHandler, OTPHandler, FormHandler {
    String getTransactionID();
}
