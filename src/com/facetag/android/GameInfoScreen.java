package com.facetag.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameInfoScreen extends Activity {
	ParseUser mUser = ParseUser.getCurrentUser();
	Game mGame;
	final String TAG = "Game Info Screen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_info_screen);

		Intent intent = getIntent();
		String gameId = intent.getStringExtra("id");

		TextView targetInfo = (TextView) findViewById(R.id.target_description);
		ImageView targetPic = (ImageView) findViewById(R.id.target_photo);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("objectId", gameId);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Log.d(TAG, "The getFirst request failed.");
				} else {
					mGame = (Game) object;
					TextView gameName = (TextView) findViewById(R.id.game_name);
					gameName.setText(mGame.getName());
				}
			}
		});

		ImageLoader.getInstance().displayImage(
				mUser.getString("profilePictureURL"), targetPic);
	}

	public void judgePhotos() {

	}
}
