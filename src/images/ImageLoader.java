package images;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;

/***
 * I can imagine different loaders for different 'difficulty' levels
 */
public interface ImageLoader {
	Drawable getImageDrawable(Context c) throws IOException;
}
