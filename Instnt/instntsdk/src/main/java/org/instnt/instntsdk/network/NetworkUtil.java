package org.instnt.instntsdk.network;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.instnt.instntsdk.data.FormCodes;
import org.instnt.instntsdk.data.FormSubmitResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkUtil {

    private ApiInterface sandboxApiInterface;
    private ApiInterface productApiInterface;

    public NetworkUtil() {
        sandboxApiInterface = getApiService(true);
        productApiInterface = getApiService(false);
    }

    private OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(createHttpLoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "text/plain,application/json;version=1");

                        return chain.proceed(ongoing.build());
                    }
                })
                .build();
    }

    private Retrofit getRetrofit(OkHttpClient httpclient, boolean isSandbox){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(isSandbox? RestUrl.BASE_URL : RestUrl.PRODUCTION_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create()) //to send string as body
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder.client(httpclient).build();
    }

    private ApiInterface getApiService(boolean isSandbox) {
        Retrofit retrofit = getRetrofit(getOkHttpClient(), isSandbox);

        return retrofit.create(ApiInterface.class);
    }

    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @SuppressLint("CheckResult")
    public Observable<FormCodes> getFormFields(String formId, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        return apiInterface.getFormCodes(formId, "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public Observable<FormSubmitResponse> submit(String url, Map<String, Object> body, boolean isSandbox) {
        ApiInterface apiInterface = isSandbox? sandboxApiInterface : productApiInterface;

        return apiInterface.submitForm(url, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
