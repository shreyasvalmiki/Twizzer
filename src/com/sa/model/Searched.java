package com.sa.model;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
//comment
@PersistenceCapable
public class Searched {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private String searchLoc;
	@Persistent
	private String searchStr;
	@Persistent
	private ArrayList<WeightedTweet> wts;
	@Persistent
	private double radius;
	@Persistent
	private Date date;
	
	public Key getId(){
		return this.id;
	}
	public void setId(Key id){
		this.id = id;
	}
	
	public ArrayList<WeightedTweet> getWts(){
		return this.wts;
	}
	public void setTweets(ArrayList<WeightedTweet> wts){
		this.wts = wts;
	}
	
	public String getSearchStr(){
		return this.searchStr;
	}
	public void setSearchStr(String searchStr){
		this.searchStr = searchStr;
	}

	public String getSearchLoc(){
		return this.searchLoc;
	}
	public void setSearchLoc(String searchLoc){
		this.searchLoc = searchLoc;
	}
	
	public double getRadius(){
		return this.radius;
	}
	public void setRadius(double radius){
		this.radius = radius;
	}
	
	public Date getDate(){
		return this.date;
	}
	public void setDate(Date date){
		this.date = date;
	}
	
	@Override
	public String toString(){
		System.out.println("date: "+date.toString());
		System.out.println("searchStr: "+searchStr);
		System.out.println("searchLoc: "+searchLoc);
		System.out.println("radius: "+radius);
		for(WeightedTweet wt:wts){
			wt.toString();
		}
		return "Created "+date.toString();
	}
	
}
