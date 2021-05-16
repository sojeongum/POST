package android.example.post;

import java.util.Date;

public class Comments {
    private String comment;
    private Date time;

    public Comments(){
        //empty constructor needed
    }
    public Comments(String comment, Date time){
        this.comment = comment;
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public Date getTime() {
        return time;
    }
}
