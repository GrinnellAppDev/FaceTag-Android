package edu.grinnell.facetag;

import java.util.ArrayList;

import android.R.drawable;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facetag_android.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.grinnell.facetag.gamecreate.CreateGameActivity;
import edu.grinnell.facetag.parse.Game;
import edu.grinnell.facetag.utils.actionBarFont;

public class GameListFragment extends SherlockFragment {
	GameScreenActivity mActivity;
	ListView mListView;
	final String TAG = "List Fragment";
	int backButtonCount;
	

	/**
	 * 
	 * List of Current Games for the User Includes Create Game and Refresh
	 * Buttons Calls Login Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (GameScreenActivity) getSherlockActivity();
		setHasOptionsMenu(true);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game_list, container, false);

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		ArrayAdapter<Game> gameAdapter = new GameArrayAdapter(mActivity,
				R.layout.game_list_adapter, mActivity.mGameList);
		mListView = (ListView) v.findViewById(R.id.gamelist);
		mListView.setAdapter(gameAdapter);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Game selectedGame = (Game) mListView.getItemAtPosition(position);
				view.setBackgroundResource(R.drawable.rectangle_2);
				gameInfo(selectedGame);
			}
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long id) {
				
			/*	AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.getApplicationContext());
				builder.setItems(R.array.menu_click_option, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show(); */
		
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Are you sure you want to leave this game?");
					builder.setNegativeButton("Cancel", null);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				    {
				        @Override
				        public void onClick(DialogInterface dialog, int whichButton)
				        {
				           try{
				        	ParseUser user = ParseUser.getCurrentUser();   
				        	Game game = (Game) mListView.getItemAtPosition(position);
				        	game.getParticipants().remove(user.getObjectId());
				        	game.delete();
				        	mActivity.mGameList.clear();
							mActivity.downloadGames();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        }
				    });
					AlertDialog dialog = builder.create();
					dialog.show();
				
				
				return true;
			}
		});

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.game_screen, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			mActivity.mGameList.clear();
			mActivity.downloadGames();
			return true;
		} else if (itemId == R.id.action_newgame) {
			Intent intent = new Intent(getActivity(), CreateGameActivity.class);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	// Launch game fragment
	public void gameInfo(Game game) {
		mActivity.mGame = game;
		mActivity.mPhotos.clear();
		String gameID = mActivity.mGame.getObjectId();
		
		if (mActivity.photoMap.containsKey(gameID)){
			mActivity.mPhotos.addAll(mActivity.photoMap.get(gameID));
		}
		
		Fragment gameInfo = new GameInfoFragment();
		mActivity.getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, gameInfo).addToBackStack(TAG).commit();
	}

	// Array Adapter for games list
	public class GameArrayAdapter extends ArrayAdapter<Game> {
		private final Context context;
		private final ArrayList<Game> games;
		int layoutResourceId;

		public GameArrayAdapter(Context context, int layoutResourceId, ArrayList<Game> games) {
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
			TextView textView = (TextView) rowView.findViewById(R.id.game_title);
			actionBarFont.fontChangeText(textView, mActivity.getApplicationContext());
			textView.setText(games.get(position).getName());
			
			

			String gameID = games.get(position).getObjectId();
			ImageView gameStatus = (ImageView) rowView.findViewById(R.id.game_status);
			// Select an appropriate drawable for the game status
			if (mActivity.photoMap.containsKey(gameID)) {
				gameStatus.setBackgroundResource(
						R.drawable.photos_to_judge_circle);
			} else
				gameStatus.setBackgroundResource(
						R.drawable.waiting_status_circle);

			return rowView;
		}
	}
}
