package com.facetag.android.gamecreate;

import java.util.ArrayList;
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
import com.actionbarsherlock.view.MenuItem;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InvitePlayersFragment extends SherlockFragment {
	final String TAG = "Invite Screen";
	CreateGameActivity mActivity;
	ArrayList<ParseUser> mUsers = new ArrayList<ParseUser>();
	ListView mListView;
	View fragView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (CreateGameActivity) getSherlockActivity();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_invite_list, container,
				false);
		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mListView = (ListView) v.findViewById(R.id.invitelist);
		
		//get players on parse
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> users, ParseException e) {
		    if (e == null) {
		    	Log.i(TAG, users.size() + " Users Retrieved");
		    	mUsers.addAll(users);
		    	ArrayAdapter<ParseUser> inviteAdapter = new ScoreListAdapter(mActivity,
						R.layout.invite_list_adapter, mUsers);
				mListView.setAdapter(inviteAdapter);
		    } else {
		    	Log.e(TAG, e.toString());
		    }
		  }
		});
		return v;
	}

	public class ScoreListAdapter extends ArrayAdapter<ParseUser> {
		private final Context context;
		private final ArrayList<ParseUser> users;
		int layoutResourceId;

		public ScoreListAdapter(Context context, int layoutResourceId,
				ArrayList<ParseUser> users) {
			super(context, layoutResourceId, users);
			this.context = context;
			this.users = users;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(layoutResourceId, parent, false);

			TextView nameText = (TextView) rowView.findViewById(R.id.invitee);
		//	nameText.setText(users.get(position).getUsername());
			nameText.setText(users.get(position).getString("fullName"));

			
			return rowView;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			GameSettingsFragment settingsFrag = new GameSettingsFragment();
			mActivity.getSupportFragmentManager().beginTransaction()
					.replace(R.id.create_fragment_container, settingsFrag).commit();
			return true;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}
}
