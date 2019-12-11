package com.blueradix.android.monstersrecyclerviewwithsqlite.Entities;

import java.io.Serializable;

public class Monster implements Serializable {


    public Long id;
    public String name;
    public String description;
    public Integer scariness;
    public String imageFileName;

    public Monster(Long id, String name, String description, Integer scariness, String imageFileName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.scariness = scariness;
        this.imageFileName = imageFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScariness() {
        return scariness;
    }

    public void setScariness(Integer scariness) {
        this.scariness = scariness;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scariness=" + scariness +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}
