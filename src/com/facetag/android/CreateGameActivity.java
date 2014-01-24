package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

// Creates a Games
//TODO Allow user to invite specific friends
	// use a new list fragment
public class CreateGameActivity extends SherlockActivity implements
		OnItemSelectedListener {
	private final String TAG = "Create Game";
	List<String> participants = new ArrayList<String>();
	int maxPoints = 5;
	ParseUser mUser = ParseUser.getCurrentUser();
	HashMap<String,Integer> scoreBoard = new HashMap<String,Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		//Init score max spinner
		String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);
		Spinner maxPointsSpinner = (Spinner) findViewById(R.id.points_win);
		maxPointsSpinner.setAdapter(spinnerAdapter);
		maxPointsSpinner.setOnItemSelectedListener(this);

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

		//Add alternate way to select friends to invite
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

	//Item selector for spinner
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
			maxPoints = pos + 1;
	}

	public void onNothingSelected(AdapterView<?> parent) {
		//Default max points is 5
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
				EditText nameField = (EditText) findViewById(R.id.name_field);
				String inputName = nameField.getText().toString();
				
				if (inputName.length() == 0)	
					newGame.setName(mUser.getString("firstName") + "'s game");
				else
					newGame.setName(inputName);
				
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
