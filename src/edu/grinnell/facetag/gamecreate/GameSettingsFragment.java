package edu.grinnell.facetag.gamecreate;

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
import com.facetag_android.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.grinnell.facetag.parse.Game;

public class GameSettingsFragment extends SherlockFragment implements OnItemSelectedListener {

	final String TAG = "Invite Screen";
	CreateGameActivity mActivity;
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();
	ListView mListView;
	View fragView;
	TextView mMaxPointsView, mMaxTimeView;
	Boolean inviteShowing = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (CreateGameActivity) getSherlockActivity();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragView = inflater.inflate(R.layout.fragment_game_settings, container, false);

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/*// Init score max spinner
		String[] arrayScore = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		ArrayAdapter<String> spinnerScoreAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_spinner_dropdown_item, arrayScore);
		Spinner maxPointsSpinner = (Spinner) fragView.findViewById(R.id.points_win);
		maxPointsSpinner.setAdapter(spinnerScoreAdapter);
		maxPointsSpinner.setOnItemSelectedListener(this);*/
		
		// spinner for maxTime
	/*	String[] arrayTime = {"2 hrs", "5 hrs", "9 hrs", "16 hrs", "24 hrs"};
		ArrayAdapter<String> spinnerTimerAdapter = new ArrayAdapter<String>(mActivity, 
				android.R.layout.simple_spinner_dropdown_item, arrayTime);
		Spinner timeRoundSpinner = (Spinner) fragView.findViewById(R.id.round_time);
		timeRoundSpinner.setAdapter(spinnerTimerAdapter);
		timeRoundSpinner.setOnItemSelectedListener(this);*/
		
		mMaxPointsView = (TextView) fragView.findViewById(R.id.max_points);
		mMaxTimeView = (TextView) fragView.findViewById(R.id.max_time);
		

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

		setSubmitButton();

		Button inviteButton = (Button) fragView.findViewById(R.id.invite_button);
		inviteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (inviteShowing) {
					inviteShowing = false;
					mActivity.getSupportFragmentManager().popBackStack();
				} else {
					inviteShowing = true;
					InvitePlayersFragment inviteFrag = new InvitePlayersFragment();
					mActivity
							.getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.left_slide_in, R.anim.left_slide_out,
									R.anim.right_slide_in, R.anim.right_slide_out)
							.add(R.id.create_fragment_container, inviteFrag).addToBackStack(TAG)
							.commit();
				}
			}
		});

		return fragView;
	}

	public class ScoreListAdapter extends ArrayAdapter<ParseUser> {
		private final Context context;
		private final ArrayList<ParseUser> users;
		int layoutResourceId;

		public ScoreListAdapter(Context context, int layoutResourceId, ArrayList<ParseUser> users) {
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	
		
		
	}

	
	public void onNothingSelected(AdapterView<?> parent) {

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
				mActivity.maxPoints= Integer.parseInt(mMaxPointsView.getText().toString());
				mActivity.maxTime= Integer.parseInt(mMaxTimeView.getText().toString());
					
				if (inputName.length() == 0)
					newGame.setName(mActivity.mUser.getString("firstName") + "'s game");
				else
					newGame.setName(inputName);

				newGame.setParticipants(mActivity.participants);
				newGame.setPointsToWin(mActivity.maxPoints);
				newGame.setTimePerTurn(mActivity.maxTime);
				newGame.saveInBackground();
				Toast.makeText(mActivity.getApplicationContext(),
						"Game Created: " + newGame.getName(), Toast.LENGTH_SHORT).show();
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
