package com.facetag.android;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class PhotoEvalFragment extends Fragment {
	final String TAG = "Photo Eval";

	View mView;
	GameScreenActivity mActivity;
	ParseUser mUser = ParseUser.getCurrentUser();
	ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();

	TextView numPics;
	TextView question;
	ImageView evalPic;

	Button yesBut;
	Button noBut;
	int picNum = 0;
	PhotoTag mPhoto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		mView = inflater
				.inflate(R.layout.fragment_photo_eval, container, false);

		mActivity = (GameScreenActivity) getActivity();
		mPhotos.addAll(mActivity.mPhotos);

		evalPic = (ImageView) mView.findViewById(R.id.eval_photo);

		numPics = (TextView) mView.findViewById(R.id.num_pics);
		question = (TextView) mView.findViewById(R.id.question);

		yesBut = (Button) mView.findViewById(R.id.affirmative);
		yesBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPhoto.setConfirmation(mPhoto.getConfirmation() + 1);
				ArrayList<ParseUser> voted = (ArrayList<ParseUser>) mPhoto
						.getVotedArray();
				voted.add(mUser);
				mPhoto.setVotedArray(voted);
				mPhoto.saveInBackground();
				try {
					rankPhoto();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		noBut = (Button) mView.findViewById(R.id.negative);
		noBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPhoto.setRejection(mPhoto.getRejection() + 1);
				ArrayList<ParseUser> voted = (ArrayList<ParseUser>) mPhoto
						.getVotedArray();
				voted.add(mUser);
				mPhoto.setVotedArray(voted);
				mPhoto.saveInBackground();
				evalPic.setImageResource(R.drawable.loading);
				try {
					rankPhoto();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		picNum = mPhotos.size();
		try {
			rankPhoto();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return mView;
	}

	private void rankPhoto() throws ParseException {
		numPics.setText(picNum + " pictures to evaluate");
		if (picNum > 0) {
			picNum--;
			mPhoto = mPhotos.get(picNum);

			ParseFile pic = mPhoto.getPhoto();
			ParseUser target = mPhoto.getTarget();
			question.setText("Does this look like "
					+ target.fetchIfNeeded().getString("fullName") + "?");

			pic.getDataInBackground(new GetDataCallback() {
				public void done(byte[] data, ParseException e) {
					if (e == null) {
						Bitmap bMap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						evalPic.setImageBitmap(bMap);
					} else {
						Log.e(TAG, "Parse Error: " + e.getMessage());
					}
				}
			});
		} else {
			Log.i(TAG, "No Photos To Rank");
			Toast.makeText(getActivity().getApplicationContext(),
					"No More Photos To Rank",
					Toast.LENGTH_SHORT).show();
			mActivity.getSupportFragmentManager().popBackStack();
		//	Fragment gameInfo = new GameInfoFragment();
		//	mActivity.getSupportFragmentManager().beginTransaction()
		//			.replace(R.id.fragment_container, gameInfo).commit();
		}
	}
}
