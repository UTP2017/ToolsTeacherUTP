package pe.edu.utp.toolsteacherutp.Models;

/**
 * Created by elbuenpixel on 24/02/17.
 */
public class SingleCourse {
    private int ID;
    private int post_author;
    private String post_content;
    private String post_title;
    private String post_excerpt;

    public SingleCourse() {
    }

    public SingleCourse(int ID, int post_author, String post_content, String post_title, String post_excerpt) {
        this.ID = ID;
        this.post_author = post_author;
        this.post_content = post_content;
        this.post_title = post_title;
        this.post_excerpt = post_excerpt;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPost_author() {
        return post_author;
    }

    public void setPost_author(int post_author) {
        this.post_author = post_author;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }
}
