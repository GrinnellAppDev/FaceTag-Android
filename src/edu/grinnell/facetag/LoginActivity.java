package edu.grinnell.facetag;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facetag_android.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/* Login to facebook here and link to parse */
public class LoginActivity extends SherlockFragmentActivity {
	private final String TAG = "LoginActivity";
	static TextView testText;
	LoginButton loginButton;
	ParseUser currentUser = ParseUser.getCurrentUser();
	ArrayList<ParseUser> invitedUsers = new ArrayList<ParseUser>();
	/*
	 * Logs in to facebook
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);



		loginButton = (LoginButton) findViewById(R.id.login);
		//Make button disapear when clicked
		//TODO add a loading animation
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FBlogin();
				//loginButton.setVisibility(View.INVISIBLE);
			}
		});
		
		  if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
		        // Go to the user info activity
		    	Intent intent = new Intent(LoginActivity.this, GameScreenActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
		    }
	}

	private void FBlogin() {
		Collection<String> permissions = new ArrayList<String>();
		permissions.add("email");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d(TAG, "FB login error: " + err.toString());
					Toast.makeText(getApplicationContext(), "Error Logging In " + err.toString(), 
							   Toast.LENGTH_SHORT).show();
					
				}
					
				else if(user.isNew()){
					Log.d(TAG, "New User signed in to FB");
					Log.d(TAG, user.getObjectId().toString());
					getFacebookInfoInBackground();
					Intent intent = new Intent(LoginActivity.this, GameScreenActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				} else { 
					Log.d(TAG, "User signed in to FB");
					Log.d(TAG, user.getObjectId().toString());
					getFacebookInfoInBackground();
					Intent intent = new Intent(LoginActivity.this, GameScreenActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				}
			}
		});
	}

	private static void getFacebookInfoInBackground() {
		Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							ParseUser.getCurrentUser().put("facebookId",
									user.getId());
							ParseUser.getCurrentUser().put("fullName",
									user.getName());
							ParseUser.getCurrentUser().put("firstName",
									user.getFirstName());
							ParseUser.getCurrentUser().put("lastName",
									user.getLastName());
							ParseUser.getCurrentUser().put("email",
									user.getProperty("email"));
							String imageURL = "https://graph.facebook.com/"
									+ user.getId()
									+ "/picture?width=200&height=200";
							ParseUser.getCurrentUser().put("profilePictureURL",
									imageURL);
							ParseUser.getCurrentUser().saveInBackground();
						}
					}
				}).executeAsync();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	
	 	
	  

}
