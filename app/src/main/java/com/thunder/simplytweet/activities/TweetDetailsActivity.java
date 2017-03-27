package com.thunder.simplytweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;
import com.thunder.simplytweet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.id.edit;
import static com.thunder.simplytweet.R.id.swipeContainer;

public class TweetDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tweet_details_profile_image)
    ImageView profileImage;
    @BindView(R.id.tweet_details_name)
    TextView name;
    @BindView(R.id.tweet_details_screen_name)
    TextView screenName;
    @BindView(R.id.tweet_details_body)
    TextView body;
    @BindView(R.id.tweet_details_media_image)
    ImageView mediaImage;
    @BindView(R.id.tweet_details_retweets_count)
    TextView retweetCount;
    @BindView(R.id.tweet_details_likes_count)
    TextView likesCount;
    @BindView(R.id.tweet_details_timestamp)
    TextView timestamp;
    @BindView(R.id.tweet_details_reply_edittext)
    EditText replyText;
    @BindView(R.id.tweet_details_reply_button)
    Button reply;

    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        ButterKnife.bind(this);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        setDataToViews(tweet);
    }

    private void setDataToViews(Tweet tweet) {
        Picasso.with(this).load(tweet.getProfileImageUrl()).into(profileImage);
        name.setText(tweet.getName());
        screenName.setText(tweet.getScreenName());
        body.setText(tweet.getBody());
        retweetCount.setText(String.valueOf(tweet.getRetweetsCount()));
        likesCount.setText(String.valueOf(tweet.getLikesCount()));
        timestamp.setText(Utils.getDateTime(tweet.getTimestamp()));
        replyText.setText(tweet.getScreenName()+" ");

        if(!TextUtils.isEmpty(tweet.getMediaImageUrl())){
            Picasso.with(this).load(tweet.getMediaImageUrl()).into(mediaImage);
        }else{
            mediaImage.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tweet_details_reply_button)
    public void replyTweet(){
        String status = replyText.getText().toString();
        String replyID = String.valueOf(tweet.getTweetId());
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.replyTweet(status, replyID, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject){
                Toast.makeText(TweetDetailsActivity.this,
                        "Successfully reply", Toast.LENGTH_LONG).show();
                replyText.setText("");
                //TODO Add newly reply in below reply list
                finish();
            }
        });
    }
}
