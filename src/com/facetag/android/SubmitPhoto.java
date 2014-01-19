package com.facetag.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SubmitPhoto extends Activity {
	final String TAG = "Submit Photo";
	byte[] mPhoto;
	ParseUser mUser = ParseUser.getCurrentUser();
	Game mGame;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);

		Bundle extras = getIntent().getExtras();
		mPhoto = extras.getByteArray("picture");
		String game = extras.getString("game");
		Log.i(TAG, game);

		Bitmap photoBitmap = BitmapFactory.decodeByteArray(mPhoto, 0,
				mPhoto.length);
		ImageView picPreview = (ImageView) findViewById(R.id.pic_view);
		picPreview.setImageBitmap(photoBitmap);

		Button acceptButton = (Button) findViewById(R.id.accept);
		acceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submitPhoto();
			}
		});

		Button rejectButton = (Button) findViewById(R.id.reject);
		rejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});	
	}

	private void submitPhoto() {
		ParseFile prsFile = new ParseFile(ParseUser.getCurrentUser().getString(
				"fullName")
				+ ".jpg", mPhoto);

		PhotoTag prsPhoto = new PhotoTag();
		prsPhoto.setPhoto(prsFile);

		prsPhoto.setConfirmation(0);
		prsPhoto.setRejection(0);

		ArrayList<ParseUser> voted = new ArrayList<ParseUser>();
		voted.add(mUser);

		prsPhoto.setTarget(mUser);

		prsPhoto.setVotedArray(voted);

		prsPhoto.setSender(mUser);

		prsPhoto.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e != null) {
					Log.e("Save To Parse", e.getMessage());
				} else {
					Log.i("Save To Parse", "Parse Upload Successful");
				}
			}
		});
		finish();
	}
}
