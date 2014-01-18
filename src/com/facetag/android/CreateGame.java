package com.facetag.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CreateGame extends FragmentActivity {
	List<ParseUser> inviteUsers = new ArrayList<ParseUser>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);

		String[] array = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, array);
		Spinner maxPointsSpinner = (Spinner) findViewById(R.id.points_win);
		maxPointsSpinner.setAdapter(spinnerAdapter);
		
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		
		userQuery.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> users, ParseException e) {
		        if (e == null) {
		            Log.d("Query", "Retrieved " + users.size() + " users");
		            inviteUsers.addAll(users);
		        } else {
		            Log.d("Query", "Error: " + e.getMessage());
		        }
		    }
		});
	}
}
