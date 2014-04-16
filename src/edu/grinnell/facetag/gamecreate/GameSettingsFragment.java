package edu.grinnell.facetag.gamecreate;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.FrameLayout;
import android.widget.ListView;
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
	Button flashButton;
	

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
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
		

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
		
		//Change fonts
		TextView inviteHeadText = (TextView) fragView.findViewById(R.id.Invite_friends);
		inviteHeadText.setTypeface(tf);
		
		TextView inviteText = (TextView) fragView.findViewById(R.id.invite_Friends_textView);
		inviteText.setTypeface(tf);
		
		TextView settingsText = (TextView) fragView.findViewById(R.id.settings_textview);
		settingsText.setTypeface(tf);
		
		TextView namegameText = (TextView) fragView.findViewById(R.id.game_name);
		namegameText.setTypeface(tf);
		
		TextView pointsText = (TextView) fragView.findViewById(R.id.points_textview);
		pointsText.setTypeface(tf);
		
		TextView timeText = (TextView) fragView.findViewById(R.id.time_textview);
		timeText.setTypeface(tf);
		
		mMaxPointsView = (TextView) fragView.findViewById(R.id.max_points);
		mMaxTimeView = (TextView) fragView.findViewById(R.id.max_time);
		
	
		
		flashButton = (Button) fragView.findViewById(R.id.submit);
		flashButton.setTypeface(tf);

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

		setSubmitButton();

		FrameLayout inviteButton = (FrameLayout) fragView.findViewById(R.id.frame_invite);
		inviteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					mActivity.getSlidingMenu().toggle();
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
		flashButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {			
				//If no friends are invited
				if (mActivity.participants.size() < 2){

					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Please invite a friend to create a game.")
						.setTitle("Ooops!")
						.setPositiveButton(android.R.string.ok,null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
					
					
				} else {
				Game newGame = new Game();
				newGame.setScoreBoard(mActivity.scoreBoard);
				EditText nameField = (EditText) fragView.findViewById(R.id.name_field);
				String inputName = nameField.getText().toString();
				
				if (mMaxPointsView.getText().toString().equals(""))
					mActivity.maxPoints = 5;
				else
					mActivity.maxPoints= Integer.parseInt(mMaxPointsView.getText().toString());
				
				if (mMaxTimeView.getText().toString().equals(""))
					mActivity.maxTime=10;
				else
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
