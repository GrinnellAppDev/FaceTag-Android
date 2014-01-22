package com.facetag.android;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facetag.android.parse.Game;
import com.facetag_android.R;

public class GameListFragment extends Fragment {
	GameScreenActivity mActivity;
	ListView mListView;
	final String TAG = "List Fragment";

	/**
	 * 
	 * List of Current Games for the User
	 * Includes Create Game and Refresh Buttons
	 * Calls Login Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (GameScreenActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_game_list, container, false);

		Button createGame = (Button) v.findViewById(R.id.create_game);
		createGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateGame.class);
				startActivity(intent);
			}
		});
		
		Button refresh = (Button) v.findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.mGameList.clear();
				mActivity.loadGames();
			}
		});
		
		ArrayAdapter<Game> gameAdapter = new GameArrayAdapter(mActivity,
				R.layout.game_list_adapter, mActivity.mGameList);
		mListView = (ListView) v.findViewById(R.id.gamelist);
		mListView.setAdapter(gameAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Game selectedGame = (Game) mListView
						.getItemAtPosition(position);
				gameInfo(selectedGame);
			}
		});
		
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void gameInfo(Game game){
		mActivity.mGame = game;
        Fragment gameInfo = new GameInfoFragment();
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, gameInfo).addToBackStack(TAG).commit();
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
}
