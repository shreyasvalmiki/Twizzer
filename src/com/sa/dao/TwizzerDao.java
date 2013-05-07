package com.sa.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.sa.model.Searched;
import com.sa.model.WeightedTweet;
import com.sa.util.*;
//
public class TwizzerDao {
	public void insertSearch(Searched srch){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(srch);
		}
		finally{
			pm.close();
		}
	}
	//
	public Searched getSearchedResult(String searchLoc, String searchStr, double radius){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Searched.class);
		q.setFilter("searchLoc == paramLoc && searchStr == paramStr && radius == paramRadius");
		q.declareParameters("String paramLoc, String paramStr, double paramRadius");
		@SuppressWarnings("unchecked")
		List<Searched> searches = (List<Searched>)q.execute(searchLoc,searchStr,radius);
		
		if(searches==null || searches.size()<1){
			
			System.out.println("Search NOT in DB");
			
			TwitterHelper feed = new TwitterHelper();
			ArrayList<WeightedTweet> wts = new ArrayList<WeightedTweet>();
			try {
				wts = feed.getRankedTweets(feed.getTweets(searchLoc, radius), searchStr);
			} catch (OAuthMessageSignerException
					| OAuthExpectationFailedException
					| OAuthCommunicationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Searched searched = new Searched();
			searched.setSearchLoc(searchLoc);
			searched.setSearchStr(searchStr);
			searched.setDate(new Date());
			searched.setTweets(wts);
			searched.setRadius(radius);
			insertSearch(searched);
			searched.toString();
			return searched;
		}
		else{
			System.out.println("Search available in DB");
			searches.get(0).toString();
			return searches.get(0);
		}
		
	}
	
}