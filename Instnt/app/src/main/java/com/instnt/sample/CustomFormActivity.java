package com.instnt.sample;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.instnt.instntsdk.InstntSDK;
import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.data.FormField;
import com.instnt.instntsdk.data.FormSubmitData;
import com.instnt.instntsdk.interfaces.GetFormCallback;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.utils.CommonUtils;
import com.instnt.instntsdk.view.BaseActivity;
import com.instnt.instntsdk.view.render.BaseInputView;
import com.instnt.instntsdk.view.render.TextInputView;
import com.instnt.sample.databinding.ActivityCustomFormBinding;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityCustomFormBinding binding;
    private InstntSDK instantSDK;
    private FormCodes formCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        //init form fields
        initFormFields();

        instantSDK = InstntSDK.getInstance();
        binding.formid.setText("v876130100000");
        binding.getFormCode.setOnClickListener(v -> {
            getFormCodes();
        });

        binding.submit.setOnClickListener(v -> submit());

        instantSDK.setCallback(this);
    }

    private void initFormFields() {
        binding.container.removeAllViews();

        FormField fNameField = new FormField();
        fNameField.setInputType("text");
        fNameField.setName("firstName");
        fNameField.setRequired(true);
        fNameField.setLabel(getString(R.string.first_name));
        fNameField.setValue("");
        fNameField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, fNameField));

        FormField sNameField = new FormField();
        sNameField.setInputType("text");
        sNameField.setName("surName");
        sNameField.setRequired(true);
        sNameField.setLabel(getString(R.string.surname));
        sNameField.setValue("");
        sNameField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, sNameField));

        FormField emailField = new FormField();
        emailField.setInputType("email");
        emailField.setName("email");
        emailField.setRequired(true);
        emailField.setLabel(getString(R.string.email));
        emailField.setValue("");
        emailField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, emailField));
    }

    /**
     * get form data
     */
    private void getFormCodes() {
        String formId = binding.formid.getText().toString().trim();

        showProgressDialog(true);
        instantSDK.setup(formId, binding.sandboxSwitch.isChecked());
        instantSDK.getFormData( new GetFormCallback() {
            @Override
            public void onResult(boolean success, FormCodes codes, String message) {
                showProgressDialog(false);

                if (!success) {
                    binding.submit.setEnabled(false);
                    DialogUtils.showAlertDialog(CustomFormActivity.this, "Failed", message, "Ok", false);
                }else {
                    binding.submit.setEnabled(true);
                    formCodes = codes;
                    binding.result.setText(convertObjectToString(codes));
                }
            }
        });
    }

    /**
     * submit form
     */
    private void submit() {
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i<binding.container.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView)binding.container.getChildAt(i);

            if (!inputView.checkValid())
                return;

            inputView.input(paramMap);
        }

        paramMap.put("form_key", formCodes.getId());

        Map <String, Object> fingerMap = new HashMap<>();
        fingerMap.put("requestId", formCodes.getFingerprint());
        fingerMap.put("visitorId", formCodes.getFingerprint());
        fingerMap.put("visitorFound", true);

        paramMap.put("fingerprint", fingerMap);
        paramMap.put("client_referer_url", formCodes.getBackendServiceURL());

        try {
            paramMap.put("client_referer_host", new URL(formCodes.getBackendServiceURL()).getHost());
        } catch (MalformedURLException e) {
            paramMap.put("client_referer_host", "");
        }

        showProgressDialog(true);

        instantSDK.submitForm(formCodes.getSubmitURL(), paramMap, this);
    }

    @Override
    public void didCancel() {
        binding.result.setText("No JWT");
        CommonUtils.showToast(this, "User Cancelled");
    }

    @Override
    public void didSubmit(FormSubmitData data, String errMessage) {
        showProgressDialog(false);

        if (data == null) {
            //api call is failed
            binding.result.setText(errMessage);
            CommonUtils.showToast(this, errMessage);
        }else {
            binding.result.setText(data.getJwt());
            CommonUtils.showToast(this, data.getDecision());
        }
    }

    private String convertObjectToString(Object obj) {
        Gson gson = new Gson();
        String result = gson.toJson(obj);
        Log.e("Super", "Result = " + result);
        return result;
    }
}