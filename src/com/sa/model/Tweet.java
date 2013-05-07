package com.sa.model;
//,
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.appengine.api.datastore.Key;
@PersistenceCapable
public class Tweet {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private String tweetId;
	@Persistent
	private String user;
	@Persistent
	private String imgUrl;
	@Persistent
	private String text;
	@Persistent
	private String location;
	
	public Key getId(){
		return this.id;
	}
	public void setId(Key key){
		this.id = key;
	}
	
	public String getTweetId(){
		return this.tweetId;
	}
	public void setTweetId(String tweetId){
		this.tweetId = tweetId;
	}
	
	public String getUser(){
		return this.user;
	}
	public void setUser(String user){
		this.user = user;
	}
	
	public String getImgUrl(){
		return this.imgUrl;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}
	
	public String getText(){
		return this.text;
	}
	public void setText(String text){
		this.text = text;
	}
	
	public String getLocation(){
		return this.location;
	}
	public void setLocation(String location){
		this.location = location;
	}
	
	@Override
	public String toString(){
		String str = "tweetId = "+tweetId+"; ";
		str += "user = "+user+"; ";
		str += "imgUrl = "+imgUrl+"; ";
		str += "location = "+location+"; ";
		str += "text = "+text+"; ";
		return str;
	}
	
}
