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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (CreateGameActivity) getSherlockActivity();
		setHasOptionsMenu(true);
		
		//get players on parse
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> users, ParseException e) {
		    if (e == null) {
		    	mUsers.addAll(users);
		    } else {
		    	Log.i(TAG, e.toString());
		    }
		  }
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_invite_list, container,
				false);

		mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ArrayAdapter<ParseUser> inviteAdapter = new ScoreListAdapter(mActivity,
				R.layout.invite_list_adapter, mUsers);
		mListView = (ListView) v.findViewById(R.id.invitelist);
		mListView.setAdapter(inviteAdapter);

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
			nameText.setText(users.get(position).getUsername());
			
			return rowView;
		}
	}
}
