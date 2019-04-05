package com.example.fyp;

import android.media.Image;

public class Images {

    private Image image;
    private String imageId;
    private String imageName;

    private Images (Image image, String imageId, String imageName){
        this.image = image;
        this.imageId = imageId;
        this.imageName = imageName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
