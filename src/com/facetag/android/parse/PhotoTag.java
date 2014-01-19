package com.facetag.android.parse;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("PhotoTag")
public class PhotoTag extends ParseObject {
	public PhotoTag() {
		// A default constructor is required.
	}

	public ParseUser getSender() {
		return getParseUser("sender");
	}

	public void setSender(ParseUser sender) {
		put("sender", sender);
	}

	public ParseFile getPhoto() {
		return getParseFile("photo");
	}

	public void setPhoto(ParseFile file) {
		put("photo", file);
	}

	public void setGame(String game) {
		put("game", game);
	}

	public String getGame() {
		return getString("game");
	}

	public void setConfirmation(int count) {
		put("confirmation", count);

	}

	public int getConfirmation() {
		return getInt("confirmation");
	}
	
	public void setRejection(int count) {
		put("rejection", count);

	}

	public int getRejection() {
		return getInt("rejections");
	}
	
	public void setVotedArray(List<ParseUser> voted){
		put("usersArray", voted);
	}
	
	public List<ParseUser> getVotedArray(){
		return getList("usersArray");
	}
	
	public void setTarget(ParseUser target){
		put("target", target);
	}
	
	public ParseUser getTarget(){
		return getParseUser("target");
	}
	
	public void setThreshold(int thr){
		put("threshold", thr);
	}
	
	public int getThreshold(){
		return getInt("threshold");
	}
	
}
