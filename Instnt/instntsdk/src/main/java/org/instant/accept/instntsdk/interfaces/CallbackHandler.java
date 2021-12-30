package org.instant.accept.instntsdk.interfaces;

import org.instant.accept.instntsdk.model.FormSubmitData;

public interface CallbackHandler {
    void successCallBack(byte[] imageData);
    void errorCallBack();
}
