package com.github.user.searchgithub.presenter;


import com.github.user.searchgithub.model.UserResponseAPI;
import com.github.user.searchgithub.network.Api;
import com.github.user.searchgithub.network.helper.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private View view;

    public SearchPresenter(View view) {
        this.view = view;
    }

    public void searchData(String query, int page) {
        view.showProgressBar();
        Api api = ServiceApi.getSearch();
        Call<UserResponseAPI> call = api.getsearch(query, page);

        call.enqueue(new Callback<UserResponseAPI>() {
            @Override
            public void onResponse(Call<UserResponseAPI> call, Response<UserResponseAPI> response) {
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
            public void onFailure(Call<UserResponseAPI> call, Throwable t) {
                view.onFailure();
            }
        });
    }

    public interface View {
        void onSuccess(Response<UserResponseAPI> response);

        void showProgressBar();

        void hideProgressBar();

        void onFailure();

        void errorCode(int code);

    }

}
