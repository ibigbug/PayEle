package com.payele.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "app.db";
	private static final int DB_VERSION = 11;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String appTableString = "create table if not exists app" +
				"(_id integer primary key autoincrement," +
				"app_version string," +
				"isLogined integer default 0," +
				"currentUid integer default 0," +
				"session string default NULL)";
		db.execSQL(appTableString);
		db.execSQL("INSERT INTO app (isLogined, currentUid, session) VALUES (0, NULL, NULL);");
		
		String accountTableString = "create table if not exists account" +
				"(id integer primary key," +
				"username string," +
				"session string," +
				"email string," +
				"location string," +
				"screen_name string)";
		db.execSQL(accountTableString);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("drop table if exists app");
		db.execSQL("drop table if exists account");
		onCreate(db);
	}
}
