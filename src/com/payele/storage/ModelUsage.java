package com.payele.storage;

import android.content.ContentValues;

/**
 * 
 * @ClassName: ModelUsage 
 * @Description: Usage Model
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 31, 2014 11:43:33 AM 
 *
 */
public class ModelUsage {
	public int id;
	public String date;
	public int usage;
	
	public ModelUsage(ContentValues cv) {
		this.date = cv.getAsString("date");
		this.usage = cv.getAsInteger("usage");
	}

	
    public ModelUsage() {
	    // TODO Auto-generated constructor stub
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
     * @return the date
     */
    public String getDate() {
    	return date;
    }

	
    /**
     * @param date the date to set
     */
    public void setDate(String date) {
    	this.date = date;
    }

	
    /**
     * @return the usage
     */
    public int getUsage() {
    	return usage;
    }

	
    /**
     * @param usage the usage to set
     */
    public void setUsage(int usage) {
    	this.usage = usage;
    }


	/* (non-Javadoc)
    * Title: toString
    * Description:
    * @return
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
	    return "ModelUsage [id=" + id + ", date=" + date + ", usage=" + usage
	            + "]";
    }
	
}
