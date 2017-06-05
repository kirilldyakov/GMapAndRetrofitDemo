package ru.strongit.googlemaps;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * Класс приложения
 */

public class GMapAndRetrofitApp extends Application {
    private static GMapAndRetrofitApi gMapAndRetrofitApi;
    private Retrofit retrofit;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://demo3526062.mockable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gMapAndRetrofitApi = retrofit.create(GMapAndRetrofitApi.class);
    }

    public static GMapAndRetrofitApi getApi() {
        return gMapAndRetrofitApi;
    }
}
