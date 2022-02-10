package org.instnt.accept.instntsdk.implementations;

import org.instnt.accept.instntsdk.interfaces.DeviceHandler;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeviceHandlerImpl implements DeviceHandler {

    private static final String TAG = "DeviceHandlerImpl";

    /**
     * Get device info
     * @param context
     * @param windowManager
     * @return
     */
    @Override
    public Map<String, String> getDeviceInfo(Context context, WindowManager windowManager) {

        Log.i(TAG, "Calling get device info");

        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        DecimalFormat df2 = new DecimalFormat("#.##");
        String rounded = df2.format(screenInches);
        int densityDpi = (int) (dm.density * 160f);

        String ScreenResolution_value = String.valueOf(dm.heightPixels) + " * " + String.valueOf(dm.widthPixels) + " Pixels";
        String screen_size = rounded + " Inches";
        String screen_density = String.valueOf(densityDpi) + " dpi";
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
        String _HARDWARE = android.os.Build.HARDWARE;
        String _MANUFACTURER = android.os.Build.MANUFACTURER;
        String _USER = android.os.Build.USER;
        String _HOST = android.os.Build.HOST;

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("device_id", android_id);
        map.put("screen_resolution_value", ScreenResolution_value);
        map.put("screen_size", screen_size);
        map.put("screen_density", screen_density);
        map.put("user_value", User_value);
        map.put("host_value", Host_value);
        map.put("version", Version);
        map.put("api_level", API_level);
        map.put("build_id", Build_ID);
        map.put("build_time", Build_Time);
        map.put("fingerprint", Fingerprint);
        map.put("osversion", _OSVERSION);
        map.put("release", _RELEASE);
        map.put("device", _DEVICE);
        map.put("model", _MODEL);
        map.put("product", _PRODUCT);
        map.put("brand", _BRAND);
        map.put("display", _DISPLAY);
        map.put("cpu_abi", _CPU_ABI);
        map.put("hardware", _HARDWARE);
        map.put("manufacturer", _MANUFACTURER);
        map.put("user", _USER);
        map.put("host", _HOST);
        return map;
    }
}
