package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CreateGame extends Activity implements
		OnItemSelectedListener {
	private final String TAG = "Create Game";
	List<String> participants = new ArrayList<String>();
	int maxPoints = 5;
	ParseUser mUser = ParseUser.getCurrentUser();
	String name = mUser.getString("firstName") + "'s game";
	HashMap<String,Integer> scoreBoard = new HashMap<String,Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);

		String[] array = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);
		Spinner maxPointsSpinner = (Spinner) findViewById(R.id.points_win);
		maxPointsSpinner.setAdapter(spinnerAdapter);
		maxPointsSpinner.setOnItemSelectedListener(this);

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

		userQuery.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> users, ParseException e) {
				if (e == null) {
					scoreBoard.put(mUser.getObjectId(), 0);
					participants.add(mUser.getObjectId());
					Log.d("Query", "Retrieved " + users.size() + " users");
					for (int i = 0; i < users.size(); i++){
						participants.add(users.get(i).getObjectId());
						scoreBoard.put(users.get(i).getObjectId(), 0);
					}
					Log.i(TAG, scoreBoard.toString());
				} else {
					Log.d("Query", "Error: " + e.getMessage());
				}
			}
		});
		
		setSubmitButton();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		maxPoints = pos + 1;

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
		maxPoints = 5;
	}

	public void setSubmitButton() {
		// Add a listener to the Capture button
		Button flashButton = (Button) findViewById(R.id.submit);
		flashButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Game newGame = new Game();
				newGame.setScoreBoard(scoreBoard);
				newGame.setName(ParseUser.getCurrentUser().getString("firstName") + "'s game");
				newGame.setParticipants(participants);
				newGame.setPointsToWin(maxPoints);
				newGame.setTimePerTurn(20);
				newGame.saveInBackground();
				Toast.makeText(getApplicationContext(), "Game Created: " + newGame.getName(), 
						   Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
