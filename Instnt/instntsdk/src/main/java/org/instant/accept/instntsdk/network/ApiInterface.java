package org.instant.accept.instntsdk.network;

import org.instant.accept.instntsdk.model.FormCodes;
import org.instant.accept.instntsdk.model.FormSubmitResponse;
import org.instant.accept.instntsdk.model.OTPResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Call<Map<String, Object>> getXNID1(@Url String url, @Body Map<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST()
    Observable<Map<String, Object>> getUploadUrl(@Url String url, @Body Map<String, Object> body);

//    @Headers({"Accept: application/json", "Content-Type: image/jpeg"})
    @Headers("Content-Type: image/jpeg")
    @PUT()
    @Multipart
//    Observable<Map<String, Object>> uploadDocument(@Url String url, @Part("file\"; filename=\"test.jpg\" ") RequestBody file);
    Observable<Map<String, Object>> uploadDocument(@Url String url, @Part MultipartBody.Part file);
//    Call<Map<String, Object>> uploadDocument(@Url String url, @Part MultipartBody.Part file);
//    Observable<Map<String, Object>> uploadDocument(@Url String url, @PartMap Map<String, RequestBody> map);
}
