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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.grinnell.facetag.parse.Game;
import edu.grinnell.facetag.takepicture.CameraActivity;
import edu.grinnell.facetag.utils.ImageLoaderUtility;
import edu.grinnell.facetag.utils.RoundedImageView;
import edu.grinnell.facetag.utils.actionBarFont;

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

	final String TAG = "Game Info Screen";
	GameScreenActivity mActivity;
	Boolean scoresShowing = false;

	TextView targetInfo;
	RoundedImageView targetPic;
	TextView headInfo;
	TextView newPics;
	ImageView cameraLaunch;
	TextView placeholder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_game_info, container, false);

		mActivity = (GameScreenActivity) getSherlockActivity();
		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
		
		//loading text
		placeholder = (TextView) mView.findViewById(R.id.load_text_info);
		actionBarFont.fontChangeText(placeholder, mView.getContext());
		
		
		
	
	
		
		
		mGame = mActivity.mGame;
		
		//Launch photo eval if photos to judge
		if (!mActivity.mPhotos.isEmpty()){
			launchPhotoEval();
		}
		
		setHasOptionsMenu(true);

		// Find views
		targetInfo = (TextView) mView.findViewById(R.id.target_description);
		headInfo = (TextView) mView.findViewById(R.id.heading_info);
		cameraLaunch = (ImageView) mView.findViewById(R.id.camera_info);
		
		
		
		actionBarFont.fontChangeText(headInfo, getActivity());
		actionBarFont.fontChangeText(targetInfo, getActivity());
		// TODO replace this with animation
		targetPic = (RoundedImageView) mView.findViewById(R.id.target_photo);

	//	mActivity.setTitle(mActivity.mGame.getName());
		
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
					targetInfo.setText(mTarget.getString("fullName"));
					// Load target FB image
					ImageLoaderUtility imageLoader = new ImageLoaderUtility();
					String fbPic = mTarget.getString("profilePictureURL");
					
					//Check if there's no picture
					if (fbPic == null)
						targetPic.setImageResource(R.drawable.no_user);
					else
						imageLoader.loadImage(fbPic, targetPic,	mActivity);
					
					
					cameraLaunch.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							launchCamera();
							
						}
					});
					cameraLaunch.setImageResource(R.drawable.info_camera_final);
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
        super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			mActivity.getSupportFragmentManager().popBackStack();
			return true;
		} else if (itemId == R.id.action_photos) {
			launchPhotoEval();
			return true;
		} else if (itemId == R.id.action_scores) {
			if (scoresShowing) {
				mActivity.getSupportFragmentManager().popBackStack();
				scoresShowing = false;
			} else
				viewScores();
			return true;
		} else {
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
		if (mActivity.mPhotos.size() == 0) {
			Toast.makeText(getActivity().getApplicationContext(), "No More Photos To Rank",
					Toast.LENGTH_SHORT).show();
		} else {
			Fragment photoEval = new PhotoEvalFragment();
			mActivity.getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, photoEval).addToBackStack(TAG).commit();
		}
	}

	public void launchCamera() {
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra("game", mGame.getObjectId());
		intent.putExtra("target", mTarget.getObjectId());
		startActivity(intent);
	}

}
