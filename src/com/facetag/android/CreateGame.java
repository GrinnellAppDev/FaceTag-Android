package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CreateGame extends FragmentActivity implements
		OnItemSelectedListener {
	ArrayList<ParseUser> participants = new ArrayList<ParseUser>();
	int maxPoints = 5;
	String name;
	HashMap<String,Integer> scoreBoard;
	
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
					Log.d("Query", "Retrieved " + users.size() + " users");
					participants.addAll(users);
					for (int i = 0; i < participants.size(); i++){
						scoreBoard.put(participants.get(i).getUsername(), 0);
					}
				} else {
					Log.d("Query", "Error: " + e.getMessage());
				}
			}
		});
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
				//submit new game to parse
			}
		});
	}
}
