package edu.grinnell.facetag.parse;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser{
	public User() {
		// A default constructor is required.
	}
	
	public String getEmail() {
		return getString("email");
	}
	
	public void setEmail(String email) {
		put("email", email);
	}
	
	public String getFacebookId() {
		return getString("facebookId");
	}
	
	public void setFacebookId(String fbId) {
		put("facebookId", fbId);
	}
	
	public String getFirstName() {
		return getString("firstName");
	}
	
	public void setFirstName(String firstname) {
		put("firstName", firstname);
	}
	
	public String getLastName() {
		return getString("lastName");
	}
	
	public void setLastName(String lastname) {
		put("lastName", lastname);
	}
	
	public String getFullName() {
		return getString("firstName");
	}
	
	public void setFullName(String name) {
		put("fullName", name);
	}
	
	public String getProfPicURL() {
		return getString("profilePictureURL");
	}
	
	public void setProfPicURL(String url) {
		put("profilePictureURL", url);
	}
	
}
