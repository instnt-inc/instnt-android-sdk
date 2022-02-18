package org.instnt.accept.instntsdk.interfaces;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.model.FormCodes;

import java.util.Map;

public interface FormHandler {
    void submitData(Map<String, Object> body);

    void setCallbackHandler(InstntCallbackHandler instntCallbackHandler);
    void setInstnttxnid(String instnttxnid);
    void setWorkFlowDetail(FormCodes formCodes);
}
