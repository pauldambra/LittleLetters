package org.sleepyparents.littleletters.ui;

import java.io.IOException;

import images.ImageLoader;

import org.sleepyparents.littleletters.R;
import org.sleepyparents.littleletters.events.WritingFinishedListener;
import org.sleepyparents.littleletters.views.BezierCurveWritingView;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.google.inject.Inject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends RoboActivity implements WritingFinishedListener {
	@Inject ImageLoader imageLoader;
	@InjectView(R.id.letter_image_view) ImageView letterImageView;	
	@InjectView(R.id.writing_view) BezierCurveWritingView writingView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLetterImage();
		writingView.addListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		writingView.addListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		writingView.removeListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		writingView.removeListener(this);
	}
		
	private void getLetterImage() {
		try {
			letterImageView.setImageDrawable(imageLoader.getImageDrawable(this));
		} catch (IOException e) {
			Toast.makeText(this, "Ah, this is embarrasing...", 
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_refresh:
	            //load a new image and blank the writing view
	        	getLetterImage();
	        	writingView.clear();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void writingFinished() {
		Toast.makeText(this, "THE MAGIC HAPPENS NOW", Toast.LENGTH_SHORT).show();
	}
}
