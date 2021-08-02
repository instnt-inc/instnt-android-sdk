package com.instnt.instntsdk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FormCodes implements Serializable {

    @SerializedName("form_key_id")
    private String id;

    @SerializedName("form_title")
    private String title;

    @SerializedName("form_sub_title")
    private String subtitle;

    @SerializedName("form_json_rows")
    private List<FormRow> rows;

    @SerializedName("submit_btn_label")
    private String submitBtnLabel;

    @SerializedName("backend_service_url")
    private String backendServiceURL;

    @SerializedName("signed_submit_form_url")
    private String submitURL;

    @SerializedName("css")
    private String css;

    @SerializedName("field_ids")
    private String fieldIds;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("is_iframe")
    private boolean isIframe;

    @SerializedName("is_only_get_html")
    private boolean isOnlyGetHtml;

    @SerializedName("static_asset_root")
    private String hostURL;

    @SerializedName("fingerprintjs_browser_token")
    private String fingerprint;

    @SerializedName("document_verification")
    private boolean documentVerification;

    @SerializedName("hide_form_fields")
    private boolean hideFormFields;

    @SerializedName("idmetrics_version")
    private String idmetricsVersion;

    @SerializedName("clientIp")
    private String client_ip;

    @SerializedName("expiresOn")
    private long expires_on;

    @SerializedName("signature")
    private String signature;

    @SerializedName("otpVerification")
    private boolean otp_verification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<FormRow> getRows() {
        return rows;
    }

    public void setRows(List<FormRow> rows) {
        this.rows = rows;
    }

    public String getSubmitBtnLabel() {
        return submitBtnLabel;
    }

    public void setSubmitBtnLabel(String submitBtnLabel) {
        this.submitBtnLabel = submitBtnLabel;
    }

    public String getBackendServiceURL() {
        return backendServiceURL;
    }

    public void setBackendServiceURL(String backendServiceURL) {
        this.backendServiceURL = backendServiceURL;
    }

    public String getHostURL() {
        return hostURL;
    }

    public void setHostURL(String hostURL) {
        this.hostURL = hostURL;
    }

    public String getSubmitURL() {
        return submitURL;
    }

    public void setSubmitURL(String submitURL) {
        this.submitURL = submitURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(String fieldIds) {
        this.fieldIds = fieldIds;
    }

    public boolean isIframe() {
        return isIframe;
    }

    public void setIframe(boolean iframe) {
        isIframe = iframe;
    }

    public boolean isOnlyGetHtml() {
        return isOnlyGetHtml;
    }

    public void setOnlyGetHtml(boolean onlyGetHtml) {
        isOnlyGetHtml = onlyGetHtml;
    }

    public boolean isDocumentVerification() {
        return documentVerification;
    }

    public void setDocumentVerification(boolean documentVerification) {
        this.documentVerification = documentVerification;
    }

    public boolean isHideFormFields() {
        return hideFormFields;
    }

    public void setHideFormFields(boolean hideFormFields) {
        this.hideFormFields = hideFormFields;
    }

    public String getIdmetricsVersion() {
        return idmetricsVersion;
    }

    public void setIdmetricsVersion(String idmetricsVersion) {
        this.idmetricsVersion = idmetricsVersion;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public long getExpires_on() {
        return expires_on;
    }

    public void setExpires_on(long expires_on) {
        this.expires_on = expires_on;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isOtp_verification() {
        return otp_verification;
    }

    public void setOtp_verification(boolean otp_verification) {
        this.otp_verification = otp_verification;
    }
}
