package com.example.johanna.hangman;

import android.test.suitebuilder.annotation.LargeTest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Johanna on 19.12.2016.
 */

public interface RetrofitAPI {

    @GET("hangman/{min}")
    Call<ResponseContent> getHangmanWord(@Path("min") String min);


}
