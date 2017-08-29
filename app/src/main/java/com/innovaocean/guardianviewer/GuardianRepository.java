package com.innovaocean.guardianviewer;

import com.innovaocean.guardianviewer.model.NewsArticle;

import java.util.List;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuardianRepository {

    private GuardianService guardianService;

    GuardianRepository() {
        guardianService = new Retrofit.Builder()
                .baseUrl("http://content.guardianapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GuardianService.class);
    }

    public Single<List<NewsArticle>> getNewsArticles() {
        return guardianService.searchArticles("olympics")
                .map(apiResponse -> apiResponse.response.results);
    }

}
