package com.facetag.android;

import java.util.HashMap;

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
import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
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
	final String TAG = "Game Info Screen";
	GameScreenActivity mActivity;
	
	TextView targetInfo;
	ImageView targetPic;
	TextView gameName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater
				.inflate(R.layout.fragment_game_info, container, false);

		mActivity = (GameScreenActivity) getActivity();
		mGame = mActivity.mGame;

		//Find views
		targetInfo = (TextView) mView
				.findViewById(R.id.target_description);
		targetPic = (ImageView) mView.findViewById(R.id.target_photo);
		gameName = (TextView) mView.findViewById(R.id.game_name);
		
		//Set camera button
		Button cameraButton = (Button) mView.findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCamera();
			}
		});

		gameName.setText(mGame.getName());
		
		//Find Target
		HashMap<String, String> pairings = mGame.getPairings();
		String targetID = pairings.get(mUser.getObjectId());
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId", targetID);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser user, ParseException e) {
				if (e != null){
					Log.e(TAG, e.getMessage());
				}
				else if (user == null) {
					Log.d(TAG, "The getFirst request failed.");
				} else {
					Log.d(TAG, "Target: " + user.getString("fullName"));
					mTarget = user;
					//Set target info in view
					targetInfo.setText("Target: " + mTarget.getString("fullName"));
					ImageLoader.getInstance().displayImage(
							mTarget.getString("profilePictureURL"), targetPic);
				}
			}
		});
		return mView;
	}
	
	public void viewScores() {

	}
	
	public void judgePhotos() {

	}

	public void launchCamera() {
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra("game", mGame.getObjectId());
		intent.putExtra("target", mTarget.getObjectId());
		startActivity(intent);
	}
}
