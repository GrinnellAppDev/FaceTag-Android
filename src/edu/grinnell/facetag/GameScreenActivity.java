package edu.grinnell.facetag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.grinnell.facetag.ScoresListFragment.scorePair;
import edu.grinnell.facetag.parse.Game;
import edu.grinnell.facetag.parse.PhotoTag;

public class GameScreenActivity extends SherlockFragmentActivity {
	private final String TAG = "GameScreen";
	public ArrayList<Game> mGameList = new ArrayList<Game>();
	ParseUser mUser; // the current user
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();
	ArrayList<scorePair> mScoreList = new ArrayList<scorePair>();
	HashMap<String, ArrayList<PhotoTag>> photoMap = new HashMap<String, ArrayList<PhotoTag>>();
	public Game mGame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		ParseAnalytics.trackAppOpened(getIntent());
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		// Get the user object for the current user
		mUser = ParseUser.getCurrentUser();
		if (mUser != null) {
			Toast.makeText(getApplicationContext(),
					"You are signed in as " + mUser.getString("fullName"), Toast.LENGTH_SHORT)
					.show();
			loadGames();
		} else {
			// If not logged in, send to login
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	public void loadGames() {
		// Retrieve all games that current user is in
		mUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", mUser.getObjectId());
		query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				ArrayList<String> gameIds = new ArrayList<String>();
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						mGameList.add(thisGame);
						gameIds.add(thisGame.getObjectId());
					}
					// Retrieve the photos for each game
					ParseQuery<ParseObject> pic_query = ParseQuery.getQuery("PhotoTag");
					pic_query.whereContainedIn("game", gameIds);
					pic_query.whereNotEqualTo("usersArray", mUser);
					pic_query.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> pictureList, ParseException e) {
							if (e == null) {
								ArrayList<PhotoTag> gamePhotos = new ArrayList<PhotoTag>();
								for (int i = 0; i < pictureList.size(); i++) {
									PhotoTag thisPic = (PhotoTag) pictureList.get(i);
									String gameID = thisPic.getGame();
									gamePhotos.clear();
									if (photoMap.containsKey(gameID)) {
										gamePhotos = photoMap.get(gameID);
										gamePhotos.add(thisPic);
										photoMap.put(gameID, gamePhotos);
									} else {
										gamePhotos.add(thisPic);
										photoMap.put(gameID, gamePhotos);
									}
								}
							} else {
								Log.d("score", "Error: " + e.getMessage());
							}
							GameListFragment listfrag = new GameListFragment();
							getSupportFragmentManager().beginTransaction()
									.replace(R.id.fragment_container, listfrag).commit();
						}
					});
				} else
					Log.e(TAG, e.getMessage());
			}
		});
	}
}
