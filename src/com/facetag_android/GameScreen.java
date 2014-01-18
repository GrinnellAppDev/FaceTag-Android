package com.facetag_android;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/* Pull list of parse games here */
public class GameScreen extends ListActivity {
	private final String TAG = "GameScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
	//	ListView listView = (ListView) findViewById(R.id.list);
				
	//	ArrayAdapter adapter = new ArrayAdapter(android.R.layout.simple_list_item_1) 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_screen, menu);
		return true;
	}

}
