package com.facetag_android;

import android.app.ListActivity;
import android.os.Bundle;

import com.facetag_android.parse.PhotoTag;
import com.parse.ParseQueryAdapter;

public class PhotoBrowse extends ListActivity {
	private ParseQueryAdapter<PhotoTag> mainAdapter;
	private PhotoListAdapter photoAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_browse);
		
		getListView().setClickable(false);

		/*
		mainAdapter = new ParseQueryAdapter<PrsPhoto>(this, PrsPhoto.class);
		mainAdapter.setTextKey("title");
		mainAdapter.setImageKey("photo");
		
		setListAdapter(mainAdapter);
		*/
		
		photoAdapter = new PhotoListAdapter(this);
		photoAdapter.loadObjects();
		setListAdapter(photoAdapter);
	}

}
