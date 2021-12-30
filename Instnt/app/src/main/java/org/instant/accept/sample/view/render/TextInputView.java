package org.instant.accept.sample.view.render;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.instant.accept.instntsdk.R;
import org.instant.accept.instntsdk.model.FormField;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextInputView extends BaseInputView {

    private EditText valueView;

    public TextInputView(Context context, FormField formField) {
        super(context, formField);
    }

    @Override
    void setupView() {
        View view = inflate(getContext(), R.layout.child_text, this);

        TextView label = view.findViewById(R.id.label);
        valueView = view.findViewById(R.id.value);
        label.setText(formField.isRequired()? generateAsterisk(formField.getLabel()) : formField.getLabel());
        valueView.setHint(formField.getPlaceHolder());

        switch (formField.getInputType()) {
            case "number":
                valueView.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case "phone":
                valueView.setRawInputType(InputType.TYPE_CLASS_PHONE);
                break;

            case "email":
                valueView.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

            default:
                valueView.setRawInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    @Override
    public void input(Map<String, Object> map) {
        map.put(formField.getName(), valueView.getText().toString().trim());
    }

    @Override
    public boolean checkValid() {

        if (!formField.isRequired())
            return true;

        if (TextUtils.isEmpty(valueView.getText().toString().trim())) {
            valueView.setError("Please fill out this field");
            valueView.requestFocus();
            return false;
        }

        if (formField.getInputType().equals("email") && !isEmailValid(valueView.getText().toString().trim())) {
            valueView.setError("Please fill out with valid email");
            valueView.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
