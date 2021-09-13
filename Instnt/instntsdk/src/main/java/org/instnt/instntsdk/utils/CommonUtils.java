package org.instnt.instntsdk.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import retrofit2.HttpException;

public class CommonUtils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getErrorMessage(Throwable throwable) throws JSONException, IOException {
        String errorMsg = "";
        if (throwable instanceof HttpException) {

            HttpException error = (HttpException) throwable;

            if (error.response() != null && Objects.requireNonNull(error.response()).errorBody() != null){
                errorMsg = error.response().errorBody().string();
//                String errorBody = error.response().errorBody().string();
//                JSONObject errorParentObj = new JSONObject(errorBody);
//
//                if (!errorParentObj.isNull("errorMessage")){
//                    errorMsg = errorParentObj.getString("errorMessage");
//                }else {
//                    errorMsg = throwable.getLocalizedMessage();
//                }
            }else {
                errorMsg = throwable.getLocalizedMessage();
            }
        }else {
            errorMsg = throwable.getLocalizedMessage();
        }

        return errorMsg;
    }
}
