package edu.grinnell.facetag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
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
import edu.grinnell.facetag.utils.actionBarFont;

public class GameScreenActivity extends SherlockFragmentActivity {
	private final String TAG = "GameScreen";
	public ArrayList<Game> mGameList = new ArrayList<Game>();
	ParseUser mUser; // the current user
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();
	ArrayList<scorePair> mScoreList = new ArrayList<scorePair>();
	ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();
	HashMap<String, ArrayList<PhotoTag>> photoMap = new HashMap<String, ArrayList<PhotoTag>>();
	public Game mGame;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		actionBarFont.fontChange(this.getWindow().getDecorView(), this);
		setContentView(R.layout.activity_game_screen);
		 
		
		//getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_action));
	
		
	 if (isNetworkNotAvailable()){
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(GameScreenActivity.this);
			builder.setMessage("Please make sure you are connected to the internet!")
				.setTitle("Error")
				.setPositiveButton(android.R.string.ok,null);
			
			AlertDialog dialog = builder.create();
			dialog.show();
			
		} else {
		
			ParseAnalytics.trackAppOpened(getIntent());
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		// Get the user object for the current user
		mUser = ParseUser.getCurrentUser();
		if (mUser != null) {
			Toast.makeText(getApplicationContext(),
					"You are signed in as " + mUser.getString("fullName"), Toast.LENGTH_SHORT)
					.show();
		} else {
			// If not logged in, send to login
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		}
	}

	public void downloadGames() {
		// Retrieve all games that current user is in
		setProgressBarIndeterminateVisibility(true);
		mUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", mUser.getObjectId());
		query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				ArrayList<String> gameIds = new ArrayList<String>();
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						mGameList.add(thisGame);
						gameIds.add(thisGame.getObjectId());
					}
					downloadPhotos(gameIds);
				} else
					Log.e(TAG, e.getMessage());
			}
		});
	}

	public void downloadPhotos(ArrayList<String> gameIds) {
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
				//Load list fragment to display downloaded game data
				GameListFragment listfrag = new GameListFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, listfrag).commit();
			}
		});
	}
	
	@Override
	protected void onResume() {
		 if (isNetworkNotAvailable()){
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(GameScreenActivity.this);
				builder.setMessage("Please make sure you are connected to the internet!")
					.setTitle("Error")
					.setPositiveButton(android.R.string.ok,null);
				
				AlertDialog dialog = builder.create();
				dialog.show();
				super.onResume();
				
			} else {
        mGameList.clear();		
		downloadGames();
		getSupportActionBar().setTitle("FaceTag");
		super.onResume();
			}
	}
	
	public boolean isNetworkNotAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable = true;
		if (networkInfo != null && networkInfo.isConnected()) {
			
			isAvailable = false;
		}
		return isAvailable;
	}
}
