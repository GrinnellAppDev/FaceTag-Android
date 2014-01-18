package com.facetag.android;

import com.facetag_android.R;
import com.facetag_android.R.layout;
import com.facetag_android.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/* This is where the user will judge a photo submited by someone else in the game */
public class PhotoJudgeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_judge);
	}

}
