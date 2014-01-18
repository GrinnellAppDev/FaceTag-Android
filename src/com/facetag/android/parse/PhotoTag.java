package com.facetag.android.parse;

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
	
	public void setSender(ParseUser sender){
		put("sender", sender);
	}

	public void setConfirmation(int confirmation) {
		put("confirmation", confirmation);
	}
	
	public int getConfirmation(int confirmation) {
		return getInt("confirmation");
	}

	public ParseFile getPhoto() {
		return getParseFile("photo");
	}

	public void setPhoto(ParseFile file) {
		put("photo", file);
	}
	
	public void setGame(Game game) {
		put("game", game);
	}
	
	public Game getGame() {
		return (Game) getParseObject("game");
	}
}
