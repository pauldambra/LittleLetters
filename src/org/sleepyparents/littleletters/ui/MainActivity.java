package org.sleepyparents.littleletters.ui;

import java.io.IOException;

import images.ImageLoader;

import org.sleepyparents.littleletters.R;
import org.sleepyparents.littleletters.views.BezierCurveWritingView;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.google.inject.Inject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends RoboActivity {
	@Inject ImageLoader imageLoader;
	@InjectView(R.id.letter_image_view) ImageView letterImageView;	
	@InjectView(R.id.writing_view) BezierCurveWritingView writingView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLetterImage();
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
}
