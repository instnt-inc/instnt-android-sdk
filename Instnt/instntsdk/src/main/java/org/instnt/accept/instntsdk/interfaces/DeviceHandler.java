package org.instnt.accept.instntsdk.interfaces;

import android.content.Context;
import android.view.WindowManager;

import java.util.Map;

public interface DeviceHandler {

    public Map<String, String> getDeviceInfo(Context context, WindowManager windowManager);
}
