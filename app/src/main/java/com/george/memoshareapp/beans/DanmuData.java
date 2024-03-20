package com.george.memoshareapp.beans;

import com.orient.tea.barragephoto.model.DataSource;

public class DanmuData implements DataSource {
    private String content;
    private String userName;

    public DanmuData(String content, String userName) {
        this.content = content;
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int getType() {

        return 0;
    }

    @Override
    public long getShowTime() {
        return 0;
    }
}
