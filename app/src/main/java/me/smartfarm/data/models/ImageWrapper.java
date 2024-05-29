package me.smartfarm.data.models;

import java.util.List;

public class ImageWrapper {
    private List<String> image ;

    public ImageWrapper() {
    }

    public ImageWrapper(List<String> image) {
        this.image = image;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
