package com.facetag.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* Display a list of players in the selected game and their coresponding scores */
public class ScoresListFragment extends SherlockFragment {
	final String TAG = "Score Board";
	GameScreenActivity mActivity;
	ListView mListView;
	HashMap<String, Integer> mScoreBoard;
	ArrayList<scorePair> mScoreList = new ArrayList<scorePair>();
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (GameScreenActivity) getSherlockActivity();
		mScoreBoard = mActivity.mGame.getScoreBoard();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scores_list, container,
				false);
		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mListView = (ListView) v.findViewById(R.id.scorelist);

		// Fetch the full names of each user
		ArrayList<String> players = (ArrayList<String>) mActivity.mGame
				.getParticipants();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereContainedIn("objectId", players);
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> users, ParseException e) {
				if (e == null) {
					mUsers.addAll(users);
					Iterator<ParseUser> userIter = mUsers.iterator();
					while (userIter.hasNext()) {
						ParseUser thisPlayer = userIter.next();
						scorePair thisPair = new scorePair(thisPlayer
								.getString("fullName"), mScoreBoard
								.get(thisPlayer.getObjectId()));
						mScoreList.add(thisPair);
						ArrayAdapter<scorePair> scoreAdapter = new ScoreListAdapter(
								mActivity, R.layout.score_list_adapter,
								mScoreList);
						mListView.setAdapter(scoreAdapter);
					}
				} else {
					Log.e(TAG, e.toString());
				}
			}
		});
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
