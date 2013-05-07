var avgSentiment = 0;
$(function(){
	
	function getSentimentString(val){
		if(val == 0){
			return "Neutral";
		}
		else if(val > 0 && val <= 3){
			return "Positive";
		}
		else if(val > 3 && val <= 10){
			return "Very Positive";
		}
		else if(val > 10){
			return "Unbelievably Positive";
		}
		else if(val < 0 && val >= -3){
			return "Negative";
		}
		else if(val < -3 && val >= -10){
			return "Very Negative";
		}
		else{
			return "Unbelievably Negative";
		}
	}
	
	function buildList(objList){
		
		var listItems = "";
		for(var i=0;i<objList.length;i++){
			listItems += '<li style="display:block;"><p><b>Sentiment: </b>'+getSentimentString(objList[i].sentiment)+'</p>';
			listItems += '<img style="float:left;padding:10px;" src="'+objList[i].tweet.imgUrl+'"></img>';
			listItems += '<p><b>Username: </b>'+objList[i].tweet.user+'</p>';
			listItems += '<p><b>Text: </b>'+objList[i].tweet.text+'</p>';
			listItems += '<hr></hr></li>';
			avgSentiment += objList[i].sentiment;
		}
		
		avgSentiment = avgSentiment/objList.length;
		//alert(avgSentiment);
		return listItems;
		
	}
	
	
	$("#btn").on("click",function(){
		$("#listcontainer").hide('slide', {direction: 'left'}, 1000);
		$(".wait").fadeIn(1200);
		var search = $.trim($("#search").val()).toLowerCase();
		var loc = $.trim($("#location").val()).toLowerCase();
		var radius = $("#radius").val();
		
		//alert(radius);
		if(search == "" || loc == ""){
			alert("All fields are mandatory.");
		}
		else{
			loc = loc.replace(/\s/g,"+");
			$.ajax({
				url:"twizzerserv",
				type: "GET",
				data:{
					location:loc,
					search:search,
					radius:radius
				},
				success: function(out){
					$(".dateval").html(out.date);
					$(".locval").html(out.searchLoc);
					$(".radval").html(out.radius);
					$(".searchval").html(out.searchStr);
					$("#tweets").html(buildList(out.wts));
					
					$("#avgsentiment").html(getSentimentString(avgSentiment));
					$(".wait").fadeOut();
					$("#listcontainer").show('slide', {direction: 'left'}, 1000);
				}
			});
		}
		
	});
	
	$('document').ready(function(){
		$("#listcontainer").hide();
		$(".wait").hide();
	});
	
});












