
package com.codepath.instagramviewer.model;

public class InstagramPhotoComment {

    private String userName;
    private String commentText;
    private String profilePicUrl;
    private long createdTime;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public InstagramPhotoComment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public InstagramPhotoComment() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}
