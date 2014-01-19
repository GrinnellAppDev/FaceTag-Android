package com.facetag.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* Pull list of parse games here */
public class GameListScreen extends Activity {
	private final String TAG = "GameScreen";
	public ArrayList<Game> gameList = new ArrayList<Game>();
	ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);

		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("participants", user.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> results, ParseException e) {
				if (e == null) {
					Log.i(TAG, results.size() + " games found");
					for (int i = 0; i < results.size(); i++) {
						Game thisGame = (Game) results.get(i);
						gameList.add(thisGame);
					}
					populateList();
				} else
					Log.e(TAG, e.getMessage());
			}
		});

		Button browseButton = (Button) findViewById(R.id.create_game);
		browseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createGame();
			}
		});

		Button detailButton = (Button) findViewById(R.id.gameDetail);
		detailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gameDetail();
			}
		});

	}

	public void createGame() {
		Intent intent = new Intent(this, CreateGame.class);
		startActivity(intent);
	}

	public void gameDetail() {
		Intent intent = new Intent(this, GameInfoScreen.class);
		startActivity(intent);
	}

	public void populateList() {
		ArrayAdapter<Game> gameAdapter = new GameArrayAdapter(this,
				R.layout.game_list_adapter, gameList);
		mListView = (ListView) findViewById(R.id.gamelist);
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
	}
	
	public void gameInfo(Game game){
		Intent intent = new Intent(this, GameInfoScreen.class);
		intent.putExtra("id", game.getObjectId());
		startActivity(intent);
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
