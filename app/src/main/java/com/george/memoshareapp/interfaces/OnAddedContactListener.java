package com.george.memoshareapp.interfaces;

import com.george.memoshareapp.beans.User;

import java.util.List;

public interface OnAddedContactListener {
    void onAddedContact(List<User> addedContactList);
}
