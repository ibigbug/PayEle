package com.payele.storage;


/**
 * 
 * @ClassName: ModelFeed 
 * @Description: 业界动态Model
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 17, 2014 10:38:35 AM 
 *
 */
public class ModelFeed {
	private int id;
	private String title;
	private String contnet;
	private String author;
	private String datetime;

	public ModelFeed(){}

	
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
     * @return the title
     */
    public String getTitle() {
    	return title;
    }

	
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
    	this.title = title;
    }

	
    /**
     * @return the contnet
     */
    public String getContnet() {
    	return contnet;
    }

	
    /**
     * @param contnet the contnet to set
     */
    public void setContnet(String contnet) {
    	this.contnet = contnet;
    }

	
    /**
     * @return the author
     */
    public String getAuthor() {
    	return author;
    }

	
    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
    	this.author = author;
    }

	
    /**
     * @return the datetime
     */
    public String getDatetime() {
    	return datetime;
    }

	
    /**
     * @param datetime the datetime to set
     */
    public void setDatetime(String datetime) {
    	this.datetime = datetime;
    }


	/* (non-Javadoc)
    * Title: toString
    * Description:
    * @return
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
	    return "ModelFeed [id=" + id + ", title=" + title + ", contnet="
	            + contnet + ", author=" + author + ", datetime=" + datetime
	            + "]";
    }
	
}
