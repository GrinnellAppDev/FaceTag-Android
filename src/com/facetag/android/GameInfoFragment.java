package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag.android.utils.ImageLoaderUtility;
import com.facetag_android.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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

	TextView targetInfo;
	ImageView targetPic;
	TextView gameName;
	TextView newPics;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_game_info, container, false);

		mActivity = (GameScreenActivity) getSherlockActivity();
	    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    
		mGame = mActivity.mGame;
		setHasOptionsMenu(true);
		
		// Find views
		targetInfo = (TextView) mView.findViewById(R.id.target_description);
		//TODO replace this with animation
		targetPic = (ImageView) mView.findViewById(R.id.target_photo);
		
		gameName = (TextView) mView.findViewById(R.id.game_name);

		gameName.setText("Game: " + mGame.getName());
		
		/*
		// Find Photos to vote on
		// Add this to the list fragment to display number of photos to vote on in the list
		ParseQuery<ParseObject> pic_query = ParseQuery.getQuery("PhotoTag");
		pic_query.whereEqualTo("game", mGame.getObjectId());
		pic_query.whereNotEqualTo("usersArray", mUser);
		pic_query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> pictureList, ParseException e) {
				if (e == null) {
					mPhotos.clear();
					for (int i = 0; i < pictureList.size(); i++) {
						PhotoTag thisPic = (PhotoTag) pictureList.get(i);
						mPhotos.add(thisPic);
					}
					mActivity.mPhotos.clear();
					mActivity.mPhotos.addAll(mPhotos);
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
*/

		// Find Target
		HashMap<String, String> pairings = mGame.getPairings();
		String targetID = pairings.get(mUser.getObjectId());
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId", targetID);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getMessage());
				} else if (user == null) {
					Log.d(TAG, "The getFirst request failed.");
				} else {
					Log.d(TAG, "Target: " + user.getString("fullName"));
					mTarget = user;
					// Set target info in view
					targetInfo.setText("Target: "
							+ mTarget.getString("fullName"));
					//Load target FB image
					ImageLoaderUtility imageLoader = new ImageLoaderUtility();
					imageLoader.loadImage(
							mTarget.getString("profilePictureURL"), targetPic, mActivity);
				}
			}
		});
		return mView;
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//TODO
	public void viewScores() {
		Fragment scoresList = new ScoresListFragment();
		mActivity.getSupportFragmentManager().beginTransaction()
				.addToBackStack(TAG)
				.replace(R.id.fragment_container, scoresList).commit();
	}

	public void launchPhotoEval() {
		//Do not switch to fragment if no photos to rank
		if (mPhotos.size() == 0) {
			Toast.makeText(getActivity().getApplicationContext(),
					"No More Photos To Rank", Toast.LENGTH_SHORT).show();
		} else {
			Fragment photoEval = new PhotoEvalFragment();
			mActivity.getSupportFragmentManager().beginTransaction()
					.addToBackStack(TAG)
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
