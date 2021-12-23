package org.instant.accept.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.instant.accept.instntsdk.InstntSDK;
import org.instant.accept.instntsdk.data.FormField;
import org.instant.accept.instntsdk.data.FormSubmitData;
import org.instant.accept.instntsdk.interfaces.Instnt;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.utils.CommonUtils;
import org.instant.accept.instntsdk.view.BaseActivity;
import org.instant.accept.instntsdk.view.render.TextInputView;
import org.instant.accept.sample.databinding.ActivityCustomStepFormBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomStepFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityCustomStepFormBinding binding;
    private Instnt instantSDK;
    private int currentStep = 1;
    private int maxSteps = 7;
    private String formKey;

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
        } else {
            CommonUtils.showToast(this, "Form key is not found. Please go back and proceed.");
            return;
        }

        //init form fields
        initFormFields();
        instantSDK = InstntSDK.getInstance(this.formKey, serverUrl, getBaseContext());
        getFormCodes();

        binding.previous.setOnClickListener(v -> nextStep(false));
        binding.next.setOnClickListener(v -> nextStep(true));
//        binding.verifyDocument.setOnClickListener(v -> verifyDocument());

        instantSDK.setCallback(this);
    }

    private void verifyDocument() {

        this.instantSDK.scanAndUploadDocument(getBaseContext(), this.instantSDK.getTransactionID());
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

                binding.containerStep1.setVisibility(View.VISIBLE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 2: {

                binding.headText1.setText("Enter your name");
                binding.headText2.setVisibility(View.GONE);

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.VISIBLE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 3: {

                binding.headText1.setText("Enter your contact information");
                binding.headText2.setVisibility(View.GONE);

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.VISIBLE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 4: {

                binding.headText1.setText("Enter OTP");
                binding.headText2.setVisibility(View.GONE);

                sendOTP();
                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.VISIBLE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 5: {

                binding.headText1.setText("Enter your address");
                binding.headText2.setVisibility(View.GONE);

                verifyOTP();
                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.VISIBLE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 6: {

                binding.headText1.setText("Choose the document type");
                binding.headText2.setText("As an added layer of security, we need to verify your identity before approving your application");
                binding.headText2.setVisibility(View.VISIBLE);

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.VISIBLE);
                binding.containerStep7.setVisibility(View.GONE);
                break;
            }

            case 7: {

                binding.headText1.setText("Review Capture Image");
                binding.headText2.setVisibility(View.GONE);
                scanDocument();
                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                binding.containerStep5.setVisibility(View.GONE);
                binding.containerStep6.setVisibility(View.GONE);
                binding.containerStep7.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void scanDocument() {

        int MY_CAMERA_REQUEST_CODE = 100;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_CAMERA_REQUEST_CODE);
        } else {
            instantSDK.scanAndUploadDocument(getBaseContext(), instantSDK.getTransactionID());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int MY_CAMERA_REQUEST_CODE = 100;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("test1");
                instantSDK.scanAndUploadDocument(getBaseContext(), instantSDK.getTransactionID());
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                System.out.println("test2");
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendOTP() {

        //Call send otp api
        View view = binding.containerStep3.getChildAt(0);
        TextView mobile = view.findViewById(org.instant.accept.instntsdk.R.id.value);
        String mobileNumber = mobile.getText() == null ? null : mobile.getText().toString();

        //Validate number
        if (!isValidE123(mobileNumber)) {
            CommonUtils.showToast(getBaseContext(), "Please enter a valid number with country code");
            return;
        }

        this.instantSDK.sendOTP(mobileNumber, getBaseContext());
    }

    private void verifyOTP() {

        View view = binding.containerStep3.getChildAt(0);
        TextView mobile = view.findViewById(org.instant.accept.instntsdk.R.id.value);
        String mobileNumber = mobile.getText() == null ? null : mobile.getText().toString();

        View view1 = binding.containerStep4.getChildAt(1);
        TextView otpText = view1.findViewById(org.instant.accept.instntsdk.R.id.value);
        String otpCode = otpText.getText() == null ? null : otpText.getText().toString();

        this.instantSDK.verifyOTP(mobileNumber, otpCode, getBaseContext());
    }

    private static boolean isValidE123(String s) {
        Pattern p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private void initFormFields() {
        //binding.containerStep2.removeAllViews();
        //binding.containerStep3.removeAllViews();

        //first name
        FormField fNameField = new FormField();
        fNameField.setInputType("text");
        fNameField.setName("firstName");
        fNameField.setRequired(true);
        fNameField.setLabel(getString(R.string.first_name));
        fNameField.setValue("");
        fNameField.setPlaceHolder("");

        binding.containerStep2.addView(new TextInputView(this, fNameField));

        //last name
        FormField sNameField = new FormField();
        sNameField.setInputType("text");
        sNameField.setName("surName");
        sNameField.setRequired(true);
        sNameField.setLabel(getString(R.string.surname));
        sNameField.setValue("");
        sNameField.setPlaceHolder("");

        binding.containerStep2.addView(new TextInputView(this, sNameField));

        //mobile number
        FormField mobileField = new FormField();
        mobileField.setInputType("phone");
        mobileField.setName("mobileNumber");
        mobileField.setRequired(true);
        mobileField.setLabel(getString(R.string.mobile_number));
        mobileField.setValue("");
        mobileField.setPlaceHolder("");

        binding.containerStep3.addView(new TextInputView(this, mobileField));

        //email
        FormField emailField = new FormField();
        emailField.setInputType("email");
        emailField.setName("email");
        emailField.setRequired(true);
        emailField.setLabel(getString(R.string.email));
        emailField.setValue("");
        emailField.setPlaceHolder("");

        binding.containerStep3.addView(new TextInputView(this, emailField));

        FormField otpField = new FormField();
        otpField.setInputType("text");
        otpField.setName("otp");
        otpField.setRequired(true);
        otpField.setLabel("OTP Code");
        otpField.setValue("");
        otpField.setPlaceHolder("");

        binding.containerStep4.addView(new TextInputView(this, otpField));

        //-------Step 5--------//
        FormField address = new FormField();
        address.setInputType("text");
        address.setName("address");
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
        zipCode.setName("zipcode");
        zipCode.setRequired(true);
        zipCode.setLabel("Zip Code");
        zipCode.setValue("");
        zipCode.setPlaceHolder("");

        FormField country = new FormField();
        country.setInputType("text");
        country.setName("zipcode");
        country.setRequired(true);
        country.setLabel("Zip Code");
        country.setValue("");
        country.setPlaceHolder("");

        binding.containerStep5.addView(new TextInputView(this, address));
        binding.containerStep5.addView(new TextInputView(this, city));
        binding.containerStep5.addView(new TextInputView(this, state));
        binding.containerStep5.addView(new TextInputView(this, zipCode));
        binding.containerStep5.addView(new TextInputView(this, country));
        //END-------Step 5--------//
    }

    /**
     * get form data
     */
    private void getFormCodes() {

        instantSDK.setup(formKey);
    }

    /**
     * submit form
     */
    private void submit() {
//        Map<String, Object> paramMap = new HashMap<>();
//        for (int i = 0; i<binding.container.getChildCount(); i++) {
//            BaseInputView inputView = (BaseInputView)binding.container.getChildAt(i);
//
//            if (!inputView.checkValid())
//                return;
//
//            inputView.input(paramMap);
//        }
//
//        showProgressDialog(true);
//
//        instantSDK.submitForm(paramMap, this);
    }

    @Override
    public void didCancel() {
        //binding.result.setText("No JWT");
        CommonUtils.showToast(this, "User Cancelled");
    }

    @Override
    public void didSubmit(FormSubmitData data, String errMessage) {
//        showProgressDialog(false);
//
//        if (data == null) {
//            //api call is failed
//            binding.result.setText(errMessage);
//            CommonUtils.showToast(this, errMessage);
//        }else {
//            binding.result.setText(data.getJwt());
//            CommonUtils.showToast(this, data.getDecision());
//        }
    }

    private String convertObjectToString(Object obj) {
        Gson gson = new Gson();
        String result = gson.toJson(obj);
        Log.e("Super", "Result = " + result);
        return result;
    }
}