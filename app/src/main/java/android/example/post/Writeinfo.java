package android.example.post;

public class Writeinfo {
    private String title;
    private String contents;
    private String writer;

    public Writeinfo(String title, String contents, String writer){
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public String getTitle(){return this.title;}
    public void getTitle(String title){this.title = title;}
    public String getContents(){return this.contents;}
    public void setContents(String contents){this.contents=contents;}
    public String getWriter(){return this.writer;}
    public void setWriter(String writer){this.writer=writer;}

}
