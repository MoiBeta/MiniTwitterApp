package com.example.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.databinding.ActivityDashboardBinding;
import com.example.minitwitter.ui.tweets.NewTweetDialogFragment;
import com.example.minitwitter.ui.tweets.TweetListFragment;
import com.example.minitwitter.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private ActivityDashboardBinding binding;
    ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_tweets_liked, R.id.navigation_profile)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragmentActivityDashboard.getId());
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment_activity_dashboard, TweetListFragment.newInstance(Constants.TWEET_LIST_ALL))
                .commit();

        binding.createTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewTweetDialogFragment");
            }
        });
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()){
            Glide.with(this)
                    .load(Constants.API_MINITWITTER_BASE_FILES_URL + photoUrl)
                    .dontAnimate()
                    //We should not use the cache memory to load images since
                    // they wont update
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageViewToolbarPhoto);
        }

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constants.API_MINITWITTER_BASE_FILES_URL + photo)
                        .dontAnimate()
                        //We should not use the cache memory to load images since
                        // they wont update
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.imageViewToolbarPhoto);
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        f = TweetListFragment.newInstance(Constants.TWEET_LIST_ALL);
                        binding.createTweetButton.show();
                        break;
                    case R.id.navigation_tweets_liked:
                        f = TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS);
                        binding.createTweetButton.hide();
                        break;
                    case R.id.navigation_profile:
                        f = new ProfileFragment();
                        binding.createTweetButton.hide();
                        break;
                }

                if(f != null){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_dashboard, f)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constants.SELECT_PHOTO_GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData(); //content://gallery/photos/..
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        //"filename" = filePathColum[0]
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        //Invoke selection of pictures from gallery
        Intent selectPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPicture, Constants.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this, "Not able to select pictures from gallery.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}