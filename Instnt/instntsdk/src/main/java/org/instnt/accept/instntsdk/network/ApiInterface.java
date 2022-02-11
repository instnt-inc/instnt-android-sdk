package org.instnt.accept.instntsdk.network;

import org.instnt.accept.instntsdk.model.FormCodes;
import org.instnt.accept.instntsdk.model.FormSubmitResponse;
import org.instnt.accept.instntsdk.model.OTPResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST()
    Observable<FormSubmitResponse> submitForm(@Header("User-Agent") String userAgent, @Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> sendOTP(@Header("User-Agent") String userAgent, @Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<FormCodes> getXNID(@Header("User-Agent") String userAgent, @Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> getUploadUrl(@Header("User-Agent") String userAgent, @Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Void> uploadDocument(@Header("User-Agent") String userAgent, @Url String url, @Body RequestBody image);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> verifyDocuments(@Header("User-Agent") String userAgent, @Url String url, @Body Map<String, Object> body);
}
