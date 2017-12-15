package com.gonext.callreminder.datalayers.retrofit;


import com.gonext.callreminder.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static Retrofit retrofit;
    private static Retrofit adRetrofit;
    private static OkHttpClient okHttpClient;


    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);


    private static OkHttpClient getHttpClient(){

        if (okHttpClient == null) {
            return new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();
        }else{
            return okHttpClient;
        }
    }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with ad Base url.
     * @return instance of ad retrofit.
     */
    private static Retrofit getAdRetrofit() {
        if (adRetrofit != null){
            return adRetrofit;
        }else{
            adRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_AD_URL)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return adRetrofit;
        }

    }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     * @return instance of ad retrofit.
     */

    private static Retrofit getRetrofit() {
        if (retrofit != null){
            return retrofit;
        }else{
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_AD_URL)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }

    }

    public static <S> S createAdService(Class<S> serviceClass) {
        return getAdRetrofit().create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass) {
        return getRetrofit().create(serviceClass);
    }



}
