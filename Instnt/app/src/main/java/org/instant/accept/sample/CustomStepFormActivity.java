package org.instant.accept.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.instant.accept.instntsdk.InstntSDK;
import org.instant.accept.instntsdk.data.FormField;
import org.instant.accept.instntsdk.data.FormSubmitData;
import org.instant.accept.instntsdk.interfaces.Instnt;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.utils.CommonUtils;
import org.instant.accept.instntsdk.view.BaseActivity;
import org.instant.accept.instntsdk.view.render.BaseInputView;
import org.instant.accept.instntsdk.view.render.TextInputView;
import org.instant.accept.sample.databinding.ActivityCustomStepFormBinding;

import java.util.HashMap;
import java.util.Map;

public class CustomStepFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityCustomStepFormBinding binding;
    private Instnt instantSDK;
    private int currentStep = 1;
    private int maxSteps = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomStepFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        //init form fields
        initFormFields();

        instantSDK = InstntSDK.getInstance();
        binding.formid.setText("v1633477069641729");
        binding.getFormCode.setOnClickListener(v -> {
            getFormCodes();
        });

        binding.submit.setOnClickListener(v -> submit());
        binding.previous.setOnClickListener(v -> nextStep(false));
        binding.next.setOnClickListener(v -> nextStep(true));
        binding.verifyDocument.setOnClickListener(v -> verifyDocument());

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

                binding.containerStep1.setVisibility(View.VISIBLE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                break;
            }

            case 2: {

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.VISIBLE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.GONE);
                break;
            }

            case 3: {

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.VISIBLE);
                binding.containerStep4.setVisibility(View.GONE);
                break;
            }

            case 4: {

                binding.containerStep1.setVisibility(View.GONE);
                binding.containerStep2.setVisibility(View.GONE);
                binding.containerStep3.setVisibility(View.GONE);
                binding.containerStep4.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void initFormFields() {
//        binding.containerStep2.removeAllViews();
//        binding.containerStep3.removeAllViews();

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

    }

    /**
     * get form data
     */
    private void getFormCodes() {
        String formId = binding.formid.getText().toString().trim();

        if (TextUtils.isEmpty(formId)) {
            CommonUtils.showToast(this, "Empty Form Id!");
            return;
        }

        instantSDK.setup(formId, binding.sandboxSwitch.isChecked());
        binding.submit.setEnabled(true);
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
        binding.result.setText("No JWT");
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