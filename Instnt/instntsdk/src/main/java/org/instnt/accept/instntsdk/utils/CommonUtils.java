package org.instnt.accept.instntsdk.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import retrofit2.HttpException;

public class CommonUtils {

    public static final String LOG_TAG = "InstntSDK";

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getErrorMessage(Throwable throwable) throws JSONException, IOException {
        String errorMsg = "";
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            if (error.response() != null && Objects.requireNonNull(error.response()).errorBody() != null) {
                errorMsg = error.response().errorBody().string();
                try {
                    JsonParser parser = new JsonParser();
                    JsonObject json = (JsonObject) parser.parse(errorMsg);
                    if (!json.isJsonNull() && json.has("errorMessage")) {
                        errorMsg = json.get("errorMessage").getAsString();
                    } else if (!json.isJsonNull() && json.has("message")) {
                        errorMsg = json.get("message").getAsString();
                    }
                } catch (Exception e) {
                    Log.i(CommonUtils.LOG_TAG, "Parsing error, no errorMessage key in response, so passing response message as it is");
                }
            } else {
                errorMsg = throwable.getLocalizedMessage();
            }
        } else {
            errorMsg = throwable.getLocalizedMessage();
        }
        if(errorMsg == null) {
            return "Internal server error";
        }
        return errorMsg;
    }
}
