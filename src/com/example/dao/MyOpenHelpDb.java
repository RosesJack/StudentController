package com.example.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelpDb extends SQLiteOpenHelper {

	public MyOpenHelpDb(Context context) {
		super(context, "test.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ≥ı ºªØ
		db.execSQL("create table students (_id integer primary key autoincrement,name varchar(20),sex varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
