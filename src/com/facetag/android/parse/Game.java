package com.facetag.android.parse;

import java.util.HashMap;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Game")
public class Game extends ParseObject{
	public Game() {
		// A default constructor is required.
	}
	
	public String getName() {
		return getString("name");
	}
	
	public void setName(String name) {
		put("name", name);
	}
	
	public List<String> getParticipants(){
		return getList("participants");
	}
	
	public void setParticipants(List<String> participants){
		put("participants", participants);
	}

	public void setPointsToWin(int ptw) {
		put("pointsToWin", ptw);
	}
	
	public int getPointsToWin() {
		return getInt("pointsToWin");
	}

	public void setTimePerTurn(int tpt) {
		put("timePerTurn", tpt);
	}
	
	public int getTimePerTurn() {
		return getInt("timePerTurn");
	}
	
	public void setScoreBoard(HashMap<String,Integer> scoreBoard) {
		put("scoreboard", scoreBoard);
	}
	
	public HashMap<String,Integer>  getScoreBoard() {
		return (HashMap<String, Integer>) get("scoreboard");
	}
	
	public void setUnconfirmedPhotoTags(List<PhotoTag> tags){
		put("unconfirmedPhotoTags", tags);
	}
	
	public List<PhotoTag> getUnconfirmedPhotoTags(){
		return getList("unconfirmedPhotoTags");
	}
	
	public HashMap<String, String> getPairings(){
		return (HashMap<String, String>) get("pairings");
	}
}
