package edu.grinnell.facetag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.grinnell.facetag.parse.Game;
import edu.grinnell.facetag.parse.PhotoTag;
import edu.grinnell.facetag.takepicture.CameraActivity;
import edu.grinnell.facetag.utils.ImageLoaderUtility;

/**
 * 
 * Show the target user and give buttons for camera and eval photos
 * 
 */
public class GameInfoFragment extends SherlockFragment {
	ParseUser mUser = ParseUser.getCurrentUser();
	ParseUser mTarget;
	Game mGame;
	View mView;

	ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();
	final String TAG = "Game Info Screen";
	GameScreenActivity mActivity;
	Boolean scoresShowing = false;

	TextView targetInfo;
	ImageView targetPic;
	TextView gameName;
	TextView newPics;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_game_info, container, false);

		mActivity = (GameScreenActivity) getSherlockActivity();
		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mGame = mActivity.mGame;
		setHasOptionsMenu(true);

		// Find views
		targetInfo = (TextView) mView.findViewById(R.id.target_description);
		// TODO replace this with animation
		targetPic = (ImageView) mView.findViewById(R.id.target_photo);

		gameName = (TextView) mView.findViewById(R.id.game_name);

		gameName.setText("Game: " + mGame.getName());

		getTarget();

		return mView;
	}

	// Retrieves the target for this game, calls getUsers when finished
	void getTarget() {
		// Find Target
		HashMap<String, String> pairings = mGame.getPairings();
		String targetID = pairings.get(mUser.getObjectId());
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.whereEqualTo("objectId", targetID);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser user, ParseException e) {
				getUsers();
				if (e != null) {
					Log.e(TAG, e.getMessage());
				} else if (user == null) {
					Log.d(TAG, "The getFirst request failed.");
				} else {
					Log.d(TAG, "Target: " + user.getString("fullName"));
					mTarget = user;
					// Set target info in view
					targetInfo.setText("Target: " + mTarget.getString("fullName"));
					// Load target FB image
					ImageLoaderUtility imageLoader = new ImageLoaderUtility();
					imageLoader.loadImage(mTarget.getString("profilePictureURL"), targetPic,
							mActivity);
				}
			}
		});
	}

	// Retrieve the users for the game
	void getUsers() {
		// Fetch the full names and score for each player
		mActivity.mUsers.clear();
		ArrayList<String> players = (ArrayList<String>) mActivity.mGame.getParticipants();
		ParseQuery<ParseUser> scoreQuery = ParseUser.getQuery();
		scoreQuery.whereContainedIn("objectId", players);
		scoreQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		scoreQuery.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> users, ParseException e) {
				if (e == null) {
					mActivity.mUsers.addAll(users);
				} else {
					Log.e(TAG, e.toString());
				}
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.game_info_screen, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			mActivity.getSupportFragmentManager().popBackStack();
			return true;
		case R.id.action_camera:
			launchCamera();
			return true;
		case R.id.action_photos:
			launchPhotoEval();
			return true;
		case R.id.action_scores:
			if (scoresShowing) {
				mActivity.getSupportFragmentManager().popBackStack();
				scoresShowing = false;
			} else
				viewScores();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void viewScores() {
		scoresShowing = true;
		Fragment scoresList = new ScoresListFragment();
		mActivity
				.getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.left_slide_in, R.anim.left_slide_out,
						R.anim.right_slide_in, R.anim.right_slide_out)
				.add(R.id.fragment_container, scoresList).addToBackStack(TAG).commit();
	}

	public void launchPhotoEval() {
		// Do not switch to fragment if no photos to rank
		if (mPhotos.size() == 0) {
			Toast.makeText(getActivity().getApplicationContext(), "No More Photos To Rank",
					Toast.LENGTH_SHORT).show();
		} else {
			Fragment photoEval = new PhotoEvalFragment();
			mActivity.getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, photoEval).commit();
		}
	}

	public void launchCamera() {
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra("game", mGame.getObjectId());
		intent.putExtra("target", mTarget.getObjectId());
		startActivity(intent);
	}

}
