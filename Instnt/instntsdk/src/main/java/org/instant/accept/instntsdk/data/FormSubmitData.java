package org.instant.accept.instntsdk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormSubmitData implements Serializable {
    private String status;
    private String url;
    private boolean success;

    @SerializedName("instntjwt")
    private String jwt;

    private String decision;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
