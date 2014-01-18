package com.facetag_android;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.facetag_android.parse.PhotoTag;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CameraActivity extends Activity {
	protected static Camera mCamera = null;
	private CameraPreview mPreview;

	protected String TAG = "main activity";
	public static final int MEDIA_TYPE_IMAGE = 1;

	protected Boolean preview_active;
	protected static int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
	private Handler mHandler = new Handler();
	protected String flashStatus = Camera.Parameters.FLASH_MODE_OFF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_camera);

		setPictureButton();
		setCameraSwapButton();
		setFlashButton();
		setBrowseButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.i(TAG, "onResume()");
		
		cameraInit(cameraID);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
	}

	public void cameraInit(int camID) {

		mCamera = getCameraInstance(camID);
		cameraID = camID;

		// Create our Preview view and set it as the content of our
		// activity.
		mPreview = new CameraPreview(this, mCamera);
		preview_active = true;
		Log.i(TAG, "preview initilized");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	public void setPictureButton() {
		// Add a listener to the Capture button
		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (preview_active) {
					// get an image from the camera
					mCamera.takePicture(null, null, mPicture);
					preview_active = false;
				} else {
					// else reset to preview to take another pic
					mCamera.stopPreview();
					mCamera.startPreview();
					preview_active = true;
				}
			}

		});
	}

	public void setCameraSwapButton() {
		// Add a listener to the Capture button
		Button swapButton = (Button) findViewById(R.id.button_swap);
		swapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				// must remove view before swapping it
				preview.removeView(mPreview);

				if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
					// switch to front facing camera
					cameraInit(Camera.CameraInfo.CAMERA_FACING_FRONT);
					// the flash must be off if front camera is in use
					flashStatus = Camera.Parameters.FLASH_MODE_OFF;
					Log.i(TAG, "flash off");
					Button flashButton = (Button) findViewById(R.id.button_flash);
					flashButton.setText("Turn Flash On");
				} else {
					// switch to back facing camera
					cameraInit(Camera.CameraInfo.CAMERA_FACING_BACK);
				}
				preview.addView(mPreview);
			}
		});
	}

	public void setFlashButton() {
		// Add a listener to the Capture button
		Button flashButton = (Button) findViewById(R.id.button_flash);
		flashButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleFlash();
			}
		});
	}

	public void setBrowseButton() {
		Button browseButton = (Button) findViewById(R.id.browse_photos);
		browseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startPhotoBrowse();
			}
		});
	}
	
	public void startPhotoBrowse(){
		Intent intent = new Intent(this, PhotoBrowse.class);
		startActivity(intent);
	}

	public void toggleFlash() {
		// Not applicable if front facing camera is selected
		if (cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			return;
		}

		// get Camera parameters
		Camera.Parameters params = mCamera.getParameters();
		Button flashButton = (Button) findViewById(R.id.button_flash);

		if (params.getFlashMode()
				.contentEquals(Camera.Parameters.FLASH_MODE_ON)) {
			// turn flash off
			params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			flashStatus = Camera.Parameters.FLASH_MODE_OFF;
			Log.i(TAG, "flash off");
			flashButton.setText("Turn Flash On");
		} else {
			// turn flash on
			params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			Log.i(TAG, "flash on");
			flashStatus = Camera.Parameters.FLASH_MODE_ON;
			flashButton.setText("Turn Flash Off");
		}
		// set Camera parameters
		mCamera.setParameters(params);
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(int camID) {
		Camera c = null;
		try {
			c = Camera.open(camID); // attempt to get a Camera instance
			Log.i("camera util", "camera opened");
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			Log.e("camera init", "camera unavailable");
			return null;
		}

		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onPause() {
		super.onPause();
		// surfaceDestroyed in CameraPreview is automatically called here
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.removeView(mPreview);
		mPreview.stopPreviewAndFreeCamera();
	}

	// This part is where the picture is actually taken
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			Log.i("TAG", "picture taken");

			// Resize photo from camera byte array
			Bitmap prsImg = BitmapFactory.decodeByteArray(data, 0, data.length);
			Bitmap prsImgScaled = Bitmap.createScaledBitmap(prsImg, 400, 400
					* prsImg.getHeight() / prsImg.getWidth(), false);

			Matrix matrix = new Matrix();

			/*
			 * All of the photos are sideways and for some reason the
			 * front-facing photos are upside-down compared to the back facing
			 */
			if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK)
				matrix.postRotate(90);
			else
				matrix.postRotate(270);

			Bitmap prsImgScaledRotated = Bitmap.createBitmap(prsImgScaled, 0,
					0, prsImgScaled.getWidth(), prsImgScaled.getHeight(),
					matrix, true);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			prsImgScaledRotated.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			byte[] scaledData = bos.toByteArray();

			ParseFile prsFile = new ParseFile("photo.jpg", scaledData);

			PhotoTag prsPhoto = new PhotoTag();
			prsPhoto.setPhoto(prsFile);

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());

			prsPhoto.setConfirmation(0);

			prsPhoto.setSender(ParseUser.getCurrentUser());

			prsPhoto.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if (e != null) {
						Log.e("Save To Parse", e.getMessage());
					} else {
						Log.i("Save To Parse", "Parse Upload Successful");
					}
				}
			});
		}

	};
}
