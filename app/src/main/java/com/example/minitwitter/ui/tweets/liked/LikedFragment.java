package com.example.minitwitter.ui.tweets.liked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.minitwitter.databinding.FragmentLikedBinding;


public class LikedFragment extends Fragment {

    private LikedVewModel likedVewModel;
    private FragmentLikedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        likedVewModel =
                new ViewModelProvider(this).get(LikedVewModel.class);

        binding = FragmentLikedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}