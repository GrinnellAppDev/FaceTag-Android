package edu.grinnell.facetag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.facetag_android.R;
import com.parse.ParseUser;
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
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scores_list, container, false);
		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mListView = (ListView) v.findViewById(R.id.scorelist);

		// Create scorePair objects to link the user's name with their score
		Iterator<ParseUser> userIter = mActivity.mUsers.iterator();
		while (userIter.hasNext()) {
			ParseUser thisPlayer = userIter.next();
			scorePair thisPair = new scorePair(thisPlayer.getString("fullName"),
					mScoreBoard.get(thisPlayer.getObjectId()));
			mScoreList.add(thisPair);
		}
		ArrayAdapter<scorePair> scoreAdapter = new ScoreListAdapter(mActivity,
				R.layout.score_list_adapter, mScoreList);
		mListView.setAdapter(scoreAdapter);

		return v;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			mActivity.getSupportFragmentManager().popBackStack();
			return true;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
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

		public ScoreListAdapter(Context context, int layoutResourceId, ArrayList<scorePair> scores) {
			super(context, layoutResourceId, scores);
			this.context = context;
			this.scores = scores;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(layoutResourceId, parent, false);
			TextView playerText = (TextView) rowView.findViewById(R.id.playername);
			playerText.setTypeface(tf);
			playerText.setText(scores.get(position).getPlayer());

			TextView scoreText = (TextView) rowView.findViewById(R.id.score);
			scoreText.setTypeface(tf);
			scoreText.setText("" + scores.get(position).getScore());

			return rowView;
		}
	}
}
