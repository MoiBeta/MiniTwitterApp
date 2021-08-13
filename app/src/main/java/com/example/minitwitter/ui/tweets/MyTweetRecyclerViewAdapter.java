package com.example.minitwitter.ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.databinding.FragmentTweetBinding;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {
    private Context ctx;
    private List<Tweet> mValues;
    String userName;
    TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        ctx = context;
        mValues = items;
        userName = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);
        tweetViewModel = new ViewModelProvider((FragmentActivity) ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTweetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null){
            holder.mItem = mValues.get(position);
            holder.tvUserName.setText("@" + holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();

            if(!photo.equals("")){
                Glide.with(ctx)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                        .into(holder.ivAvatar);
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);

            holder.ivShowMenu.setVisibility(View.GONE);
            if(holder.mItem.getUser().getUsername().equals(userName)){
                holder.ivShowMenu.setVisibility(View.VISIBLE);
            }

            holder.ivShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.openDialogTweetMenu(ctx, holder.mItem.getId());
                }
            });

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            for(Like like: holder.mItem.getLikes()){
                if(like.getUsername().equals(userName)){
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.Pink));
                    holder.tvLikesCount.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }

            if(holder.mItem.getLikes().size() > 0){

            }
        }
    }

    public void setData(List<Tweet> tweetList){
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final ImageView ivShowMenu;
        public final TextView tvUserName;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(FragmentTweetBinding binding) {
            super(binding.getRoot());
            mView = binding.item;
            ivAvatar = binding.imageViewAvatar;
            ivLike = binding.imageViewLike;
            tvUserName = binding.textViewUsername;
            tvMessage = binding.textViewMessage;
            tvLikesCount = binding.textViewLikes;
            ivShowMenu = binding.imageViewShowMenu;
        }
    }
}