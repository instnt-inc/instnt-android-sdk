package com.instnt.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.instnt.instntsdk.InstntSDK;
import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.data.FormSubmitData;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.utils.CommonUtils;
import com.instnt.instntsdk.view.BaseActivity;
import com.instnt.sample.databinding.ActivityDefaultFormBinding;

public class DefaultFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityDefaultFormBinding binding;
    private InstntSDK instantSDK;

    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDefaultFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int MY_CAMERA_REQUEST_CODE = 100;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("test1");
                instantSDK.startAuthentication(getBaseContext());
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                System.out.println("test2");
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void init() {
        instantSDK = InstntSDK.getInstance();
        binding.formid.setText("v876130100000");
        binding.show.setOnClickListener(v -> {
            show();
        });
        int MY_CAMERA_REQUEST_CODE = 100;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_CAMERA_REQUEST_CODE);
        } else {
            instantSDK.startAuthentication(getBaseContext());
        }

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
