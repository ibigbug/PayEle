package com.payele.storage;

/**
 * 
 * @ClassName: ModelApp 
 * @Description: Global APP Model
 * only on record with _id = 1
 * save app info and if any user 
 * has logined
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 11, 2014 1:08:24 PM 
 *
 */

public class ModelApp {
	public int _id;
	public String app_version;
	public int isLogined;
	public int currentUid;
	public String session;

	public ModelApp(){}
	
	public ModelApp (int currentUid) {
	    this.currentUid = currentUid;
    }

	
    
    /**
     * @return the _id
     */
    public int get_id() {
    	return _id;
    }

	
    /**
     * @param _id the _id to set
     */
    public void set_id(int _id) {
    	this._id = _id;
    }

	/**
     * @return the app_version
     */
    public String getApp_version() {
    	return app_version;
    }

	
    /**
     * @param app_version the app_version to set
     */
    public void setApp_version(String app_version) {
    	this.app_version = app_version;
    }

	
    /**
     * @return the isLogined
     */
    public int getIsLogined() {
    	return isLogined;
    }

	
    /**
     * @param isLogined the isLogined to set
     */
    public void setIsLogined(int isLogined) {
    	this.isLogined = isLogined;
    }

	
    /**
     * @return the currentUid
     */
    public int getCurrentUid() {
    	return currentUid;
    }

	
    /**
     * @param currentUid the currentUid to set
     */
    public void setCurrentUid(int currentUid) {
    	this.currentUid = currentUid;
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

	/* (non-Javadoc)
    * Title: toString
    * Description:
    * @return
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
	    return "ModelApp [_id=" + _id + ", app_version=" + app_version
	            + ", isLogined=" + isLogined + ", currentUid=" + currentUid
	            + ", session=" + session + "]";
    }
	
}