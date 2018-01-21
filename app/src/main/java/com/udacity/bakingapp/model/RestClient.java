package com.udacity.bakingapp.model;

import com.udacity.bakingapp.BuildConfig;
import com.udacity.bakingapp.IdlingResource.CustomIdlingRegistry;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class RestClient {
    private static final String TAG = RestClient.class.getSimpleName();
    private static Api REST_CLIENT;
    private static String ROOT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static Api post() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        //to enable log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            CustomIdlingRegistry.registerOkHttp(httpClient.build());
        }
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(3);
        httpClient.dispatcher(dispatcher);
        httpClient.addInterceptor(logging);
        // end

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT)
                .addConverterFactory(CustomConverter.create())
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(Api.class);
    }
}
