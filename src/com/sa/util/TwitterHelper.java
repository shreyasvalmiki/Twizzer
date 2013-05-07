package com.sa.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.*;
import com.sa.model.*;
// 
public class TwitterHelper {

	static String accessToken = "60617145-Ie90EshbEfeXh4QJIspFf8u5PfoL8BCpZlhZpbVRg";
	static String accessSecret = "mER3AAEJdYk9keUXd3Ti1ShSTYZRr6VySI63BKU";
	static String consumerKey = "5NmkLd1R57zYoIik8vXc3g";
	static String consumerSecret = "1t1mVnzh0MB5VziwEEr14Oc6VotZxoR27mE1XnPQOS8";
	OAuthConsumer consumer;

	private double lat = 0;
	private double lng = 0;
	private String locString = "";
	public TwitterHelper() {
		consumer = new DefaultOAuthConsumer(consumerKey,consumerSecret);
		consumer.setTokenWithSecret(accessToken, accessSecret);
	}

	public ArrayList<Tweet> getTweets(String locString,double radius)throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException{
		this.locString = locString;
		setCoordinates();
		//URL url = new URL("http://search.twitter.com/search.json?geocode="+lat+","+lng+","+radius+"mi");
		URL url = new URL("https://api.twitter.com/1.1/search/tweets.json?geocode="+lat+","+lng+","+radius+"mi");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);
		InputStream is = request.getInputStream();

		StringBuilder sb = readFromInputStream(is);

		System.out.println(sb.toString());
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		try {
			JSONObject json = new JSONObject(sb.toString());
			JSONArray results = json.getJSONArray("statuses");

			for(int i=0;i<results.length();i++){
				Tweet tweet = new Tweet();
				JSONObject res = results.getJSONObject(i);
				//Date date = new Date();
				JSONObject user = res.getJSONObject("user");
				//tweet.setCreDtTime()
				tweet.setUser(user.getString("screen_name"));
				tweet.setTweetId(res.getString("id_str"));
				tweet.setLocation(res.getString("place"));
				tweet.setImgUrl(user.getString("profile_image_url"));
				tweet.setText(res.getString("text"));
				tweets.add(tweet);
				System.out.println(tweet.toString());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tweets;
	}

	private void setCoordinates() throws IOException{
		
		System.out.println(locString);

		URL geoURL = new URL("http://maps.google.com/maps/api/geocode/json?address="+locString+"&sensor=true");
		HttpURLConnection geoReq = (HttpURLConnection) geoURL.openConnection();

		InputStream geoIs = geoReq.getInputStream();

		StringBuilder geoSb = new StringBuilder();
		geoSb = readFromInputStream(geoIs);

		try {

			JSONObject jsonObj = new JSONObject(geoSb.toString());
			JSONArray results = jsonObj.getJSONArray("results");
			JSONObject geom = results.getJSONObject(0).getJSONObject("geometry");
			JSONObject loc = geom.getJSONObject("location");

			lat = loc.getDouble("lat");
			lng = loc.getDouble("lng");
			System.out.println(lat);
			System.out.println(lng);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private StringBuilder readFromInputStream(InputStream is){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
	}


	public ArrayList<WeightedTweet> getRankedTweets(ArrayList<Tweet> tweets,String search){
		ArrayList<WeightedTweet> weightedTweets = new ArrayList<WeightedTweet>();

		for(Tweet tweet: tweets){
			WeightedTweet wt = new WeightedTweet();
			wt.setTweet(tweet);
			wt.setWeight(0);
			weightedTweets.add(wt);
		}

		String[] splits = search.split("\\s");
		String currentSearch = "";
		for(int i=0;i<splits.length;i++){
			currentSearch += " " +  splits[i];
			currentSearch = currentSearch.trim().toLowerCase();
			for(WeightedTweet wt:weightedTweets){
				String tweetTxt = wt.getTweet().getText().toLowerCase();
				int countCmb = StringUtils.countMatches(tweetTxt,currentSearch);
				int countOne = StringUtils.countMatches(tweetTxt,splits[i]);
				int total = wt.getWeight();
				total += countCmb*3 + countOne*2;
				wt.setWeight(total);
			}
		}
		Collections.sort(weightedTweets,new TweetWeightCompare());
		setTweetSentiment(weightedTweets);
		return weightedTweets;
	}


	private void setTweetSentiment(ArrayList<WeightedTweet> rankedTweets){

		for(WeightedTweet wt:rankedTweets){
			String tweetText = wt.getTweet().getText().toLowerCase();
			int sentiment = 0;
			String[] splits = tweetText.split("[\\s,!?.;:&\"']");
			for(int i=0;i<splits.length;i++){
				sentiment += getSentiment(splits[i]);
			}
			wt.setSentiment(sentiment);
		}
	}

	private HashMap<String,Integer> getSentimentMap(){
		BufferedReader br;
		HashMap<String,Integer> sentimentMap = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new FileReader("WEB-INF/sentiments.txt"));

			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split("\\t");
				//System.out.println("Word: "+split[0]+" Sentiment:"+split[1]);
				sentimentMap.put(split[0], Integer.parseInt(split[1]));
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentimentMap;

	}
	
	private int getSentiment(String search){
		HashMap<String,Integer> sentimentMap = getSentimentMap();
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(sentimentMap.keySet());
		String item = binarySearch(list,search,0,list.size()-1);
		if(!item.equals("")){
			return sentimentMap.get(item);
		}
		else
			return 0;
	}

	private String binarySearch(ArrayList<String> list,String search,int low, int high){
		
		if(high<low){
			return "";
		}
		else{
			int mid = (high+low)/2;
			if(list.get(mid).startsWith(search)){
				return list.get(mid);
			}
			else if(search.compareToIgnoreCase(list.get(mid)) > 0){
				return binarySearch(list,search,mid+1,high);
			}
			else{
				return binarySearch(list,search,low,mid-1);
			}
		}
	}
}
