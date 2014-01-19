package com.facetag.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	final String TAG = "Game Info Screen";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_game_info, container, false);

		GameScreenActivity mActivity = (GameScreenActivity) getActivity();

		TextView targetInfo = (TextView) v.findViewById(R.id.target_description);
		ImageView targetPic = (ImageView) v.findViewById(R.id.target_photo);
		TextView gameName = (TextView) v.findViewById(R.id.game_name);

		ImageLoader.getInstance().displayImage(
				mUser.getString("profilePictureURL"), targetPic);
		
		gameName.setText(mActivity.mGame.getName());
		
		return v;
	}

	public void judgePhotos() {

	}
}
