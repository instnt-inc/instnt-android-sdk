package org.instnt.instntsdk.data;

import java.io.Serializable;

public class FormSubmitResponse implements Serializable {
    private FormSubmitData data;

    public FormSubmitData getData() {
        return data;
    }

    public void setData(FormSubmitData data) {
        this.data = data;
    }
}
