package com.sa.util;
// 
import java.util.Comparator;
import com.sa.model.WeightedTweet;

public class TweetWeightCompare implements Comparator<WeightedTweet>{
	@Override
	public int compare(WeightedTweet a, WeightedTweet b){
		return (b.getWeight()>a.getWeight()?1:(b.getWeight()==a.getWeight()?0:-1));
	}
}
