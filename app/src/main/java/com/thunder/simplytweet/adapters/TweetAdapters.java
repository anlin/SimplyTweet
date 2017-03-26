package com.thunder.simplytweet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anlinsquall on 25/3/17.
 */

public class TweetAdapters extends RecyclerView.Adapter<TweetAdapters.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_profile)
        ImageView profile;
        @BindView(R.id.user_username)
        TextView userName;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.body)
        TextView body;
        @BindView(R.id.time)
        TextView timeAgo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Tweet> tweets;
    private Context context;

    public TweetAdapters(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }

    private Context getContext(){
        return this.context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        //Bind data with view
        holder.name.setText(tweet.getName());
        holder.userName.setText(tweet.getScreenName());
        holder.body.setText(tweet.getBody());
        holder.timeAgo.setText(Utils.getRelativeTimeAgo(tweet.getTimestamp()));
        Picasso.with(context).load(tweet.getProfileImageUrl()).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
}
