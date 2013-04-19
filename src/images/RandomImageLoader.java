package images;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class RandomImageLoader implements ImageLoader {
	private Random randomGenerator = new Random();
	
	@Override
	public Drawable getImageDrawable(Context c) throws IOException {
		String[] imagePaths = c.getAssets().list("");
		String path = imagePaths[randomGenerator.nextInt(imagePaths.length)];
		InputStream ims = c.getAssets().open(path);
        return Drawable.createFromStream(ims, null);
	}

}
