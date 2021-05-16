package android.example.post;

import java.util.Date;

public class Writeinfo extends PostId {
    private String title;
    private String contents;
    private String image;
    private Date time;

    public Writeinfo(){
        //empty constructor needed
    }

    public Writeinfo(String title, String contents, String image, Date time){
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getImage() {
        return image;
    }

    public Date getTime() {
        return time;
    }
}
