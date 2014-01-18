package com.facetag.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.facetag_android.R;
import com.parse.ParseUser;

public class UserListFragment extends ListFragment {
	List<String> userStrings = new ArrayList<String>();
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CreateGame mActivity = (CreateGame) getActivity();
		List<ParseUser> inviteUsers = mActivity.inviteUsers;

		Iterator userIter = inviteUsers.iterator();
		while (userIter.hasNext()) {
			ParseUser thisUser = (ParseUser) userIter.next();
			userStrings.add(thisUser.getString("fullName"));
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.user_list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, userStrings);

		setListAdapter(adapter);
	}

}
