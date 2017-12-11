package com.yl.yiliao.model;


import java.util.List;

public class FriendsData {
    private String GroupTitle;
    private List<FriendData> GroupFriends;

    public String getGroupTitle() {
        return GroupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        GroupTitle = groupTitle;
    }

    public List<FriendData> getGroupFriends() {
        return GroupFriends;
    }

    public void setGroupFriends(List<FriendData> groupFriends) {
        GroupFriends = groupFriends;
    }
}
