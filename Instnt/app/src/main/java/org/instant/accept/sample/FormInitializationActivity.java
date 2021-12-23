package org.instant.accept.sample;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import org.instant.accept.instntsdk.utils.CommonUtils;
import org.instant.accept.instntsdk.view.BaseActivity;
import org.instant.accept.sample.databinding.ActivityFormInitializationBinding;

public class FormInitializationActivity extends BaseActivity {

    ActivityFormInitializationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormInitializationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.show.setOnClickListener(v -> showForm());
        binding.formid.setText("v1633477069641729");
        binding.serverUrl.setText("https://dev2-api.instnt.org/public/");
        binding.validServerUrls.setText("https://dev2-api.instnt.org/public/ \nhttps://sandbox-api.instnt.org/public/ \nhttps://api.instnt.org/public/");
    }

    private void showForm() {

        String formKey = binding.formid.getText().toString();
        String serverUrl = binding.serverUrl.getText().toString();

        if(formKey.trim().length() < 1) {
            CommonUtils.showToast(this, "Please enter valid formkey");
            return;
        }

        if(!serverUrl.endsWith("public/")) {
            CommonUtils.showToast(this, "Invalid URL! Sample valid URL is : https://sandbox-api.instnt.org/public/");
            return;
        }

        Intent intent = new Intent(this, CustomStepFormActivity.class);
        intent.putExtra("formKey", formKey);
        intent.putExtra("serverUrl", serverUrl);
        startActivity(intent);
    }
}