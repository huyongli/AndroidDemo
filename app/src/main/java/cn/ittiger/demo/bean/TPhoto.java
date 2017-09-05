package cn.ittiger.demo.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: ylhu
 * @time: 17-9-5
 */

public class TPhoto {

    private String title;

    private List<String> photos;

    public TPhoto(String title) {

        this.title = title;
        this.photos = new ArrayList<>();
        int random = new Random().nextInt(5) + 5;
        for(int i = 0; i < random; i++) {
            this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
        }
//        this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
//        this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
//        this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
//        this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
//        this.photos.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
    }

    public String getTitle() {

        return title;
    }

    public List<String> getPhotos() {

        return photos;
    }


}
