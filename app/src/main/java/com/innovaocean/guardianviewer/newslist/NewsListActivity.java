package com.innovaocean.guardianviewer.newslist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.innovaocean.guardianviewer.App;
import com.innovaocean.guardianviewer.GuardianRepository;
import com.innovaocean.guardianviewer.R;
import com.innovaocean.guardianviewer.databinding.NewsListBinding;
import com.innovaocean.guardianviewer.databinding.NewsListItemBinding;
import com.innovaocean.guardianviewer.model.NewsArticle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NewsListActivity extends AppCompatActivity implements NewsListPresenter.View {

    private NewsListPresenter presenter;
    private NewsListAdapter adapter;
    private NewsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsListAdapter();

        binding = DataBindingUtil.setContentView(this, R.layout.news_list);
        binding.articles.setLayoutManager(new LinearLayoutManager(this));
        binding.articles.setAdapter(adapter);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            presenter.refreshArticles();
        });

        GuardianRepository repository = ((App) getApplicationContext()).getGuardianRepository();
        presenter = new NewsListPresenter(this, repository, Schedulers.io(), AndroidSchedulers.mainThread());
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
    }

    @Override
    public void showNewsArticles(List<NewsArticle> articles) {
        binding.articles.setVisibility(View.VISIBLE);
        binding.errorView.setVisibility(View.GONE);
        adapter.setArticleList(articles);
    }

    @Override
    public void showError() {
        binding.articles.setVisibility(View.GONE);
        binding.errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayArticle(NewsArticle article) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(article.webUrl));
        startActivity(webIntent);
    }

    private class NewsListAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<NewsArticle> articleList = new ArrayList<>();

        void setArticleList(List<NewsArticle> articleList) {
            this.articleList = articleList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            NewsArticle article = articleList.get(position);
            holder.binding.title.setText(article.webTitle);

            if (article.fields != null && article.fields.thumbnail != null) {
                Glide.with(NewsListActivity.this)
                        .load(article.fields.thumbnail)
                        .into(holder.binding.icon);
            }

            holder.itemView.setOnClickListener(view -> {
                presenter.articleClicked(article);
            });
        }

        @Override
        public int getItemCount() {
            return articleList.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        NewsListItemBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
