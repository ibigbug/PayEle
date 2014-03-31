package com.payele.storage;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context){
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void testWork() {
		ModelApp app = loadAppInfo();
		Log.d("TEST_DB_WORK", app.toString());			
	}
	
	private Cursor queryTheCursor (String table_name) {
		Cursor c = db.rawQuery("SELECT * FROM " + table_name, null);
		return c;
	}
	
	public ModelApp loadAppInfo() {
	    ModelApp app = new ModelApp();
	    Cursor cursor = queryTheCursor("app");
	    while (cursor.moveToNext()) {
	    	// THERE IS ONLY ONE QUERY RESULT FROM TABLE app
	    	app.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
	        app.setIsLogined(cursor.getInt(cursor.getColumnIndex("isLogined")));
	        app.setCurrentUid(cursor.getInt(cursor.getColumnIndex("currentUid")));
	        app.setSession(cursor.getString(cursor.getColumnIndex("session")));
	        app.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
        }
	    return app;
    }
	
	public void loginUser (int uid, String session){
		ContentValues cv = new ContentValues();
		cv.put("isLogined", 1);	
		cv.put("currentUid", uid);
		cv.put("session", session);
		db.update("app", cv, "_id = ?", new String[]{"1"});
		
	}
	
	public void logoutUser () {
		ContentValues cv = new ContentValues();
		cv.put("currentUid", 0);
		cv.put("isLogined", 0);
		cv.put("session", "NULL");
		db.update("app", cv, "_id = ?", new String[]{"1"});
		
	}
	
	
	/*
	 * Account Manager START
	 */
	
	public ModelAccount createAccount (ModelAccount account) {
		ContentValues cv = new ContentValues();
		cv.put("id", account.getId());
		cv.put("username", account.getUsername());
		cv.put("screen_name", account.getScreen_name());
		cv.put("session", account.getSession());
		
		db.insert("account", null, cv);
		Log.d("CREATE_ACCOUNT_DEBUG", account.toString());
		
		return account;
	}
	
	public ModelAccount createOrUpdateAccount (ModelAccount account) {
		ModelAccount existAccount = retriveAccount(account.getUsername());
		if (existAccount == null)
			return createAccount(account);
		else {
			ContentValues cv = new ContentValues();
			cv.put("id", account.getId());
			cv.put("username", account.getUsername());
			cv.put("screen_name", account.getScreen_name());
			cv.put("session", account.getSession());
			
			updateAccount(account.getId(), cv);
			return retriveAccount(existAccount.getId());
		} 
			
	}
	
	public ModelAccount updateAccount (int uid, ContentValues cv) {
		db.update("account", cv, "id = ?", new String[]{String.valueOf(uid)});
		ModelAccount account = retriveAccount(uid);
		return account;
	}
	
	public ModelAccount updateAccount (int uid, JSONObject profile) {
		ContentValues cv = new ContentValues();
		try {
	        cv.put("screen_name", profile.getString("screen_name"));
	        cv.put("location", profile.getString("location"));
	        cv.put("email", profile.getString("email"));
	        
        } catch (JSONException e) {
        	Log.e("DBManager", "Json Error :" + profile.toString());
	        e.printStackTrace();
        }
		
		return updateAccount(uid, cv);
	}
	
	public ModelAccount retriveAccount (int uid) {
		ModelAccount account = new ModelAccount();
		Cursor cursor = db.rawQuery("SELECT * FROM account WHERE id = ?", new String[]{String.valueOf(uid)});
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			account.setId(cursor.getInt(cursor.getColumnIndex("id")));
			account.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			account.setScreen_name(cursor.getString(cursor.getColumnIndex("screen_name")));
			account.setSession(cursor.getString(cursor.getColumnIndex("session")));
			account.setScreen_name(cursor.getString(cursor.getColumnIndex("screen_name")));
			account.setLocation(cursor.getString(cursor.getColumnIndex("location")));
			account.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			
			return account;
		} else
			return null;
	}
	
	public ModelAccount retriveAccount(String username) {
		Cursor cursor = db.rawQuery("SELECT * FROM account WHERE username = ?", new String[]{username});
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return retriveAccount(cursor.getInt(cursor.getColumnIndex("id")));
		} else
			return null;
    }
	
	public void deleteAccount (int uid) {
		db.delete("account", "id = ?", new String[]{String.valueOf(uid)});
	}
	
	/*
	 * Account Manager END 
	 */
	
	public void closeDB() {
		db.close();
	}

}
