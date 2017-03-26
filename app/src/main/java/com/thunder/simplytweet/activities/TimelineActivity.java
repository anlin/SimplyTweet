package com.thunder.simplytweet.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.adapters.EndlessRecyclerViewScrollListener;
import com.thunder.simplytweet.adapters.TweetAdapters;
import com.thunder.simplytweet.fragments.ComposeDialogFragment;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeDialogListener {

    @BindView(R.id.tweets_timeline)
    RecyclerView tweetsView;

//    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<Tweet> tweets;
    TweetAdapters adapter;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setupTweet();
        loadMoreTweets(1);
    }

    private void showComposeDialog() {
        Log.d("DEBUG", "SHOW COMPOSE");
        FragmentManager manager = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance("Simply Tweet");
        composeDialogFragment.show(manager, "fragment_compose");
    }

    private void setupTweet() {

        tweets = new ArrayList<Tweet>();
        adapter = new TweetAdapters(this, tweets);
        tweetsView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tweetsView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreTweets(page);
            }
        };
        tweetsView.addOnScrollListener(scrollListener);
    }

    private void loadMoreTweets(int page){
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.getHomeTimeline(page, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                tweets.addAll(Tweet.fromJson(jsonArray));
                adapter.notifyDataSetChanged();
                Log.d("DEBUG", "timeline " + tweets.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                showComposeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinishComposeDialog(final String tweet) {
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.postTweet(tweet,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Tweet postedTweet = new Tweet(jsonObject);
                tweets.add(0, postedTweet);
                adapter.notifyItemInserted(0);
                tweetsView.scrollToPosition(0);
            }
        });
    }
}
