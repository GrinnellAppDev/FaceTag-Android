package edu.grinnell.facetag.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class actionBarFont {
	
public static void fontChange(View v, Context c){
		
		TextView mAppName;
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if(titleId == 0)
		    titleId = com.actionbarsherlock.R.id.abs__action_bar_title;

		mAppName = (TextView) v.findViewById(titleId);
		
		Typeface face = Typeface.createFromAsset(c.getAssets(), "fonts/Raleway-Regular.ttf");
		mAppName.setTypeface(face);
		
		
	

}


public static void fontChangeText(TextView v, Context c){
	
	
	Typeface tf = Typeface.createFromAsset(c.getAssets(), "fonts/Raleway-Regular.ttf");
	
	v.setTypeface(tf);
	


}

}



