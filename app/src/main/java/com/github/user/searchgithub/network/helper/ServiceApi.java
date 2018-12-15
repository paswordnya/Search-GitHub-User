package com.github.user.searchgithub.network.helper;


import com.github.user.searchgithub.network.Api;
import com.github.user.searchgithub.network.BaseID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceApi {
    private static OkHttpClient getOkHttpClient() {
        return  new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request authorisedRequest = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "")
                                .build();
                        return chain.proceed(authorisedRequest);
                    }
                }).build();
    }
    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BaseID.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();
    }



    public static Api getSearch() {
        return getRetrofitInstance().create(Api.class);
    }
    public static Api getUserDetail() {
        return getRetrofitInstance().create(Api.class);
    }

}
