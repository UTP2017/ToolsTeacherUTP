package pe.edu.utp.toolsteacherutp.Models;

/**
 * Created by elbuenpixel on 24/02/17.
 */
public class Course {
    private String slug;
    private String title;
    private int id;
    private String link;
    private AcfCourse acf;

    public AcfCourse getAcf() {
        return acf;
    }

    public void setAcf(AcfCourse acf) {
        this.acf = acf;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
