package edu.grinnell.facetag;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
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
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import edu.grinnell.facetag.gamecreate.CreateGameActivity;
import edu.grinnell.facetag.parse.Game;
import edu.grinnell.facetag.utils.ImageLoaderUtility;
import edu.grinnell.facetag.utils.RoundedImageView;
import edu.grinnell.facetag.utils.actionBarFont;

public class GameListFragment extends SherlockFragment {
	GameScreenActivity mActivity;
	ListView mListView;
	final String TAG = "List Fragment";
	int backButtonCount;
	protected SwipeRefreshLayout mSwipeRefresh;

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
		if (mActivity.mUser != null) {
			TextView userID = (TextView) v.findViewById(R.id.userID);
			mSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
			mSwipeRefresh.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					 if (isNetworkNotAvailable()){
							
							
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setMessage("Please make sure you are connected to the internet!")
								.setTitle("Error")
								.setPositiveButton(android.R.string.ok,null);
							
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					 else {
						 mActivity.mGameList.clear();
						 mActivity.downloadGames();
				}
					
				}
			});
			mSwipeRefresh.setColorScheme(R.color.SwipeDefault, R.color.Swipe1, R.color.Swipe2, R.color.Swipe3);
			actionBarFont.fontChangeText(userID, getActivity().getApplicationContext());
			userID.setText(mActivity.mUser.getString("fullName"));
			
			RoundedImageView imageHolder = (RoundedImageView) v.findViewById(R.id.image_holder);
			String picUrl = mActivity.mUser.getString("profilePictureURL");
			ImageLoaderUtility imageLoader = new ImageLoaderUtility();
			imageLoader.loadImage(picUrl, imageHolder, getActivity().getApplicationContext());
		}

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		final ArrayAdapter<Game> gameAdapter = new GameArrayAdapter(mActivity,
				R.layout.game_list_adapter, mActivity.mGameList);
		mListView = (ListView) v.findViewById(R.id.gamelist);
		if (mActivity.mGameList.isEmpty()){
			TextView empty = (TextView) v.findViewById(R.id.empty);
			actionBarFont.fontChangeText(empty, getActivity().getApplicationContext());
			empty.setVisibility(View.VISIBLE);
		}
		else {
		mListView.setAdapter(gameAdapter);
		}

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
			public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position,
					long id) {

				/*
				 * AlertDialog.Builder builder = new
				 * AlertDialog.Builder(mActivity.getApplicationContext());
				 * builder.setItems(R.array.menu_click_option, mDialogListener);
				 * AlertDialog dialog = builder.create(); dialog.show();
				 */

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Are you sure you want to leave this game?");
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						ParseUser user = ParseUser.getCurrentUser();
						Game game = (Game) mListView.getItemAtPosition(position);
						game.getParticipants().remove(user.getObjectId());
						mActivity.mGameList.remove(position);
						game.saveInBackground();
						mActivity.mGameList.clear();
						mActivity.downloadGames();
						gameAdapter.notifyDataSetChanged();						
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

		if (itemId == R.id.action_logout) {
			ParseUser currentUser = ParseUser.getCurrentUser();
			ParseFacebookUtils.getSession().closeAndClearTokenInformation();

			ParseUser.logOut();
			if (currentUser != null) {
				Log.d(TAG, "USER IS NOT NULL TROLLOLOLOL");
			}

			Intent intent = new Intent(mActivity.getApplicationContext(), LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);

			return true;
		}
		else if (itemId == R.id.action_newgame) {
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
	
	public boolean isNetworkNotAvailable() {
		ConnectivityManager manager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable = true;
		if (networkInfo != null && networkInfo.isConnected()) {
			
			isAvailable = false;
		}
		return isAvailable;
	}

	// Launch game fragment
	public void gameInfo(Game game) {
		mActivity.mGame = game;
		mActivity.mPhotos.clear();
		String gameID = mActivity.mGame.getObjectId();

		if (mActivity.photoMap.containsKey(gameID)) {
			mActivity.mPhotos.addAll(mActivity.photoMap.get(gameID));
		}

		GameInfoFragment gameInfo = new GameInfoFragment();
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
				gameStatus.setBackgroundResource(R.drawable.photos_to_judge_circle);
			} else
				gameStatus.setBackgroundResource(R.drawable.waiting_status_circle);

			return rowView;
		}
	}
}
