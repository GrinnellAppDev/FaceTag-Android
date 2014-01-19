package com.facetag.android;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* Pull list of parse games here */
public class GameListScreen extends ListActivity {
	private final String TAG = "GameScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		ParseUser user = ParseUser.getCurrentUser();
		TextView testText = (TextView) findViewById(R.id.test_text);

		testText.setText(user.getString("fullName"));
		

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", ParseUser.getCurrentUser().getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			  public void done(List<ParseObject> results, ParseException e) {
				  Log.i(TAG, results.get(0).getString("name"));
			  }
			});
		
		// ListView listView = (ListView) findViewById(R.id.list);
		// ArrayAdapter adapter = new
		// ArrayAdapter(android.R.layout.simple_list_item_1)

		Button browseButton = (Button) findViewById(R.id.create_game);
		browseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createGame();
			}
		});
		
		Button detailButton = (Button) findViewById(R.id.gameDetail);
		detailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gameDetail();
			}
		});
	}

	public void createGame() {
		Intent intent = new Intent(this, CreateGame.class);
		startActivity(intent);
	}

	public void gameDetail() {
		Intent intent = new Intent(this, GameInfoScreen.class);
		startActivity(intent);
	}

}
