package com.facetag.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderUtility {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected ProgressBar spinner = null;

	public ImageLoaderUtility() {
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	protected SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			@SuppressWarnings("unused")
			String message = null;
			view.clearAnimation();
			switch (failReason.getType()) {
			case IO_ERROR:
				message = "Input/Output error";
				break;
			case DECODING_ERROR:
				message = "Image can't be decoded";
				break;
			case NETWORK_DENIED:
				message = "Downloads are denied";
				break;
			case OUT_OF_MEMORY:
				message = "Out Of Memory error";
				break;
			case UNKNOWN:
				message = "Unknown error";
				break;
			}
			Animation a = ((ImageView) view).getAnimation();
			if (a != null)
				view.clearAnimation();
				//a.cancel();
			view.setVisibility(View.GONE);
		}
	};

	// load image based on URL
	public void loadImage(String imgUrl, ImageView imgView, Context context) {

		DisplayImageOptions options;

		options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.resetViewBeforeLoading().cacheOnDisc()
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		spinner = new ProgressBar(context, null,
				android.R.attr.progressBarStyleSmall);

		imageLoader.displayImage(imgUrl, imgView, options, listener);
	}
}