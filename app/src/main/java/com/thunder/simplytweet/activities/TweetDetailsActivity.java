package com.thunder.simplytweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.utils.Utils;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        ButterKnife.bind(this);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
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

        if(!TextUtils.isEmpty(tweet.getMediaImageUrl())){
            Picasso.with(this).load(tweet.getMediaImageUrl()).into(mediaImage);
        }else{
            mediaImage.setVisibility(View.GONE);
        }
    }
}
