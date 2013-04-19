package org.sleepyparents.littleletters.test.images;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import images.ImageLoader;
import images.RandomImageLoader;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.inject.Inject;

@RunWith(RobolectricTestRunner.class)
public class RandomImageLoaderTests {
	@Inject Context context;
	
	@Before
	public void Setup() {
		assertNotNull(context);
	}
	
	@Test
	public void ShouldLoadAnyImage() {
		ImageLoader loader = new RandomImageLoader();
		try {
			Drawable image = loader.getImageDrawable(context);
			assertNotNull(image);
		} catch (IOException e) {
			fail("should not throw here. "+e.getMessage());
		}
	}
}
