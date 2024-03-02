package com.george.memoshareapp.beans;

import java.io.Serializable;

public class Danmu implements Serializable {

    private String name;
    private String danmu_content;

    @Override
    public String toString() {
        return "Danmu{" +
                "name='" + name + '\'' +
                ", danmu_content='" + danmu_content + '\'' +
                '}';
    }

    public Danmu(String name, String danmu_content) {
        this.name = name;
        this.danmu_content = danmu_content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDanmu_content() {
        return danmu_content;
    }

    public void setDanmu_content(String danmu_content) {
        this.danmu_content = danmu_content;
    }
}
