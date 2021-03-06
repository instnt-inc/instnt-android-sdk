package org.instnt.accept.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.instnt.accept.instntsdk.InstntCallbackHandler;
import org.instnt.accept.instntsdk.enums.DocumentType;
import org.instnt.accept.instntsdk.enums.ErrorCallbackType;
import org.instnt.accept.instntsdk.model.FormField;
import org.instnt.accept.instntsdk.InstntSDK;
import org.instnt.accept.instntsdk.model.FormSubmitData;
import org.instnt.accept.instntsdk.model.InstntImageData;
import org.instnt.accept.instntsdk.utils.CommonUtils;
import org.instnt.accept.sample.databinding.ActivityCustomStepFormBinding;
import org.instnt.accept.sample.view.BaseActivity;
import org.instnt.accept.sample.view.render.BaseInputView;
import org.instnt.accept.sample.view.render.TextInputView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomStepFormActivity extends BaseActivity implements InstntCallbackHandler {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String DOCUMENT_VERIFY_LICENSE_KEY = "AwG5mCdqXkmCj9oNEpGV8UauciP8s4cqFT848FfjUjwAZQJfa8ZvrEpmYsPME0RTo/Q0kRowDCGz7HPhfSdyeE7rOLtB3JAhuABdQ2R7dGhVy2EUdt5ENQBBIoveIZdf1pwVY2EUgDoGm8REDU+rr2C2";
    private boolean isAutoUpload = false;
    private boolean isIncludeDocumentUpload = true;
    private boolean isFront;
    private boolean isSelfie = false;
    private String documentType;
    private ActivityCustomStepFormBinding binding;
    private InstntSDK instantSDK;
    private int currentStep = 1;
    private int maxSteps = 9;
    private String formKey;
    private byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomStepFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {

        Bundle extras = getIntent().getExtras();
        String serverUrl;
        if (extras != null) {
            this.formKey = extras.getString("formKey");
            serverUrl = extras.getString("serverUrl");
            this.isAutoUpload = extras.getBoolean("isAutoUpload");
            this.isIncludeDocumentUpload = extras.getBoolean("isIncludeDocumentUpload");
            this.binding.scanAndUploadDocumentBtn.setVisibility(View.GONE);
            this.binding.scanAndUploadDocumentBtn.setOnClickListener(v -> processScanAndUploadDocument());
        } else {
            CommonUtils.showToast(this, "Form key is not found. Please go back and proceed.");
            return;
        }

        showProgressDialog(true);
        instantSDK = InstntSDK.init(this.formKey, serverUrl, this);
        binding.previous.setOnClickListener(v -> nextStep(false));
        binding.next.setOnClickListener(v -> validateCurrentStep(true));
        binding.signup.setOnClickListener(v -> reInitForm());
    }

    private boolean isSubmitAgain = false;

    private void processScanAndUploadDocument() {

        this.isSubmitAgain = true;
        this.currentStep = 5;
        this.isIncludeDocumentUpload = true;
        binding.previous.setVisibility(View.VISIBLE);
        binding.next.setVisibility(View.VISIBLE);
        binding.next.setEnabled(true);
        this.binding.scanAndUploadDocumentBtn.setVisibility(View.GONE);
        nextStep(true);
    }

    private void reInitForm() {

        Intent intent = new Intent(this, FormInitializationActivity.class);
        startActivity(intent);
    }

    private void validateCurrentStep(boolean isNext) {

        switch (this.currentStep) {

            case 1: {

                nextStep(isNext);
                break;
            }

            case 2: {

                nextStep(isNext);
                break;
            }

            case 3: {

                //Check if otp verification enable
                if(this.instantSDK.isOTPverificationEnabled()) {
                    showProgressDialog(true);
                    sendOTP();
                } else {
                    nextStep(isNext);
                }

                break;
            }

            case 4: {

                showProgressDialog(true);
                verifyOTP();
                break;
            }

            case 5: {

                nextStep(isNext);
                break;
            }

            case 6: {

                if(this.instantSDK.isDocumentVerificationEnabled() && this.isIncludeDocumentUpload) {
                    this.isFront = true;
                    this.documentType = "License";
                    scanDocument("License");
                } else {
                    nextStep(isNext);
                }

                break;
            }

            case 7: {

                if(this.instantSDK.isDocumentVerificationEnabled() && this.isIncludeDocumentUpload) {
                    this.isFront = false;
                    this.documentType = "License";
                    scanDocument("License");
                } else {
                    nextStep(isNext);
                }

                break;
            }

            case 8: {

                showProgressDialog(true);
                if(this.instantSDK.isDocumentVerificationEnabled() && this.isIncludeDocumentUpload) {
                    this.instantSDK.verifyDocuments("License", this.instantSDK.getInstnttxnid());
                } else {
                    this.submit();
                }
                break;
            }
        }
    }

    private void nextStep(boolean isNext) {

        if(isNext) {

            this.currentStep++;

            if(this.maxSteps == currentStep) {
                binding.next.setEnabled(false);
            }

            if(currentStep > 1)
                binding.previous.setEnabled(true);

        } else {

            this.currentStep--;

            if(currentStep < this.maxSteps) {
                binding.next.setEnabled(true);
            }

            if(currentStep == 1)
                binding.previous.setEnabled(false);

        }

        switch (this.currentStep) {

            case 1: {

                binding.headText1.setText("Instnt Signup Demo");
                binding.headText2.setVisibility(View.VISIBLE);

                binding.containerStep1Declaration.setVisibility(View.VISIBLE);
                binding.containerStep2Name.setVisibility(View.GONE);
                binding.containerStep3Contact.setVisibility(View.GONE);
                binding.containerStep4Otp.setVisibility(View.GONE);
                binding.containerStep5Address.setVisibility(View.GONE);
                binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                binding.containerStep7Review.setVisibility(View.GONE);
                binding.containerStep8Success.setVisibility(View.GONE);
                break;
            }

            case 2: {

                binding.headText1.setText("Enter your name");
                binding.headText2.setVisibility(View.GONE);

                binding.containerStep1Declaration.setVisibility(View.GONE);
                binding.containerStep2Name.setVisibility(View.VISIBLE);
                binding.containerStep3Contact.setVisibility(View.GONE);
                binding.containerStep4Otp.setVisibility(View.GONE);
                binding.containerStep5Address.setVisibility(View.GONE);
                binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                binding.containerStep7Review.setVisibility(View.GONE);
                binding.containerStep8Success.setVisibility(View.GONE);
                break;
            }

            case 3: {

                binding.headText1.setText("Enter your contact information");
                binding.headText2.setVisibility(View.GONE);

                binding.containerStep1Declaration.setVisibility(View.GONE);
                binding.containerStep2Name.setVisibility(View.GONE);
                binding.containerStep3Contact.setVisibility(View.VISIBLE);
                binding.containerStep4Otp.setVisibility(View.GONE);
                binding.containerStep5Address.setVisibility(View.GONE);
                binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                binding.containerStep7Review.setVisibility(View.GONE);
                binding.containerStep8Success.setVisibility(View.GONE);
                break;
            }

            case 4: {

                if(this.instantSDK.isOTPverificationEnabled()) {
                    binding.headText1.setText("Enter OTP");
                    binding.headText2.setVisibility(View.GONE);

                    binding.containerStep1Declaration.setVisibility(View.GONE);
                    binding.containerStep2Name.setVisibility(View.GONE);
                    binding.containerStep3Contact.setVisibility(View.GONE);
                    binding.containerStep4Otp.setVisibility(View.VISIBLE);
                    binding.containerStep5Address.setVisibility(View.GONE);
                    binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                    binding.containerStep7Review.setVisibility(View.GONE);
                    binding.containerStep8Success.setVisibility(View.GONE);
                } else {
                    binding.containerStep4Otp.removeAllViews();
                    nextStep(isNext);
                }

                break;
            }

            case 5: {

                binding.headText1.setText("Enter your address");
                binding.headText2.setVisibility(View.GONE);

                binding.containerStep1Declaration.setVisibility(View.GONE);
                binding.containerStep2Name.setVisibility(View.GONE);
                binding.containerStep3Contact.setVisibility(View.GONE);
                binding.containerStep4Otp.setVisibility(View.GONE);
                binding.containerStep5Address.setVisibility(View.VISIBLE);
                binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                binding.containerStep7Review.setVisibility(View.GONE);
                binding.containerStep8Success.setVisibility(View.GONE);
                break;
            }

            case 6: {

                if(this.instantSDK.isDocumentVerificationEnabled() && this.isIncludeDocumentUpload) {
                    binding.headText1.setText("Choose the document type");
                    binding.headText2.setText("As an added layer of security, we need to verify your identity before approving your application");
                    binding.headText2.setVisibility(View.VISIBLE);

                    binding.containerStep1Declaration.setVisibility(View.GONE);
                    binding.containerStep2Name.setVisibility(View.GONE);
                    binding.containerStep3Contact.setVisibility(View.GONE);
                    binding.containerStep4Otp.setVisibility(View.GONE);
                    binding.containerStep5Address.setVisibility(View.GONE);
                    binding.containerStep6Choosedoctype.setVisibility(View.VISIBLE);
                    binding.containerStep7Review.setVisibility(View.GONE);
                    binding.containerStep8Success.setVisibility(View.GONE);
                } else {
                    nextStep(isNext);
                }

                break;
            }

            case 7: {

                if(this.instantSDK.isDocumentVerificationEnabled() && this.isIncludeDocumentUpload) {
                    binding.headText1.setText("Review Capture Image");
                    binding.headText2.setVisibility(View.GONE);
                    binding.containerStep1Declaration.setVisibility(View.GONE);
                    binding.containerStep2Name.setVisibility(View.GONE);
                    binding.containerStep3Contact.setVisibility(View.GONE);
                    binding.containerStep4Otp.setVisibility(View.GONE);
                    binding.containerStep5Address.setVisibility(View.GONE);
                    binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                    binding.containerStep7Review.setVisibility(View.VISIBLE);
                    binding.containerStep8Success.setVisibility(View.GONE);
                } else {
                    if(isNext) {
                        this.currentStep = 8;
                        validateCurrentStep(isNext);
                    } else {
                        this.currentStep = 5;
                        nextStep(isNext);
                    }
                }

                break;
            }

            case 9: {
                binding.headText1.setVisibility(View.GONE);
                binding.headText2.setVisibility(View.GONE);
                binding.containerStep1Declaration.setVisibility(View.GONE);
                binding.containerStep2Name.setVisibility(View.GONE);
                binding.containerStep3Contact.setVisibility(View.GONE);
                binding.containerStep4Otp.setVisibility(View.GONE);
                binding.containerStep5Address.setVisibility(View.GONE);
                binding.containerStep6Choosedoctype.setVisibility(View.GONE);
                binding.containerStep7Review.setVisibility(View.GONE);
                binding.containerStep8Success.setVisibility(View.VISIBLE);
                binding.previous.setVisibility(View.GONE);
                binding.next.setVisibility(View.GONE);
                break;
            }

        }
    }

    private void scanDocument(String documentType) {
        if(!this.isAutoUpload) {
            binding.next.setEnabled(false);
            binding.uploadDocBtn.setVisibility(View.VISIBLE);
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_CAMERA_REQUEST_CODE);
        } else {
            instantSDK.scanDocument(this.isFront, this.isAutoUpload, documentType, getBaseContext(), DOCUMENT_VERIFY_LICENSE_KEY, this.instantSDK.getInstnttxnid());
        }
    }

    private void sendOTP() {

        //Call send otp api
        View view = binding.containerStep3Contact.getChildAt(0);
        TextView mobile = view.findViewById(org.instnt.accept.instntsdk.R.id.value);
        String mobileNumber = mobile.getText() == null ? null : mobile.getText().toString();

        //Validate number
        if (!isValidE123(mobileNumber)) {
            showProgressDialog(false);
            CommonUtils.showToast(getBaseContext(), "Please enter a valid number with country code");
            return;
        }

        this.instantSDK.sendOTP(mobileNumber, this.instantSDK.getInstnttxnid());
    }

    private void verifyOTP() {

        View view = binding.containerStep3Contact.getChildAt(0);
        TextView mobile = view.findViewById(org.instnt.accept.instntsdk.R.id.value);
        String mobileNumber = mobile.getText() == null ? null : mobile.getText().toString();

        View view1 = binding.containerStep4Otp.getChildAt(1);
        TextView otpText = view1.findViewById(org.instnt.accept.instntsdk.R.id.value);
        String otpCode = otpText.getText() == null ? null : otpText.getText().toString();

        this.instantSDK.verifyOTP(mobileNumber, otpCode, this.instantSDK.getInstnttxnid());
    }

    private static boolean isValidE123(String s) {
        Pattern p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private void initFormFields() {
        //binding.containerStep2.removeAllViews();
        //binding.containerStep3.removeAllViews();

        //-------Step 2--------//
        //first name
        FormField fNameField = new FormField();
        fNameField.setInputType("text");
        fNameField.setName("firstName");
        fNameField.setRequired(true);
        fNameField.setLabel(getString(R.string.first_name));
        fNameField.setValue("");
        fNameField.setPlaceHolder("");

        //last name
        FormField sNameField = new FormField();
        sNameField.setInputType("text");
        sNameField.setName("surName");
        sNameField.setRequired(true);
        sNameField.setLabel(getString(R.string.surname));
        sNameField.setValue("");
        sNameField.setPlaceHolder("");

        binding.containerStep2Name.addView(new TextInputView(this, fNameField));
        binding.containerStep2Name.addView(new TextInputView(this, sNameField));
        //END-------Step 2--------//

        //-------Step 3--------//
        //mobile number
        FormField mobileField = new FormField();
        mobileField.setInputType("phone");
        mobileField.setName("mobileNumber");
        mobileField.setRequired(true);
        mobileField.setLabel(getString(R.string.mobile_number));
        mobileField.setValue("");
        mobileField.setPlaceHolder("");

        //email
        FormField emailField = new FormField();
        emailField.setInputType("email");
        emailField.setName("email");
        emailField.setRequired(true);
        emailField.setLabel(getString(R.string.email));
        emailField.setValue("");
        emailField.setPlaceHolder("");

        binding.containerStep3Contact.addView(new TextInputView(this, mobileField));
        binding.containerStep3Contact.addView(new TextInputView(this, emailField));
        //END-------Step 3--------//

        //-------Step 4--------//
        FormField otpField = new FormField();
        otpField.setInputType("text");
        otpField.setName("otpCode");
        otpField.setRequired(true);
        otpField.setLabel("OTP Code");
        otpField.setValue("");
        otpField.setPlaceHolder("");

        binding.containerStep4Otp.addView(new TextInputView(this, otpField));
        //END-------Step 4--------//

        //-------Step 5--------//
        FormField address = new FormField();
        address.setInputType("text");
        address.setName("physicalAddress");
        address.setRequired(true);
        address.setLabel("Address");
        address.setValue("");
        address.setPlaceHolder("");

        FormField city = new FormField();
        city.setInputType("text");
        city.setName("city");
        city.setRequired(true);
        city.setLabel("City");
        city.setValue("");
        city.setPlaceHolder("");

        FormField state = new FormField();
        state.setInputType("text");
        state.setName("state");
        state.setRequired(true);
        state.setLabel("State");
        state.setValue("");
        state.setPlaceHolder("");

        FormField zipCode = new FormField();
        zipCode.setInputType("text");
        zipCode.setName("zip");
        zipCode.setRequired(true);
        zipCode.setLabel("Zip Code");
        zipCode.setValue("");
        zipCode.setPlaceHolder("");

        FormField country = new FormField();
        country.setInputType("text");
        country.setName("country");
        country.setRequired(true);
        country.setLabel("Country");
        country.setValue("");
        country.setPlaceHolder("");

        binding.containerStep5Address.addView(new TextInputView(this, address));
        binding.containerStep5Address.addView(new TextInputView(this, city));
        binding.containerStep5Address.addView(new TextInputView(this, state));
        binding.containerStep5Address.addView(new TextInputView(this, zipCode));
        binding.containerStep5Address.addView(new TextInputView(this, country));
        //END-------Step 5--------//

        binding.driverLicense.setChecked(true);
        if(this.isAutoUpload) {
            binding.uploadDocBtn.setVisibility(View.GONE);
        } else {
            binding.uploadDocBtn.setVisibility(View.VISIBLE);
            binding.uploadDocBtn.setOnClickListener(v -> {
                showProgressDialog(true);
                this.instantSDK.uploadAttachment(this.imageData, this.isFront, this.isSelfie, this.instantSDK.getInstnttxnid());
            });
        }
    }

    /**
     * submit form
     */
    private void submit() {
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i<binding.containerStep2Name.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView) binding.containerStep2Name.getChildAt(i);
            inputView.input(paramMap);
        }

        for (int i = 0; i<binding.containerStep3Contact.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView) binding.containerStep3Contact.getChildAt(i);
            inputView.input(paramMap);
        }

        for (int i = 0; i<binding.containerStep5Address.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView) binding.containerStep5Address.getChildAt(i);
            inputView.input(paramMap);
        }

        if(this.isSubmitAgain) {
            paramMap = new HashMap<>();
        }

        this.instantSDK.submitData(getBaseContext(), this.getWindowManager(), paramMap, this.instantSDK.getInstnttxnid());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                instantSDK.scanDocument(this.isFront, this.isAutoUpload, this.documentType, getBaseContext(), DOCUMENT_VERIFY_LICENSE_KEY, this.instantSDK.getInstnttxnid());
            } else {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void uploadAttachmentSuccessCallback(InstntImageData instntImageData) {
        if(!this.isAutoUpload) {
            showProgressDialog(false);
            binding.next.setEnabled(true);
            CommonUtils.showToast(this, "Document uploaded successfully");
            binding.uploadDocBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void scanDocumentSuccessCallback(InstntImageData instntImageData) {
        this.imageData = instntImageData.getImageData();
        Bitmap bm = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        binding.imageData.setMinimumHeight(dm.heightPixels);
        binding.imageData.setMinimumWidth(dm.widthPixels);
        binding.imageData.setImageBitmap(bm);
        binding.imageData.setVisibility(View.VISIBLE);
        showProgressDialog(false);
        nextStep(true);
    }

    @Override
    public void submitDataSuccessCallback(FormSubmitData formSubmitData) {
        showProgressDialog(false);
        binding.decision.setText("Decision : " + formSubmitData.getDecision());
        binding.jwtToken.setText(formSubmitData.getJwt());

        if(this.isIncludeDocumentUpload) {
            this.binding.scanAndUploadDocumentBtn.setVisibility(View.GONE);
        } else {
            this.binding.scanAndUploadDocumentBtn.setVisibility(View.VISIBLE);
        }
        nextStep(true);
    }

    @Override
    public void initTransactionSuccessCallback(String instnttxnid) {
        showProgressDialog(false);
        //init form fields
        initFormFields();
    }

    @Override
    public void sendOTPSuccessCallback(String message) {
        showProgressDialog(false);
        nextStep(true);
    }

    @Override
    public void verifyOTPSuccessCallback(String message) {
        showProgressDialog(false);
        nextStep(true);
    }

    @Override
    public void verifyDocumentsInitiationCallback(String message) {
        this.submit();
    }

    @Override
    public void instntErrorCallback(String message, ErrorCallbackType errorCallbackType) {
        switch (errorCallbackType) {
            case SUBMIT_FORM_ERROR:
            case SEND_OTP_ERROR:
            case VERIFY_OTP_ERROR:
            case SCAN_DOCUMENT_CANCELLED_ERROR:
            case SCAN_DOCUMENT_CAPTURE_ERROR: {
                showProgressDialog(false);
                CommonUtils.showToast(this, message);
                break;
            }
            case INIT_TRANSACTION_ERROR: {
                CommonUtils.showToast(this, message);
                Intent intent = new Intent(this, FormInitializationActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}