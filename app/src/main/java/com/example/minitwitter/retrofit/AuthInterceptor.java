package com.example.minitwitter.retrofit;

import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//In order to be able to use the private parts of the services offered by the API (for example service
// as getting all twitters) we need to start applying the authorization token in the requests we
// will be using.

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(Constants.PREF_TOKEN);
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
        return chain.proceed(request);
    }
}
