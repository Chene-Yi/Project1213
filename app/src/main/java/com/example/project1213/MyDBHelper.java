package com.example.project1213;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 繼承 SQLiteOpenHelper 類別
public class MyDBHelper extends SQLiteOpenHelper {
    private static final String name = "mdataBase.db"; // 資料庫名稱
    private static final int version = 1; // 資料庫版本

    // 自訂建構子，只需傳入一個 Context 物件即可
    MyDBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立 myTable 資料表，包含一個 book 字串欄位和一個 price 整數欄位
        db.execSQL("CREATE TABLE myTable(book text PRIMARY KEY, price integer NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS myTable;");
        onCreate(db);
    }
}

