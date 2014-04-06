package edu.grinnell.facetag.gamecreate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facetag_android.R;
import com.parse.ParseUser;

// Creates a Game
public class CreateGameActivity extends SherlockFragmentActivity {
	private final String TAG = "Create Game";
	List<String> participants = new ArrayList<String>();
	int maxPoints = 5;
	int maxTime = 4;
	ParseUser mUser = ParseUser.getCurrentUser();
	CreateGameActivity mActivity = this;
	HashMap<String, Integer> scoreBoard = new HashMap<String, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		GameSettingsFragment settingsFrag = new GameSettingsFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.create_fragment_container, settingsFrag).commit();
	}
}
