package com.george.memoshareapp.beans;

public class ContactInfo {
    private String name;
    private int picture;

    public ContactInfo(){

    }
    public ContactInfo(String name, int picture){
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
