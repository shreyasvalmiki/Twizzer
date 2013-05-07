package com.sa.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
//// 
@PersistenceCapable
public class WeightedTweet {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private Tweet tweet;
	@Persistent
	private Integer weight;
	@Persistent
	private Integer sentiment;
	
	
	public Key getId(){
		return this.id;
	}
	public void setId(Key key){
		this.id = key;
	}
	public Tweet getTweet(){
		return this.tweet;
	}
	public void setTweet(Tweet tweet){
		this.tweet = tweet;
	}
	
	public Integer getWeight(){
		return this.weight;
	}
	public void setWeight(Integer weight){
		this.weight = weight;
	}
	
	public Integer getSentiment(){
		return this.sentiment;
	}
	public void setSentiment(Integer sentiment){
		this.sentiment = sentiment;
	}
	
	@Override
	public String toString(){
		System.out.println("********WeightedTweet********");
		System.out.println("tweet: "+tweet.toString());
		System.out.println("weight: "+weight);
		System.out.println("sentiment: "+sentiment);
		System.out.println("********WeightedTweet********");
		return "tweet: "+tweet.toString()+"; weight: "+weight+"; sentiment: "+sentiment+"; ";
	}
	
	
}
