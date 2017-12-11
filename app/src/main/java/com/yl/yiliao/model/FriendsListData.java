package com.yl.yiliao.model;


import java.util.List;

public class FriendsListData extends BaseModel<FriendsListData>{
    private List<FriendsData> Friends;

    public List<FriendsData> getFriends() {
        return Friends;
    }

    public void setFriends(List<FriendsData> friends) {
        Friends = friends;
    }
}
