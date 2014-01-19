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
	ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();

	TextView numPics;
	TextView question;
	ImageView evalPic;
	Button yesBut;
	Button noBut;
	int picNum;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_photo_eval, container, false);

		mActivity = (GameScreenActivity) getActivity();
		mPhotos.addAll(mActivity.mPhotos);

		evalPic = (ImageView) mView.findViewById(R.id.eval_photo);
		
		numPics = (TextView) mView.findViewById(R.id.num_pics);
		question = (TextView) mView.findViewById(R.id.question);
		
		yesBut = (Button) mView.findViewById(R.id.affirmative);
		yesBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//update photo object
			}
		});
		
		noBut = (Button) mView.findViewById(R.id.negative);
		noBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//update photo object
			}
		});


		if (mPhotos != null) {
			numPics.setText(mPhotos.size() + " pictures to evaluate");
			
			try {
				rankPhoto(mPhotos.get(0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		else
			Log.e(TAG, "mPhotos is null");

	//	for (int i = 0; i < mPhotos.size(); i++) {
	//	}

		return mView;
	}

	
	
	private void rankPhoto(PhotoTag photo) throws ParseException {
		ParseFile pic = photo.getPhoto();
		ParseUser target = photo.getTarget();
		question.setText("Does this look like " + target.fetchIfNeeded().getString("fullName") + "?");
		
		pic.getDataInBackground(new GetDataCallback() {
			  public void done(byte[] data, ParseException e) {
				    if (e == null) {
				    	Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
						evalPic.setImageBitmap(bMap);
				    } else {
				      Log.e(TAG, "Parse Error: " + e.getMessage());
				    }
				  }
				});
		
		
	}
}
