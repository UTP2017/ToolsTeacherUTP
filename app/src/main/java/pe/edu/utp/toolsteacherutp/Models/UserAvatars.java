package pe.edu.utp.toolsteacherutp.Models;

/**
 * Created by elbuenpixel on 15/03/17.
 */

public class UserAvatars {
    private String size_24;
    private String size_48;
    private String size_96;

    public String getSize_24() {
        return size_24;
    }

    public void setSize_24(String size_24) {
        this.size_24 = size_24;
    }

    public String getSize_48() {
        return size_48;
    }

    public void setSize_48(String size_48) {
        this.size_48 = size_48;
    }

    public String getSize_96() {
        return size_96;
    }

    public void setSize_96(String size_96) {
        this.size_96 = size_96;
    }

    @Override
    public String toString() {
        return "{\"size_24\": \""+ getSize_24() +"\", \"size_48\": " + getSize_48() +  ", \"size_96\": \"" + getSize_96() + "\"}";
    }
}
