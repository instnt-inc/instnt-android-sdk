package com.instnt.instntsdk.network;

import com.instnt.instntsdk.data.FormCodes;
import com.instnt.instntsdk.data.FormSubmitData;
import com.instnt.instntsdk.data.FormSubmitResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET(RestUrl.GET_FORM_CODES)
    Observable<FormCodes> getFormCodes(@Path("key") String key, @Query("format") String format);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<FormSubmitResponse> submitForm(@Url String url, @Body Map<String, Object> body);
}
