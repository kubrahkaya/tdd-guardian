package com.innovaocean.guardianviewer.newslist;

import com.innovaocean.guardianviewer.GuardianRepository;
import com.innovaocean.guardianviewer.model.NewsArticle;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

class NewsListPresenter {

    private View view;
    private CompositeDisposable disposables;
    private GuardianRepository guardianRepository;
    private Scheduler uiScheduler;
    private Scheduler ioScheduler;

    NewsListPresenter(View view, GuardianRepository repository, Scheduler ioScheduler, Scheduler uiScheduler) {
        this.view = view;
        this.guardianRepository = repository;
        this.disposables = new CompositeDisposable();
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }

    void start() {
        loadArticles();
    }

    void stop() {
        disposables.dispose();
    }

    void refreshArticles() {
        loadArticles();
    }

    void articleClicked(NewsArticle article) {
        view.displayArticle(article);
    }

    private void loadArticles() {
        guardianRepository.getNewsArticles()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(newsArticles -> {
                    view.showNewsArticles(newsArticles);
                }, throwable -> {
                    throwable.printStackTrace();
                    view.showError();
                });
    }

    interface View {
            void showNewsArticles(List<NewsArticle> articles);

        void displayArticle(NewsArticle article);

        void showError();
    }

}
