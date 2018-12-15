package com.github.user.searchgithub.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponseAPI {
    @SerializedName("items")
    private List<Users> items;


    public void setItems(List<Users> items){
        this.items = items;
    }

    public List<Users> getItems(){
        return items;
    }
}

