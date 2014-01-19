package com.facetag.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* Pull list of parse games here */
public class GameScreenActivity extends FragmentActivity {
	private final String TAG = "GameScreen";
	public ArrayList<Game> mGameList = new ArrayList<Game>();
	public Game mGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Toast.makeText(
					getApplicationContext(),
					"You are signed in as " + currentUser.getString("fullName"),
					Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", currentUser.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						mGameList.add(thisGame);
					}
					GameListFragment listfrag = new GameListFragment();
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.fragment_container, listfrag)
							.commit();
				} else
					Log.e(TAG, e.getMessage());
			}
		});
	}
}
