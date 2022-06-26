package com.leandrolid.soccernews.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leandrolid.soccernews.R;
import com.leandrolid.soccernews.databinding.NewsItemBinding;
import com.leandrolid.soccernews.domains.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<News> news;
    private final NewsListener favoriteClickListener;

    public NewsAdapter(List<News> news, NewsListener favoriteClickListener) {
        this.news = news;
        this.favoriteClickListener = favoriteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        @NonNull NewsItemBinding binding = NewsItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        NewsItemBinding binding = holder.binding;
        News newsItem = news.get(position);

        binding.tvTitle.setText(newsItem.title);
        binding.tvDescription.setText(newsItem.description);
        Glide.with(context).load(newsItem.image).into(binding.ivThumbnail);
        Glide.with(context).load(newsItem.image).circleCrop().into(binding.ivAvatar);
        Glide.with(context).load(newsItem.favorite ? R.drawable.ic_favorite_full_24 : R.drawable.ic_favorite_empty_24).into(binding.ibFavorite);

        binding.tbOpenLink.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(newsItem.link));
            context.startActivity(intent);
        });

        binding.ibShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, newsItem.title);
            intent.putExtra(Intent.EXTRA_TEXT, newsItem.link);
            context.startActivity(Intent.createChooser(intent, "Choose one"));
        });

        binding.ibFavorite.setOnClickListener(view -> {
            newsItem.favorite = !newsItem.favorite;
            favoriteClickListener.onClick(newsItem);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final NewsItemBinding binding;

        public ViewHolder(NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface NewsListener {
        void onClick(News news);
    }
}
