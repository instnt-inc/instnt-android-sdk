package org.instnt.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import org.instnt.instntsdk.InstntSDK;
import org.instnt.instntsdk.data.FormField;
import org.instnt.instntsdk.data.FormSubmitData;
import org.instnt.instntsdk.interfaces.SubmitCallback;
import org.instnt.instntsdk.utils.CommonUtils;
import org.instnt.instntsdk.view.BaseActivity;
import org.instnt.instntsdk.view.render.BaseInputView;
import org.instnt.instntsdk.view.render.TextInputView;
import org.instnt.sample.databinding.ActivityCustomFormBinding;

import java.util.HashMap;
import java.util.Map;

public class CustomFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityCustomFormBinding binding;
    private InstntSDK instantSDK;

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

        //first name
        FormField fNameField = new FormField();
        fNameField.setInputType("text");
        fNameField.setName("firstName");
        fNameField.setRequired(true);
        fNameField.setLabel(getString(R.string.first_name));
        fNameField.setValue("");
        fNameField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, fNameField));

        //last name
        FormField sNameField = new FormField();
        sNameField.setInputType("text");
        sNameField.setName("surName");
        sNameField.setRequired(true);
        sNameField.setLabel(getString(R.string.surname));
        sNameField.setValue("");
        sNameField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, sNameField));

        //email
        FormField emailField = new FormField();
        emailField.setInputType("email");
        emailField.setName("email");
        emailField.setRequired(true);
        emailField.setLabel(getString(R.string.email));
        emailField.setValue("");
        emailField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, emailField));

        //mobile number
        FormField mobileField = new FormField();
        mobileField.setInputType("phone");
        mobileField.setName("mobileNumber");
        mobileField.setRequired(true);
        mobileField.setLabel(getString(R.string.mobile_number));
        mobileField.setValue("");
        mobileField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, mobileField));

        //address
        FormField addressField = new FormField();
        addressField.setInputType("text");
        addressField.setName("physicalAddress");
        addressField.setRequired(true);
        addressField.setLabel(getString(R.string.address));
        addressField.setValue("");
        addressField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, addressField));

        //zip
        FormField zipField = new FormField();
        zipField.setInputType("text");
        zipField.setName("zip");
        zipField.setRequired(true);
        zipField.setLabel(getString(R.string.zip_code));
        zipField.setValue("");
        zipField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, zipField));

        //city
        FormField cityField = new FormField();
        cityField.setInputType("text");
        cityField.setName("city");
        cityField.setRequired(true);
        cityField.setLabel(getString(R.string.city));
        cityField.setValue("");
        cityField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, cityField));

        //state
        FormField stateField = new FormField();
        stateField.setInputType("text");
        stateField.setName("state");
        stateField.setRequired(true);
        stateField.setLabel(getString(R.string.state));
        stateField.setValue("");
        stateField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, stateField));

        //country
        FormField countryField = new FormField();
        countryField.setInputType("text");
        countryField.setName("country");
        countryField.setRequired(true);
        countryField.setLabel(getString(R.string.country));
        countryField.setValue("");
        countryField.setPlaceHolder("");

        binding.container.addView(new TextInputView(this, countryField));
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
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i<binding.container.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView)binding.container.getChildAt(i);

            if (!inputView.checkValid())
                return;

            inputView.input(paramMap);
        }

        showProgressDialog(true);

        instantSDK.submitForm(paramMap, this);
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