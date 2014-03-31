package com.payele.storage;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class ModelAccount {
	private int id;
	private String username;
	private String session;
	private String screen_name;
	private String location;
	private String email;
	
	public ModelAccount(){}
	
	public ModelAccount(JSONObject accountJsonObject) {
		try {
	        id = accountJsonObject.getInt("id");
	        username = accountJsonObject.getString("username");
			screen_name = accountJsonObject.getString("screen_name");
			session = accountJsonObject.getString("session");
        } catch (JSONException e) {
	        Log.e("Init Model Account", "Wrong accountJsonObject: " + accountJsonObject.toString());
	        e.printStackTrace();
        }
		
	}

	
    /**
     * @return the id
     */
    public int getId() {
    	return id;
    }

	
    /**
     * @param id the id to set
     */
    public void setId(int id) {
    	this.id = id;
    }

	
    /**
     * @return the username
     */
    public String getUsername() {
    	return username;
    }

	
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
    	this.username = username;
    }

	
    /**
     * @return the session
     */
    public String getSession() {
    	return session;
    }

	
    /**
     * @param session the session to set
     */
    public void setSession(String session) {
    	this.session = session;
    }

	
    /**
     * @return the screen_name
     */
    public String getScreen_name() {
    	return screen_name;
    }

	
    /**
     * @param screen_name the screen_name to set
     */
    public void setScreen_name(String screen_name) {
    	this.screen_name = screen_name;
    }

	
    /**
     * @return the location
     */
    public String getLocation() {
    	return location;
    }

	
    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
    	this.location = location;
    }

	
    /**
     * @return the email
     */
    public String getEmail() {
    	return email;
    }

	
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
    	this.email = email;
    }

	/* (non-Javadoc)
    * Title: toString
    * Description:
    * @return
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
	    return "ModelAccount [id=" + id + ", username=" + username
	            + ", session=" + session + ", screen_name=" + screen_name + "]";
    }
	
	
}
