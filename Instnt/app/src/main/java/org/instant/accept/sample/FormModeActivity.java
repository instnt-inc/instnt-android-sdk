package org.instant.accept.sample;


import android.content.Intent;
import android.os.Bundle;

import org.instant.accept.instntsdk.view.BaseActivity;
import org.instant.accept.sample.databinding.ActivityFormModeBinding;

public class FormModeActivity extends BaseActivity {

    ActivityFormModeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.defaultForm.setOnClickListener(v -> goDefault());
        binding.customForm.setOnClickListener(v -> goCustom());
        binding.customStepForm.setOnClickListener(v -> goCustomStepForm());
    }

    private void goDefault() {
        Intent intent = new Intent(this, DefaultFormActivity.class);
        startActivity(intent);
    }

    private void goCustom() {
        Intent intent = new Intent(this, CustomFormActivity.class);
        startActivity(intent);
    }

    private void goCustomStepForm() {
        Intent intent = new Intent(this, CustomStepFormActivity.class);
        startActivity(intent);
    }
}