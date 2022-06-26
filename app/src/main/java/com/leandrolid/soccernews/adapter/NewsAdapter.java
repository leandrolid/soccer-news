package com.leandrolid.soccernews.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        News newsItem = news.get(position);

        holder.binding.tvTitle.setText(newsItem.title);
        holder.binding.tvDescription.setText(newsItem.description);
        Glide.with(context).load(newsItem.image).into(holder.binding.ivThumbnail);
        Glide.with(context).load(newsItem.image).circleCrop().into(holder.binding.ivAvatar);

        holder.binding.tbOpenLink.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(newsItem.link));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.binding.ibShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, newsItem.title);
            intent.putExtra(Intent.EXTRA_TEXT, newsItem.link);
            holder.itemView.getContext().startActivity(Intent.createChooser(intent, "Choose one"));
        });

        holder.binding.ibFavorite.setOnClickListener(view -> {
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
