package com.george.memoshareapp.test;

/**
 * @projectName: recyclerView
 * @package: com.george.recyclerview
 * @className: CardItem
 * @author: George
 * @description: TODO
 * @date: 2023/5/16 17:30
 * @version: 1.0
 */
public class CardItem {
    private String title;
    private String description;
    private int imageResId;

    public CardItem() {
    }
    public CardItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}