package org.instant.accept.instntsdk.data;

import java.io.Serializable;

public class OTPVerificationResult implements Serializable {

    private String id;

    private String[] errors;

    private boolean valid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
