package edu.grinnell.facetag;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.facetag_android.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import edu.grinnell.facetag.parse.PhotoTag;
import edu.grinnell.facetag.utils.RoundedImageView;
import edu.grinnell.facetag.utils.actionBarFont;

/*
 * Fragment to prompt the user to evaluate photos that they have not yet seen
 */
public class PhotoEvalFragment extends SherlockFragment {
	final String TAG = "Photo Eval";

	View mView;
	GameScreenActivity mActivity;
	ParseUser mUser = ParseUser.getCurrentUser();
	ArrayList<PhotoTag> mPhotos = new ArrayList<PhotoTag>();

	TextView numPics;
	TextView question; // Indicates the user being targeted
	RoundedImageView evalPic;

	Button yesBut;
	Button noBut;
	// TODO Button: "I Dont Know"
	int picNum = 0;
	PhotoTag mPhoto;

	Animation spin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (GameScreenActivity) getSherlockActivity();
		Log.v(TAG , mUser.toString());
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mActivity.invalidateOptionsMenu();

		mView = inflater.inflate(R.layout.fragment_photo_eval, container, false);

		mPhotos.clear();
		mPhotos.addAll(mActivity.mPhotos);

		evalPic = (RoundedImageView) mView.findViewById(R.id.eval_photo);
		startLoadingAnim();

		spin = AnimationUtils.loadAnimation(mActivity, R.anim.loading);
		evalPic.setAnimation(spin);

		numPics = (TextView) mView.findViewById(R.id.num_pics);
		question = (TextView) mView.findViewById(R.id.question);

		OnClickListener voteButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.affirmative)
					mPhoto.setConfirmation(mPhoto.getConfirmation() + 1);
				else if (v.getId() == R.id.negative)
					mPhoto.setRejection(mPhoto.getRejection() + 1);
				ArrayList<ParseUser> voted = (ArrayList<ParseUser>) mPhoto.getVotedArray();
				voted.add(mUser);
				mPhoto.setVotedArray(voted);
				mPhoto.saveInBackground();
				startLoadingAnim();
				try {
					loadPhoto();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};

		yesBut = (Button) mView.findViewById(R.id.affirmative);
		yesBut.setOnClickListener(voteButtonListener);
		noBut = (Button) mView.findViewById(R.id.negative);
		noBut.setOnClickListener(voteButtonListener);

		picNum = mPhotos.size();
		try {
			loadPhoto();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return mView;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.removeItem(R.id.action_photos);
	}

	private void startLoadingAnim() {
		evalPic.setScaleType(ImageView.ScaleType.CENTER);
		evalPic.setImageResource(R.drawable.loading);
		evalPic.setAnimation(spin);
	}

	private void loadPhoto() throws ParseException {
		actionBarFont.fontChangeText(numPics, mView.getContext());
		numPics.setText(picNum + " pictures to evaluate");
		if (picNum > 0) {
			picNum--;
			mPhoto = mPhotos.get(picNum);

			ParseFile pic = mPhoto.getPhoto();
			ParseUser target = mPhoto.getTarget();
			actionBarFont.fontChangeText(question, mView.getContext());
			question.setText("Does this look like " + target.fetchIfNeeded().getString("fullName")
					+ "?");

			pic.getDataInBackground(new GetDataCallback() {
				public void done(byte[] data, ParseException e) {
					if (e == null) {
						Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
						evalPic.clearAnimation();
						evalPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
						evalPic.setImageBitmap(bMap);
					} else {
						Log.e(TAG, "Parse Error: " + e.getMessage());
					}
				}
			});
		} else {
			// If no more photos, return to detail fragment
			Toast.makeText(getActivity().getApplicationContext(), "No Photos To Rank",
					Toast.LENGTH_SHORT).show();
			mActivity.getSupportFragmentManager().popBackStack();
			// Fragment infoFrag = new GameInfoFragment();
			// mActivity.getSupportFragmentManager().beginTransaction()
			// .replace(R.id.fragment_container, infoFrag).commit();
		}
	}
}
