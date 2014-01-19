package com.facetag.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* Pull list of parse games here */
public class GameScreenActivity extends FragmentActivity {
	private final String TAG = "GameScreen";
	public ArrayList<Game> mGameList = new ArrayList<Game>();
	public ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();
	ParseUser mUser;
	public Game mGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		ParseAnalytics.trackAppOpened(getIntent());

		mUser = ParseUser.getCurrentUser();
		if (mUser != null) {
			Toast.makeText(getApplicationContext(),
					"You are signed in as " + mUser.getString("fullName"),
					Toast.LENGTH_SHORT).show();
			loadGames();

		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}

		GameListFragment listfrag = new GameListFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, listfrag).commit();
	}

	public void loadGames() {
		mUser = ParseUser.getCurrentUser();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", mUser.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						mGameList.add(thisGame);
						GameListFragment listfrag = new GameListFragment();
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.fragment_container, listfrag)
								.commit();
					}
				} else
					Log.e(TAG, e.getMessage());
			}
		});
	}
}
