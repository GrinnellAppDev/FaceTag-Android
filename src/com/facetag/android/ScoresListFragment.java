package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facetag_android.R;

public class ScoresListFragment extends SherlockFragment {
	final String TAG = "Score Board";
	GameScreenActivity mActivity;
	ListView mListView;
	HashMap<String, Integer> mScoreBoard;
	ArrayList mScoreList = new ArrayList();

	/**
	 * Display list of user scores 
	 * TODO break down hashmap into array
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (GameScreenActivity) getSherlockActivity();
		mScoreBoard = mActivity.mGame.getScoreBoard();
		mScoreList.addAll(mScoreBoard.entrySet());
		Log.i(TAG, mScoreList.toString());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_scores_list, container, false);
		
		return v;
	}
	
	public class ScoreMapAdapter extends ArrayAdapter {
		private final Context context;
		private final ArrayList scores;
		int layoutResourceId;

		public ScoreMapAdapter(Context context, int layoutResourceId,
				ArrayList scores) {
			super(context, layoutResourceId, scores);
			this.context = context;
			this.scores = scores;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(layoutResourceId, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.game_title);
			//textView.setText(scores.get(position).getName());

			return rowView;
		}
	}
}
