package com.example.minitwitter.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.ui.tweets.BottonModalTweetFragment;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;
    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets(){ return tweets; }

    public void openDialogTweetMenu(Context ctx, int idTweet){
        BottonModalTweetFragment dialogTweet = BottonModalTweetFragment.newInstance(idTweet);
    dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "BottonModalTweetFragment");
    }

    public LiveData<List<Tweet>> getFavsTweets(){
        favTweets = tweetRepository.getFavsTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets(){
        getNewTweets();
        return getFavsTweets();
    }

    public void insertTweet(String message){
        tweetRepository.createTweet(message);
    }

    public void likeTweet(int idTweet){
        tweetRepository.likeTweet(idTweet);
    }

    public void deleteTweet(int idTweet){
       tweetRepository.deleteTweet(idTweet);
    }
}
