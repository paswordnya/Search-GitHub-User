package com.github.user.searchgithub.view.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.user.searchgithub.R;
import com.github.user.searchgithub.model.UserResponseAPI;
import com.github.user.searchgithub.model.Users;
import com.github.user.searchgithub.presenter.DetailUserGithubPresenter;

import retrofit2.Response;

public class DetailUserGithub extends AppCompatActivity implements DetailUserGithubPresenter.UserDetailView {
    private DetailUserGithubPresenter detailUserGithubPresenter;
    private ImageView bgImage;
    private TextView profileName;
    private TextView bio;
    private TextView location;
    private TextView followersCount;
    private TextView followingCount;
    private ProgressBar progresBar;
    private String username = "";
    private CardView layoutCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_github);
        Intent dtIntent = getIntent(); // gets the previously created intent
        username = dtIntent.getStringExtra("username");
        bgImage = findViewById(R.id.bg_image);
        profileName = findViewById(R.id.profile_name);
        location = findViewById(R.id.location);
        bio = findViewById(R.id.bio);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        progresBar = findViewById(R.id.progresBar);
        layoutCard = findViewById(R.id.layoutCard);
        detailUserGithubPresenter = new DetailUserGithubPresenter(this);
        if (!username.equalsIgnoreCase("") || username != null) {
            detailUserGithubPresenter.getDetailUser(username);
        }else {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onSuccess(Response<Users> response) {
        Users users = response.body();
        Glide.with(this)
                .load(response.body()
                .getAvatarUrl())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.detail)
                .into(bgImage);
        profileName.setText(users.getName());
        bio.setText(users.getBio());
        location.setText("Location : "+ users.getLocation());
        followersCount.setText(String.valueOf(users.getFollowers()));
        followingCount.setText(String.valueOf(users.getFollowing()));
        layoutCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        progresBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progresBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure() {
        progresBar.setVisibility(View.GONE);
    }

    @Override
    public void errorCode(int code) {
        progresBar.setVisibility(View.GONE);
    }
}
