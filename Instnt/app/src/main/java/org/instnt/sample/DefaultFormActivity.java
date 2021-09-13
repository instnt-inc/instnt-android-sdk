package org.instnt.sample;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import org.instnt.instntsdk.InstntSDK;
import org.instnt.instntsdk.data.FormSubmitData;
import org.instnt.instntsdk.interfaces.SubmitCallback;
import org.instnt.instntsdk.utils.CommonUtils;
import org.instnt.instntsdk.view.BaseActivity;
import org.instnt.sample.databinding.ActivityDefaultFormBinding;

public class DefaultFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityDefaultFormBinding binding;
    private InstntSDK instantSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDefaultFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        instantSDK = InstntSDK.getInstance();
        binding.formid.setText("v876130100000");
        binding.show.setOnClickListener(v -> {
            show();
        });

        instantSDK.setCallback(this);
    }

    private void show() {
        String formId = binding.formid.getText().toString().trim();

        if (TextUtils.isEmpty(formId)) {
            CommonUtils.showToast(this, "Empty Form Id!");
            return;
        }

        instantSDK.setup(formId, binding.sandboxSwitch.isChecked());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showForm();
            }
        }, 1500);
    }

    private void showForm() {
        instantSDK.showForm(this, this);
    }

    @Override
    public void didCancel() {
        binding.result.setText("No JWT");
        CommonUtils.showToast(this, "User Cancelled");
    }

    @Override
    public void didSubmit(FormSubmitData data, String errMessage) {
        if (data == null) {
            //api call is failed
            binding.result.setText("No JWT");
            CommonUtils.showToast(this, errMessage);
        }else {
            binding.result.setText(data.getJwt());
            CommonUtils.showToast(this, data.getDecision());
        }
    }
}