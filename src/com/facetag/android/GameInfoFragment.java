package com.facetag.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseUser;

public class GameInfoFragment extends Fragment {
	ParseUser mUser = ParseUser.getCurrentUser();
	ParseUser mTarget;
	final String TAG = "Game Info Screen";
	GameScreenActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_game_info, container, false);

		mActivity = (GameScreenActivity) getActivity();

		TextView targetInfo = (TextView) v
				.findViewById(R.id.target_description);
		ImageView targetPic = (ImageView) v.findViewById(R.id.target_photo);
		TextView gameName = (TextView) v.findViewById(R.id.game_name);

		ImageLoader.getInstance().displayImage(
				mUser.getString("profilePictureURL"), targetPic);

		gameName.setText(mActivity.mGame.getName());

		Button cameraButton = (Button) v.findViewById(R.id.camera_button);

		cameraButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchCamera();
			}
		});
		return v;
	}

	public void judgePhotos() {

	}

	public void launchCamera() {
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra("Game Name", mActivity.mGame.getName());
		startActivity(intent);
	}
}
