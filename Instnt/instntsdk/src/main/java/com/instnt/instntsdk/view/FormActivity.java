package com.instnt.instntsdk.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.instnt.instntsdk.InstntSDK;
import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.data.FormField;
import com.instnt.instntsdk.data.FormRow;
import com.instnt.instntsdk.data.FormSubmitData;
import com.instnt.instntsdk.databinding.ActivityFormBinding;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.view.render.BaseInputView;
import com.instnt.instntsdk.view.render.CheckboxInputView;
import com.instnt.instntsdk.view.render.DatePickerInputView;
import com.instnt.instntsdk.view.render.SpinnerInputView;
import com.instnt.instntsdk.view.render.TextInputView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends BaseActivity {

    public static final String FORM_CORDS = "form_codes";

    private ActivityFormBinding binding;
    private FormCodes formCodes;
    private SubmitCallback submitCallback;
    private InstntSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        formCodes = (FormCodes)getIntent().getSerializableExtra(FORM_CORDS);

        sdk = InstntSDK.getInstance();
        submitCallback = sdk.getSubmitCallback();

        init();
    }

    private void init() {
        binding.next.setText(formCodes.getSubmitBtnLabel());
        binding.title.setText(formCodes.getTitle());

        for (FormRow row : formCodes.getRows()) {
            addView(row);
        }

        binding.next.setOnClickListener(v -> submit());
        binding.cancel.setOnClickListener(v -> cancel());
    }

    private void submit() {
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i<binding.container.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView)binding.container.getChildAt(i);

            if (!inputView.checkValid())
                return;

            inputView.input(paramMap);
        }

        showProgressDialog(true);

        sdk.submitForm(paramMap, new SubmitCallback() {
            @Override
            public void didCancel() {

            }

            @Override
            public void didSubmit(FormSubmitData submitData, String errMsg) {
                showProgressDialog(false);

                if (submitCallback != null)
                    submitCallback.didSubmit(submitData, errMsg);

                if (submitData != null) {
                    //api is success
                    finish();
                }
            }
        });
    }

    private void cancel() {
        if (submitCallback != null) {
            submitCallback.didCancel();
        }
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    private void addView(FormRow rowData) {

        if (rowData == null || rowData.getColumns().isEmpty())
            return;

        FormField field = rowData.getColumns().get(0).getField();

        if (isSelect(field.getInputType())) {
            //spinner
            binding.container.addView(new SpinnerInputView(this, field));
        }else if (isCheckbox(field.getInputType())) {
            //checkbox
            binding.container.addView(new CheckboxInputView(this, field));
        }else if (isDatePicker(field.getInputType())) {
            //date picker
            binding.container.addView(new DatePickerInputView(this, field));
        }else {
            //text
            binding.container.addView(new TextInputView(this, field));
        }
    }

    private boolean isSelect(String inputTYpe) {
        return inputTYpe.equals("select");
    }

    private boolean isCheckbox(String inputTYpe) {
        return inputTYpe.equals("checkbox");
    }

    private boolean isDatePicker(String inputTYpe) {
        return inputTYpe.equals("date");
    }
}