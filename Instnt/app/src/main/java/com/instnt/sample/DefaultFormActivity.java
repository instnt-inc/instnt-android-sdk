package com.instnt.sample;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.instnt.instntsdk.InstntSDK;
import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.data.FormSubmitData;
import com.instnt.instntsdk.interfaces.SubmitCallback;
import com.instnt.instntsdk.utils.CommonUtils;
import com.instnt.instntsdk.view.BaseActivity;
import com.instnt.sample.databinding.ActivityDefaultFormBinding;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultFormActivity extends BaseActivity implements SubmitCallback {

    private ActivityDefaultFormBinding binding;
    private InstntSDK instantSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, String> deviceInfoMap = getDeviceInfo();

        for (Map.Entry<String, String> entry : deviceInfoMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        binding = ActivityDefaultFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private Map<String, String> getDeviceInfo() {

        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        DecimalFormat df2 = new DecimalFormat("#.##");
        String rounded = df2.format(screenInches);
        int densityDpi = (int) (dm.density * 160f);

        String ScreenResolution_value = String.valueOf(dm.heightPixels) + " * " + String.valueOf(dm.widthPixels) + " Pixels";
        String screen_size = rounded + " Inches";
        String screen_density = String.valueOf(densityDpi) + " dpi";
        String BootLoader_value = Build.BOOTLOADER;
        String User_value = Build.USER;
        String Host_value = Build.HOST;
        String Version = Build.VERSION.RELEASE;
        String API_level = Build.VERSION.SDK_INT + "";
        String Build_ID = Build.ID;
        String Build_Time = Build.TIME + "";
        String Fingerprint = Build.FINGERPRINT;
        String _OSVERSION = System.getProperty("os.version");
        String _RELEASE = android.os.Build.VERSION.RELEASE;
        String _DEVICE = android.os.Build.DEVICE;
        String _MODEL = android.os.Build.MODEL;
        String _PRODUCT = android.os.Build.PRODUCT;
        String _BRAND = android.os.Build.BRAND;
        String _DISPLAY = android.os.Build.DISPLAY;
        String _CPU_ABI = android.os.Build.CPU_ABI;
        String _CPU_ABI2 = android.os.Build.CPU_ABI2;
        String _UNKNOWN = android.os.Build.UNKNOWN;
        String _HARDWARE = android.os.Build.HARDWARE;
        String _ID = android.os.Build.ID;
        String _MANUFACTURER = android.os.Build.MANUFACTURER;
        String _SERIAL = android.os.Build.SERIAL;
        String _USER = android.os.Build.USER;
        String _HOST = android.os.Build.HOST;

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("android_id", android_id);
        map.put("ScreenResolution_value", ScreenResolution_value);
        map.put("screen_size", screen_size);
        map.put("screen_density", screen_density);
        map.put("BootLoader_value", BootLoader_value);
        map.put("User_value", User_value);
        map.put("Host_value", Host_value);
        map.put("Version", Version);
        map.put("API_level", API_level);
        map.put("Build_ID", Build_ID);
        map.put("Build_Time", Build_Time);
        map.put("Fingerprint", Fingerprint);
        map.put("_OSVERSION", _OSVERSION);
        map.put("_RELEASE", _RELEASE);
        map.put("_DEVICE", _DEVICE);
        map.put("_MODEL", _MODEL);
        map.put("_PRODUCT", _PRODUCT);
        map.put("_BRAND", _BRAND);
        map.put("_DISPLAY", _DISPLAY);
        map.put("_CPU_ABI", _CPU_ABI);
        map.put("_CPU_ABI2", _CPU_ABI2);
        map.put("_UNKNOWN", _UNKNOWN);
        map.put("_HARDWARE", _HARDWARE);
        map.put("_ID", _ID);
        map.put("_MANUFACTURER", _MANUFACTURER);
        map.put("_SERIAL", _SERIAL);
        map.put("_USER", _USER);
        map.put("_HOST", _HOST);
        return map;
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