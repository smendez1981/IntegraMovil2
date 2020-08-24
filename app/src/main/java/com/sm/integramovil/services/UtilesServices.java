package com.sm.integramovil.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.sm.integramovil.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class UtilesServices {

    public static String BASE_URL ="";
    public static String PREFS_NAME = "integramovil";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Context context) {
        if (retrofit==null) {

            SharedPreferences pref =  context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            BASE_URL =  pref.getString("apiurl", "http://10.0.2.2:5000/");

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .addInterceptor(new BasicAuthInterceptor("admin", "admin"))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiEndpointInterface getAPIService(Context context) {
        return UtilesServices.getClient(BASE_URL,context).create(ApiEndpointInterface.class);
    }

}

