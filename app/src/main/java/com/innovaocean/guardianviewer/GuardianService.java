package com.innovaocean.guardianviewer;

import com.innovaocean.guardianviewer.model.ApiResponse;
import com.innovaocean.guardianviewer.model.NewsArticle;
import com.innovaocean.guardianviewer.model.SearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GuardianService {

    String API_KEY_PARAMETER = "&api-key=3f88b1f1-1d20-4c35-b34c-8884b4bc3639";

    @GET("/search?show-fields=webTitle,thumbnail&page-size=50" + API_KEY_PARAMETER)
    Single<ApiResponse<SearchResponse>> searchArticles(@Query("q") String searchTerm);

    @GET
    Single<NewsArticle> getArticle(@Url String articleUrl, @Query("show-fields") String fields);

}
