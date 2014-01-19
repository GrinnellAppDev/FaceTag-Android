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
import android.widget.Toast;

import com.facetag.android.parse.Game;
import com.facetag.android.parse.PhotoTag;
import com.facetag_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
	String mGame;
	String mTarget;
	PhotoTag prsPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);

		Bundle extras = getIntent().getExtras();
		mPhoto = extras.getByteArray("picture");
		mGame = extras.getString("game");
		mTarget = extras.getString("target");
		Log.i(TAG, mGame);

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
		prsPhoto = new PhotoTag();

		prsPhoto.setConfirmation(0);
		prsPhoto.setRejection(0);

		ArrayList<ParseUser> voted = new ArrayList<ParseUser>();
		voted.add(mUser);

		prsPhoto.setGame(mGame);

		prsPhoto.setVotedArray(voted);

		prsPhoto.setSender(mUser);
		
		prsPhoto.setThreshold(1);

		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId", mTarget);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getMessage());
					makeErrorToast();
				} else if (user == null) {
					Log.d(TAG, "The getFirst request failed.");
					makeErrorToast();
				} else {
					prsPhoto.setTarget(user);
					ParseFile prsFile = new ParseFile(ParseUser.getCurrentUser().getString(
							"firstName")
							+ "-" + user.getString("firstName") + ".jpg", mPhoto);
					prsPhoto.setPhoto(prsFile);
					prsPhoto.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							if (e != null) {
								Log.e("Save To Parse", e.getMessage());
								makeErrorToast();
							} else {
								Log.i("Save To Parse",
										"Parse Upload Successful");
							}
						}
					});
				}
			}
		});
		finish();
	}
	
	public void makeErrorToast(){
		Toast.makeText(getApplicationContext(),
				"Unable to submit photo",
				Toast.LENGTH_SHORT).show();	
	}
}
