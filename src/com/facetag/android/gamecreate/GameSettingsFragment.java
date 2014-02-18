package com.facetag.android.gamecreate;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameSettingsFragment extends SherlockFragment implements
OnItemSelectedListener {

	final String TAG = "Invite Screen";
	CreateGameActivity mActivity;
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();
	ListView mListView;
	View fragView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (CreateGameActivity) getSherlockActivity();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragView = inflater.inflate(R.layout.fragment_game_settings, container,
				false);

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Init score max spinner
		String[] array = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_item, array);
		Spinner maxPointsSpinner = (Spinner) fragView.findViewById(R.id.points_win);
		maxPointsSpinner.setAdapter(spinnerAdapter);
		maxPointsSpinner.setOnItemSelectedListener(this);

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

		setSubmitButton();

		Button inviteButton = (Button) fragView.findViewById(R.id.invite_button);
		inviteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InvitePlayersFragment inviteFrag = new InvitePlayersFragment();
				mActivity.getSupportFragmentManager().beginTransaction()
						.add(R.id.create_fragment_container, inviteFrag)
						.addToBackStack(TAG).commit();
			}
		});

		return fragView;
	}

	public class ScoreListAdapter extends ArrayAdapter<ParseUser> {
		private final Context context;
		private final ArrayList<ParseUser> users;
		int layoutResourceId;

		public ScoreListAdapter(Context context, int layoutResourceId,
				ArrayList<ParseUser> users) {
			super(context, layoutResourceId, users);
			this.context = context;
			this.users = users;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(layoutResourceId, parent, false);

			TextView nameText = (TextView) rowView.findViewById(R.id.invitee);
			nameText.setText(users.get(position).getUsername());

			return rowView;
		}
	}

	// Item selector for spinner
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		mActivity.maxPoints = pos + 1;
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Default max points is 5
		mActivity.maxPoints = 5;
	}

	public void setSubmitButton() {
		// Add a listener to the Capture button
		Button flashButton = (Button) fragView.findViewById(R.id.submit);
		flashButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Game newGame = new Game();
				newGame.setScoreBoard(mActivity.scoreBoard);
				EditText nameField = (EditText) fragView.findViewById(R.id.name_field);
				String inputName = nameField.getText().toString();

				if (inputName.length() == 0)
					newGame.setName(mActivity.mUser.getString("firstName") + "'s game");
				else
					newGame.setName(inputName);

				newGame.setParticipants(mActivity.participants);
				newGame.setPointsToWin(mActivity.maxPoints);
				newGame.setTimePerTurn(20);
				newGame.saveInBackground();
				Toast.makeText(mActivity.getApplicationContext(),
						"Game Created: " + newGame.getName(),
						Toast.LENGTH_SHORT).show();
				mActivity.finish();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(mActivity);
			return true;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}
}
