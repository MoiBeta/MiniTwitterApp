package com.example.minitwitter.ui.profile;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.databinding.FragmentProfileBinding;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    boolean loadingData = true;
    PermissionListener allPermissionsListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.btChangePassword.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Click on edit", Toast.LENGTH_SHORT).show();
        });
        binding.btSave.setOnClickListener(view -> {
            String username = binding.etUserName.getText().toString();
            String email = binding.etEmail.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            String website = binding.editTextWbesite.getText().toString();
            String password = binding.etCurrentPassword.getText().toString();

            if(username.isEmpty()){
                binding.etUserName.setError("The User Name is needed to continue");
            } else if (email.isEmpty()){
                binding.etEmail.setError("The Email address is needed to continue");
            }else if(password.isEmpty()){
                binding.etCurrentPassword.setError("The Password is needed to continue");
            } else{
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, description, website, password);
                profileViewModel.updateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Updating information", Toast.LENGTH_SHORT).show();
                binding.btSave.setEnabled(false);
            }
        });

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Invoke image selection
                //Invoke method to check permissions
                checkPermissions();
            }
        });

        //Viewmodel
        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {
                loadingData = true;
                binding.etUserName.setText(responseUserProfile.getUsername());
                binding.etEmail.setText(responseUserProfile.getEmail());
                binding.editTextWbesite.setText(responseUserProfile.getWebsite());
                binding.editTextDescription.setText(responseUserProfile.getDescripcion());
                if (!loadingData) {
                    binding.btSave.setEnabled(false);
                    Toast.makeText(getActivity(), "Information saved correctly", Toast.LENGTH_SHORT).show();
                }
                if(!responseUserProfile.getPhotoUrl().isEmpty()){
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWITTER_BASE_FILES_URL + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            //We should not use the cache memory to load images since
                            // they wont update
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.ivAvatar);
                }
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String photo) {
                if(!photo.isEmpty()){
                Glide.with(getActivity())
                        .load(Constants.API_MINITWITTER_BASE_FILES_URL + photo)
                        .dontAnimate()
                        //We should not use the cache memory to load images since
                        // they wont update
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.ivAvatar);
                }
            }
        });

        return binding.getRoot();
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permissions")
                .withMessage("The requested permissions are neccesary to select pictures")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener
        );

        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}