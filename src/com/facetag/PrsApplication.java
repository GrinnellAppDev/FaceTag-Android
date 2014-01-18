package com.facetag;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PrsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ParseObject.registerSubclass(PrsPhoto.class);

		/*
		 * Fill in this section with your Parse credentials
		 */
		Parse.initialize(this, "VgUmqfwmveatEK77VKqejESUi9g2YTiLd5ARi5Zv", "rZOccIO0y35BMrfv599RyclrNo5QT308WRCLkTnY");

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
