package com.example.minitwitter.ui.tweets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button btTwit;
    private ImageView iVClose, ivAvatar;
    private EditText etMessage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.nuevo_tweet_full_dialog, container, false);
        btTwit = view.findViewById(R.id.buttonTwittear);
        iVClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewavatar);
        etMessage = view.findViewById(R.id.editTextTextMultiLine);
        btTwit.setOnClickListener(this);
        iVClose.setOnClickListener(this);
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constants.API_MINITWITTER_BASE_URL + photoUrl)
                    .into(ivAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String message = etMessage.getText().toString();
        if(id == R.id.buttonTwittear){
            if(message.isEmpty()){
                Toast.makeText(getActivity(), "You need to write something", Toast.LENGTH_SHORT).show();
            } else {
                TweetViewModel tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(message);
                getDialog().dismiss();
            }
        } else if (id == R.id.imageViewClose){
            if(!message.isEmpty()){
                showDialogConfirm();
            }else{
                getDialog().dismiss();
            }
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to cancel your tweet? The message will be erased.").setTitle("Cancel Tweet");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
