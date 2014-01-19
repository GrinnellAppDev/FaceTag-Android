package com.facetag.android;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameInfoFragment extends Fragment {
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

		mActivity = (GameScreenActivity) getActivity();
		mGame = mActivity.mGame;

		// Find views
		newPics = (TextView) mView.findViewById(R.id.new_photos);
		targetInfo = (TextView) mView.findViewById(R.id.target_description);
		targetPic = (ImageView) mView.findViewById(R.id.target_photo);
		gameName = (TextView) mView.findViewById(R.id.game_name);

		gameName.setText(mGame.getName());

		// Set camera button
		Button cameraButton = (Button) mView.findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCamera();
			}
		});

		// Set scores button
		Button scores = (Button) mView.findViewById(R.id.view_scores);
		scores.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewScores();
			}
		});
		
		// Set photo judge button
		Button pictures = (Button) mView.findViewById(R.id.eval_photos);
		pictures.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchPhotoEval();
			}
		});

		// Find Photos to vote on
		ParseQuery<ParseObject> pic_query = ParseQuery.getQuery("PhotoTag");
		pic_query.whereEqualTo("game", mGame.getObjectId());
		pic_query.whereNotEqualTo("usersArray", mUser);
		pic_query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> pictureList, ParseException e) {
				if (e == null) {
					newPics.setText("There are " + pictureList.size()
							+ " new photos!");
					for (int i = 0; i < pictureList.size(); i++){
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
					ImageLoader.getInstance().displayImage(
							mTarget.getString("profilePictureURL"), targetPic);
				}
			}
		});
		return mView;
	}

	public void ratePhotos(){
		//begin photo rate fragment
	}
	
	public void viewScores() {
		HashMap<String, Integer> scoreBoard = mGame.getScoreBoard();
		// new list fragment to display users and corresponding scores
	}

	
	public void launchPhotoEval(){
        Fragment photoEval = new PhotoEvalFragment();
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, photoEval).addToBackStack(TAG).commit();
	}
	
	public void launchCamera() {
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra("game", mGame.getObjectId());
		intent.putExtra("target", mTarget.getObjectId());
		startActivity(intent);
	}
}
