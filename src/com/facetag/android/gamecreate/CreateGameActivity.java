package com.facetag.android.gamecreate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facetag.android.parse.Game;
import com.facetag_android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

// Creates a Games
//TODO Allow user to invite specific friends
// use a new list fragment
public class CreateGameActivity extends SherlockFragmentActivity {
	private final String TAG = "Create Game";
	List<String> participants = new ArrayList<String>();
	int maxPoints = 5;
	ParseUser mUser = ParseUser.getCurrentUser();
	CreateGameActivity mActivity = this;
	HashMap<String, Integer> scoreBoard = new HashMap<String, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		GameSettingsFragment settingsFrag = new GameSettingsFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.create_fragment_container, settingsFrag)
				.addToBackStack(TAG).commit();
	}
}
