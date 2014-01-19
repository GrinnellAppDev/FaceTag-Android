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
	List<String> gameList = new ArrayList<String>();
	ArrayList<ParseObject> parseGameList = new ArrayList<ParseObject>();
	ParseUser mUser = ParseUser.getCurrentUser();
	public ArrayList<Game> mGameList = new ArrayList<Game>();
	Spinner selectGame;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);

		Bundle extras = getIntent().getExtras();
		mPhoto = extras.getByteArray("picture");

		Bitmap photoBitmap = BitmapFactory.decodeByteArray(mPhoto, 0,
				mPhoto.length);
		ImageView picPreview = (ImageView) findViewById(R.id.pic_view);
		picPreview.setImageBitmap(photoBitmap);

		selectGame = (Spinner) findViewById(R.id.selectGame);

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


		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", user.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						mGameList.add(thisGame);
						ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
								GameArrayAdapter, mGameList);
						selectGame.setAdapter(adp);
					}
				}
			}
		});
	}
	
	public class GameArrayAdapter extends ArrayAdapter<Game> {
		private final Context context;
		private final ArrayList<Game> games;
		int layoutResourceId;

		public GameArrayAdapter(Context context, int layoutResourceId,
				ArrayList<Game> games) {
			super(context, layoutResourceId, games);
			this.context = context;
			this.games = games;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(layoutResourceId, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.game_title);
			textView.setText(games.get(position).getName());

			return rowView;
		}
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
