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
import android.widget.Toast;

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
		targetInfo = (TextView) mView.findViewById(R.id.target_description);
		targetPic = (ImageView) mView.findViewById(R.id.target_photo);
		gameName = (TextView) mView.findViewById(R.id.game_name);

		gameName.setText("Game: " + mGame.getName());

		// Set camera button
		Button cameraButton = (Button) mView.findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCamera();
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

	public void viewScores() {
		Fragment scoresList = new ScoresListFragment();
		mActivity.getSupportFragmentManager().beginTransaction()
				.addToBackStack(TAG)
				.replace(R.id.fragment_container, scoresList).commit();
	}

	public void launchPhotoEval() {
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
