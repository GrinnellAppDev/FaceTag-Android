package edu.grinnell.facetag.takepicture;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.facetag_android.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.grinnell.facetag.parse.PhotoTag;

public class SubmitPhotoActivity extends SherlockActivity {
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

		final Button acceptButton = (Button) findViewById(R.id.accept);
		acceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptButton.setBackgroundResource(R.drawable.rec_button_press);
				acceptButton.setTextColor(Color.parseColor("#7F7F7F"));
				submitPhoto();
			}
		});

		final Button rejectButton = (Button) findViewById(R.id.reject);
		rejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rejectButton.setBackgroundResource(R.drawable.rec_button_press);
				acceptButton.setTextColor(Color.parseColor("#7F7F7F"));
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
		
		prsPhoto.setThreshold(3);

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
