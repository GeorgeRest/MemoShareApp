package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.User;

public interface OnSaveUserListener {
    void OnSaveUserListener(User user);
    void OnCount(Long count);
}
