package org.instant.accept.instntsdk.network;

import org.instant.accept.instntsdk.data.FormCodes;
import org.instant.accept.instntsdk.data.FormSubmitResponse;
import org.instant.accept.instntsdk.data.OTPResponse;

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

    @Headers("Content-Type: application/json")
    @POST()
    Observable<OTPResponse> sendOTP(@Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> getXNID(@Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> getUploadUrl(@Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: image/jpeg")
    @POST()
    Observable<Map<String, Object>> uploadDocument(@Url String url, @Body byte[] body);
}
