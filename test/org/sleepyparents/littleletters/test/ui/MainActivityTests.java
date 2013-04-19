package org.sleepyparents.littleletters.test.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.tester.android.view.TestMenuItem;
import org.sleepyparents.littleletters.R;
import org.sleepyparents.littleletters.ui.MainActivity;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTests {
	@Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = new MainActivity().getResources()
        		.getString(R.string.hello_world);
        assertThat(hello, equalTo("Hello world!"));
    }
	
	@Test
	public void whenStartingActivityShouldGetRandomImage() {
		//Image is set in onCreate so...
		MainActivity mainActivity = new MainActivity();
		mainActivity.onCreate(null);
		ImageView letterImage = (ImageView) mainActivity.findViewById(R.id.letter_image_view);
		assertNotNull(letterImage.getDrawable());
	}
	
	@Test
	public void whenClickingRefreshShouldLoadImage() {
		MenuItem item = new TestMenuItem(R.id.menu_refresh);
		MainActivity mainActivity = new MainActivity();
		mainActivity.onCreate(null);
		ImageView letterImageView = (ImageView) mainActivity.findViewById(R.id.letter_image_view);
		Drawable startDrawable = letterImageView.getDrawable();
		mainActivity.onOptionsItemSelected(item);
		fail("so we need to inject a mock image loader innit?!");
	}
}
