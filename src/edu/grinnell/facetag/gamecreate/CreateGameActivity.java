package edu.grinnell.facetag.gamecreate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.view.MenuItem;
import com.facetag_android.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.parse.ParseUser;

// Creates a Game
public class CreateGameActivity extends SlidingFragmentActivity {
	private final String TAG = "Create Game";
	List<String> participants = new ArrayList<String>();
	int maxPoints = 5;
	int maxTime = 4;
	ParseUser mUser = ParseUser.getCurrentUser();
	CreateGameActivity mActivity = this;
	HashMap<String, Integer> scoreBoard = new HashMap<String, Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		setBehindContentView(R.layout.menu_frame);
		initMenu(getSlidingMenu());

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new InvitePlayersFragment()).commit();

		GameSettingsFragment settingsFrag = new GameSettingsFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.create_fragment_container, settingsFrag).commit();
	}

	public static void initMenu(SlidingMenu menu) {
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeDegree(0.35f);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow_right);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindOffset(200);
		menu.setMenu(R.layout.menu_frame);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(mActivity);
			return true;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}
}
