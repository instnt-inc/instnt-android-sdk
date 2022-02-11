package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.enums.CallbackType;

public interface CallbackHandler {
    void successCallBack(CallbackData data, String message, CallbackType callbackType);
    void errorCallBack(String message, CallbackType callbackType);
}
