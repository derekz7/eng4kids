package com.ducle.learnengforkids.Module;

public class VideoYT {
    private String Title;
    private String Thumbnail;
    private String video_ID;

    public  VideoYT(){

    }

    public VideoYT(String title, String thumbnail, String video_ID) {
        Title = title;
        Thumbnail = thumbnail;
        this.video_ID = video_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getVideo_ID() {
        return video_ID;
    }

    public void setVideo_ID(String video_ID) {
        this.video_ID = video_ID;
    }
}
