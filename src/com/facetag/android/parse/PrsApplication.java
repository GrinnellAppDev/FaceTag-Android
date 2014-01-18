package com.facetag.android.parse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PrsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ParseObject.registerSubclass(PhotoTag.class);
		ParseObject.registerSubclass(Game.class);

		/*
		 * Fill in this section with your Parse credentials
		 */
		Parse.initialize(this, "97s1dXeGVg72YmhWjZVFXQvWFILwyyV78pftvQBe", "isrQzRX1HkVQyuqnab3m8DGEIWWcpKAt2iJ0CtFu");
		ParseFacebookUtils.initialize("551206871642297");
		
		// annonymous user
		ParseUser.enableAutomaticUser();

		/*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
		ParseACL defaultACL = new ParseACL();

		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}

}
