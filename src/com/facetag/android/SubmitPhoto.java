package com.facetag.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

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
	byte[] mPhoto;
	List<String> gameList = new ArrayList<String>();
	ArrayList<ParseObject >parseGameList = new ArrayList<ParseObject>();

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
		
		gameList.add(0,"Select Game"); //Add element at 0th index
		
		//Query Parse for game list
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> parseResponse, ParseException e) {
		        if (e == null) {		            
		            parseGameList.addAll(parseResponse);
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		
		Spinner selectGame = (Spinner) findViewById(R.id.selectGame);
		ArrayAdapter<String> adp=new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,gameList);
		selectGame.setAdapter(adp);
		
	}

	private void submitPhoto() {
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		ParseFile prsFile = new ParseFile("phototag" + timeStamp + ".jpg",
				mPhoto);

		PhotoTag prsPhoto = new PhotoTag();
		prsPhoto.setPhoto(prsFile);

		prsPhoto.setConfirmation(0);

		prsPhoto.setSender(ParseUser.getCurrentUser());

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
