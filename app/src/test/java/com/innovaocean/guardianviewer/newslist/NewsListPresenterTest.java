package com.innovaocean.guardianviewer.newslist;

import com.innovaocean.guardianviewer.GuardianRepository;
import com.innovaocean.guardianviewer.model.NewsArticle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsListPresenterTest {

    @Mock
    NewsListPresenter.View view;

    @Mock
    GuardianRepository repository;

    NewsListPresenter presenter;

    @Before
    public void setUp() {
        presenter = new NewsListPresenter(view, repository, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void testShowArticles() {
        //given
        NewsArticle article = new NewsArticle();

        List<NewsArticle> articlesList = new ArrayList<>();
        articlesList.add(article);

        //when
        when(repository.getNewsArticles()).thenReturn(Single.just(articlesList));
        presenter.start();

        //then
        verify(view, times(1)).showNewsArticles(articlesList);
    }

    @Test
    public void testShowError() {
        when(repository.getNewsArticles()).thenReturn(Single.error(new Exception()));
        presenter.start();

        verify(view, times(1)).showError();
    }

    @Test
    public void testRefreshArticles() {
        when(repository.getNewsArticles())
                .thenReturn(Single.just(new ArrayList<>()));

        presenter.refreshArticles();
        verify(repository, times(1)).getNewsArticles();
    }

    @Test
    public void testArticleClicked() {
        NewsArticle article = new NewsArticle();
        presenter.articleClicked(article);
        verify(view, times(1))
                .displayArticle(article);
    }

}