package com.github.user.searchgithub.presenter;

import com.github.user.searchgithub.model.UserResponseAPI;
import com.github.user.searchgithub.model.Users;
import com.github.user.searchgithub.network.Api;
import com.github.user.searchgithub.network.helper.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserGithubPresenter {
    private UserDetailView view;

    public DetailUserGithubPresenter(UserDetailView view) {
        this.view = view;
    }

    public void getDetailUser(String username){
        view.showProgressBar();
        Api api = ServiceApi.getUserDetail();
        Call<Users> call = api.getDetailUser(username);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                view.hideProgressBar();
                if (response != null && response.code() == 200) {
                    view.onSuccess(response);
                } else if (response.code() == 403) {
                    view.errorCode(response.code());
                } else {
                    view.onFailure();
                }
            }
            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                view.onFailure();
            }
        });
    }

    public interface UserDetailView {
        void onSuccess(Response<Users> response);

        void showProgressBar();

        void hideProgressBar();

        void onFailure();

        void errorCode(int code);

    }
}
