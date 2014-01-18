package com.facetag_android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

/* Pull list of parse games here */
public class GameScreen extends ListActivity {
	private final String TAG = "GameScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		
		ParseUser user = ParseUser.getCurrentUser();
		TextView testText = (TextView) findViewById(R.id.test_text);
		setCreateGameButton(); 
		
		testText.setText(user.getString("fullName"));
		
	//	ListView listView = (ListView) findViewById(R.id.list);
	//	ArrayAdapter adapter = new ArrayAdapter(android.R.layout.simple_list_item_1) 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_screen, menu);
		return true;
	}
	
	public void setCreateGameButton() {
		Button browseButton = (Button) findViewById(R.id.create_game);
		browseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createGame();
			}
		});
	}
	
	public void createGame() {
		Intent intent = new Intent(this, CreateGame.class);
		startActivity(intent);
	}

}
