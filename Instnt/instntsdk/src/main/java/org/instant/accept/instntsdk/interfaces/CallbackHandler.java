package org.instant.accept.instntsdk.interfaces;

import org.instant.accept.instntsdk.enums.CallbackType;
import org.instant.accept.instntsdk.model.FormSubmitData;

public interface CallbackHandler {
    void successCallBack(Object data, String message, CallbackType callbackType);
    void errorCallBack(String message, CallbackType callbackType);
}
