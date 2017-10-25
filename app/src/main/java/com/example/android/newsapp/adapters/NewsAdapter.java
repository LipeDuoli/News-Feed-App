package com.example.android.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.models.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<News> mNewsList;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News currentNews = mNewsList.get(position);

        holder.mTitle.setText(currentNews.getTitle());
        holder.mSection.setText(currentNews.getSectionName());
        holder.mDate.setText(formatDate(currentNews.getPublicationDate()));

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(currentNews.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void clear() {
        mNewsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<News> data) {
        mNewsList.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mSection;
        private final TextView mDate;
        private final View mRootView;

        public ViewHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.news_list_item_title);
            mSection = (TextView) itemView.findViewById(R.id.news_list_item_section);
            mDate = (TextView) itemView.findViewById(R.id.news_list_item_publication_date);
        }
    }

    /**
     * Retorna a data string formatada
     */
    private String formatDate(Date dateObject) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        return dateFormat.format(dateObject);
    }
}
