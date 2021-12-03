package com.instnt.instntsdk.authentication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.idmetrics.dc.DSHandler;
import com.idmetrics.dc.utils.DSCaptureMode;
import com.idmetrics.dc.utils.DSError;
import com.idmetrics.dc.utils.DSHandlerListener;
import com.idmetrics.dc.utils.DSOptions;
import com.idmetrics.dc.utils.DSResult;

public class AuthenticID {

    public AuthenticID(Context baseContext) {
        processToCallScan(baseContext);
    }

    public void processToCallScan(android.content.Context context) {
        DSOptions dsOptions = new DSOptions();
        dsOptions.licensingKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";
        scanIdDocument(dsOptions, true, context);
    }

    private void scanIdDocument(DSOptions settings, Boolean isFront, android.content.Context context) {

        DSHandler dsHandler = DSHandler.getInstance(context);
        DSHandler.staticLicenseKey = "AwFuEf5j3YXwEACwj9eE4w6RGWQ0zgPbjGmu+Xw684ryGP3GicSEE7ZYB0FAhoikRH3imeR02U7kuT4OjVL5B1s3JhBrPY9KWU9sgCVmTIW0r7ehq9CvTjTBfaR7NTCV179MlNeDbEzwh5FSD8ROc3Zq";
        dsHandler.options = settings;
        dsHandler.init(DSCaptureMode.Manual, new DSHandlerListener() {
            @Override
            public void handleScan(DSResult dsResult) {
                System.out.println("test1");
            }

            @Override
            public void scanWasCancelled() {
                System.out.println("test2");
            }

            @Override
            public void captureError(DSError dsError) {
                System.out.println("test3 : " + dsError.message);
            }
        });

        dsHandler.start();
    }
}
