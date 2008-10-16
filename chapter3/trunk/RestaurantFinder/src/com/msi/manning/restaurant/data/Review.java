package com.msi.manning.restaurant.data;

import java.util.Date;

public class Review {
    public String name;
    public String author;
    public String link;
    public String imageLink;
    public String location;
    public String phone;
    public String rating;
    public Date date;
    public String content;
    public String cuisine; // input only

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("*Review*\n");
        sb.append("name:" + this.name + "\n");
        sb.append("author:" + this.author + "\n");
        sb.append("link:" + this.link + "\n");
        sb.append("imageLink:" + this.imageLink + "\n");
        sb.append("location:" + this.location + "\n");
        sb.append("phone:" + this.phone + "\n");
        sb.append("rating:" + this.rating + "\n");
        sb.append("date:" + this.date + "\n");
        sb.append("content:" + this.content);
        return sb.toString();
    }
}
