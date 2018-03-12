package com.ziffytech.models;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public class CategoryModel {
    private String id;
    private String title;
    private String slug;
    private String parent;
    private String leval;
    private String description;
    private String image;
    private String status;
    private String Count;
    private String PCount;

    public String getId()
    {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug()
    {
        return this.slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParent()
    {
        return this.parent;
    }
    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLeval()
    {
        return this.leval;
    }
    public void setLeval(String leval) {
        this.leval = leval;
    }

    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage()
    {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus()
    {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount()
    {
        return this.Count;
    }
    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getPCount()
    {
        return this.PCount;
    }
    public void setPCount(String PCount) {
        this.PCount = PCount;
    }


}
