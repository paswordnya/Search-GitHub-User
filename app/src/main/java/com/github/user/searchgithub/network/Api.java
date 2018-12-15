
package com.github.user.searchgithub.network;

import com.github.user.searchgithub.model.UserResponseAPI;
import com.github.user.searchgithub.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET(BaseID.GET_SEARCH)
    Call<UserResponseAPI> getsearch(
            @Query("q") String query,
            @Query("page") int page);

    @GET(BaseID.GET_USER_DETAIL+"{username}")
    Call<Users> getDetailUser(@Path("username") String username);

}