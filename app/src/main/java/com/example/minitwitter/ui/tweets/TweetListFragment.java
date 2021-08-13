package com.example.minitwitter.ui.tweets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetListFragment extends Fragment {
    private int tweetListType = 1;
    RecyclerView recyclerView;
    MyTweetRecyclerViewAdapter adapter;
    List<Tweet> tweetList;
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    TweetViewModel tweetViewModel;
    SwipeRefreshLayout swipeRefreshLayout;

    public TweetListFragment() {
    }

    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        //When passing parameters, it is important to make sure that the name is a constant.
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        // Set the adapter
            Context context = view.getContext();
            recyclerView = view.findViewById(R.id.tweetList);
            //this is set so the user can refresh the list by swiping down the list
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.TwitterBlue));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Activates refreshing list animation
                    swipeRefreshLayout.setRefreshing(true);
                    if(tweetListType == Constants.TWEET_LIST_ALL){
                        loadNewData();
                    } else if(tweetListType == Constants.TWEET_LIST_FAVS){
                        loadNewFavData();
                    }
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new MyTweetRecyclerViewAdapter(
                    getActivity(),
                    tweetList);
            recyclerView.setAdapter(adapter);

            retrofitInit();

            if(tweetListType == Constants.TWEET_LIST_ALL){
                loadTweetData();
            } else if(tweetListType == Constants.TWEET_LIST_FAVS){
                loadFavTweetData();
            }
        return view;
    }

    private void loadFavTweetData() {
        tweetViewModel.getFavsTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
               tweetList = tweets;
               adapter.setData(tweetList);
               tweetViewModel.getNewFavTweets().removeObserver(this);
            }
        });
    }

    private void loadNewFavData() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);

            }
        });
    }

    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
    }

    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                //Deactivates refreshing list animation
                adapter.setData(tweetList);
            }
        });
    }

    private void loadNewData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                //Deactivates refreshing list animation
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getTweets().removeObserver(this);
            }
        });
    }
}