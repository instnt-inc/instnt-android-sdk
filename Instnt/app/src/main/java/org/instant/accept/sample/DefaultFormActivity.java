package org.instant.accept.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.instant.accept.instntsdk.InstntSDK;
import org.instant.accept.instntsdk.data.FormSubmitData;
import org.instant.accept.instntsdk.interfaces.Instnt;
import org.instant.accept.instntsdk.interfaces.SubmitCallback;
import org.instant.accept.instntsdk.network.RestUrl;
import org.instant.accept.instntsdk.utils.CommonUtils;
import org.instant.accept.instntsdk.view.BaseActivity;
import org.instant.accept.sample.databinding.ActivityDefaultFormBinding;

public class DefaultFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityDefaultFormBinding binding;
    private Instnt instantSDK;

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
                instantSDK.scanAndUploadDocument(getBaseContext(), instantSDK.getTransactionID());
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                System.out.println("test2");
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void init() {
        instantSDK = InstntSDK.getInstance("v876130100000", RestUrl.SANDBOX_URL, getBaseContext());
        binding.formid.setText("v876130100000");
        binding.show.setOnClickListener(v -> {
            show();
        });
        int MY_CAMERA_REQUEST_CODE = 100;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_CAMERA_REQUEST_CODE);
        } else {
            instantSDK.scanAndUploadDocument(getBaseContext(), instantSDK.getTransactionID());
        }

        instantSDK.setCallback(this);
    }

    private void show() {
        String formId = binding.formid.getText().toString().trim();

        if (TextUtils.isEmpty(formId)) {
            CommonUtils.showToast(this, "Empty Form Id!");
            return;
        }

        instantSDK.setup(formId);

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
