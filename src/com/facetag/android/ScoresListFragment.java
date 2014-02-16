package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facetag_android.R;

/* Display a list of players in the selected game and their coresponding scores */
public class ScoresListFragment extends SherlockFragment {
	final String TAG = "Score Board";
	GameScreenActivity mActivity;
	ListView mListView;
	HashMap<String, Integer> mScoreBoard;
	ArrayList<scorePair> mScoreList = new ArrayList<scorePair>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (GameScreenActivity) getSherlockActivity();
		mScoreBoard = mActivity.mGame.getScoreBoard();
		ArrayList<String> players = (ArrayList<String>) mActivity.mGame.getParticipants();
		
		Iterator<String> keys = mScoreBoard.keySet().iterator();
		while (keys.hasNext()) {
			String thisPlayer = keys.next();
			scorePair thisPair = new scorePair(thisPlayer,
					mScoreBoard.get(thisPlayer));
			mScoreList.add(thisPair);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scores_list, container,
				false);

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ArrayAdapter<scorePair> scoreAdapter = new ScoreListAdapter(mActivity,
				R.layout.score_list_adapter, mScoreList);
		mListView = (ListView) v.findViewById(R.id.scorelist);
		mListView.setAdapter(scoreAdapter);

		return v;
	}

	// Object in order to transfer hashmap into an array
	public class scorePair {
		private String player;
		private int score;

		public scorePair(String player, int score) {
			this.player = player;
			this.score = score;
		}

		public String getPlayer() {
			return player;
		}

		public int getScore() {
			return score;
		}
	}

	public class ScoreListAdapter extends ArrayAdapter<scorePair> {
		private final Context context;
		private final ArrayList<scorePair> scores;
		int layoutResourceId;

		public ScoreListAdapter(Context context, int layoutResourceId,
				ArrayList<scorePair> scores) {
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
			TextView playerText = (TextView) rowView
					.findViewById(R.id.playername);
			playerText.setText(scores.get(position).getPlayer());

			TextView scoreText = (TextView) rowView.findViewById(R.id.score);
			scoreText.setText("" + scores.get(position).getScore());

			return rowView;
		}
	}
}
