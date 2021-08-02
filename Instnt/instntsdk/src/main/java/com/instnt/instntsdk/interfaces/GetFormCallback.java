package com.instnt.instntsdk.interfaces;

import com.instnt.instntsdk.data.FormCodes;

public interface GetFormCallback {
    void onResult(boolean success, FormCodes codes, String errMessage);
}
