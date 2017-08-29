package com.innovaocean.guardianviewer.newslist;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.innovaocean.guardianviewer.App;
import com.innovaocean.guardianviewer.GuardianRepository;
import com.innovaocean.guardianviewer.R;
import com.innovaocean.guardianviewer.model.NewsArticle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewsListActivityTest {

    @Mock
    GuardianRepository repository;

    @Rule
    public ActivityTestRule<NewsListActivity> activityRule = new ActivityTestRule<>(NewsListActivity.class, false, false);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        App app = (App) InstrumentationRegistry.getTargetContext().getApplicationContext();
        app.setGuardianRepository(repository);
    }

    @Test
    public void showArticles() {
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.webTitle = "Test Article";

        List<NewsArticle> newsArticles = new ArrayList<>();
        newsArticles.add(newsArticle);

        when(repository.getNewsArticles())
                .thenReturn(Single.just(newsArticles));

        activityRule.launchActivity(new Intent());

        onView(withText("Test Article"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void showError() {
        when(repository.getNewsArticles())
                .thenReturn(Single.error(new Exception()));

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.error_view))
                .check(matches(isDisplayed()));
    }

}