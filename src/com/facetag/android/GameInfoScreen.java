package com.facetag.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facetag_android.R;
import com.parse.ParseUser;

public class GameInfoScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_info_screen);
		
		TextView targetInfo = (TextView) findViewById(R.id.target_description);
		ImageView targetPic = (ImageView) findViewById(R.id.target_photo);
		
		targetInfo.setText(ParseUser.getCurrentUser().getString("fullName"));
		
		
		
	}
}
