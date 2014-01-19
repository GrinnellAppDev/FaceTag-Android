package com.facetag.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseUser;


public class GameInfoScreen extends Activity {
	ParseUser mUser = ParseUser.getCurrentUser();
	Game mGame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_info_screen);
		
		TextView targetInfo = (TextView) findViewById(R.id.target_description);
		ImageView targetPic = (ImageView) findViewById(R.id.target_photo);
		
		targetInfo.setText("Your Target Is: " + mUser.getString("fullName"));
		
		ImageLoader.getInstance().displayImage(mUser.getString("profilePictureURL"), targetPic);	
	}
	
	public void judgePhotos(){
		
	}
}
