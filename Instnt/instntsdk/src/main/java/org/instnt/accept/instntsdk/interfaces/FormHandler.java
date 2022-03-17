package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.model.FormCodes;

import java.util.Map;

public interface FormHandler {
    void submitData(Map<String, Object> body, String instnttxnid);

    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
    void setWorkFlowDetail(FormCodes formCodes);
    void setServerUrl(String serverUrl);
}
