
package com.codepath.instagramviewer.model;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhoto {

    private String profilePicUrl;
    private String username;
    private String imageUrl;
    private long createdTime;
    private int imageHeight;
    private int likesCount;
    private String caption;
    private List<InstagramPhotoComment> comments;
    private int commentsCount;
    private String photoId;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<InstagramPhotoComment> getComments() {
        return comments;
    }

    public void setComments(List<InstagramPhotoComment> comments) {
        if (comments == null) {
            this.comments = new ArrayList<InstagramPhotoComment>();
        } else {
            this.comments = comments;
        }
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}
