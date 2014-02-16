package com.facetag.android.takepicture;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* A basic Camera preview class
 * This is a SurfaceView that controls how the camera view is displayed.
 * Must coordinate with CameraActivity to handle lifecycle events*/
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	protected String TAG = "camera preview";
	protected int parentWidth, parentHeight;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		camera.setDisplayOrientation(90);
		camera.getParameters().setPreviewSize(parentWidth, parentHeight);
		
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		if (mCamera == null){
			/* Sometimes the surface will be created BEFORE onResume is called.
				This causes a crash, so we just return if mCamera is null, and surfaceCreated
				will be called again during onResume */
			Log.e(TAG, "camera is null");
			return;
		}
		
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
		Log.i(TAG, "surface created");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			// Call stopPreview() to stop updating the preview surface.
			// mCamera.stopPreview();
			stopPreviewAndFreeCamera();
		}
		Log.i(TAG, "surface destroyed");
	}

	/**
	 * When this function returns, mCamera will be null.
	 */
	public void stopPreviewAndFreeCamera() {

		if (mCamera != null) {
			// Call stopPreview() to stop updating the preview surface.
			mCamera.stopPreview();
			Log.i(TAG, "preview stopped");

			// Important: Call release() to release the camera for use by other
			// applications. Applications should release the camera immediately
			// during onPause() and re-open() it during onResume()).
			mCamera.release();
			Log.i(TAG, "camera released");

			mCamera = null;
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		
		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
}
