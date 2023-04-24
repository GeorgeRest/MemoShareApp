package com.george.memoshareapp.sourceprovider.dbaccess;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * @projectName: MemoShareApp
 * @package: com.george.memoshareapp.sourceprovider
 * @className: LitepalOp
 * @author: George
 * @description: 数据库操作
 * @date: 2023/4/23 14:55
 * @version: 1.0
 */
public class LitePalOP<T extends LitePalSupport> {
    private Class<T> zClass;
    private SQLiteDatabase db;

    public LitePalOP(Class<T> zClass) {
        this.zClass = zClass;
        db = LitePal.getDatabase();
    }

    public boolean insert(T entity) {
        return entity.save();
    }

    public boolean insert(List<T> entities){

        return LitePal.saveAll(entities);
    }
}
