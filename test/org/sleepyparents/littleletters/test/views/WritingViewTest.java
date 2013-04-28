package org.sleepyparents.littleletters.test.views;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.sleepyparents.littleletters.R;
import org.sleepyparents.littleletters.events.WritingFinishedListener;
import org.sleepyparents.littleletters.views.BezierCurveWritingView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.google.inject.Inject;

@RunWith(RobolectricTestRunner.class)
public class WritingViewTest {
	BezierCurveWritingView writingView;
	
	@Inject Context context;
	
	@Test
	public void whenWritingStopsItShouldNotifyListeners() {
		LinearLayout mainActivity = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.activity_main, null);
		writingView = (BezierCurveWritingView) mainActivity.findViewById(R.id.writing_view);

		//need to listen to the view...
		WritingFinishedListener mockListener = mock(WritingFinishedListener.class);
		writingView.addListener(mockListener);
		
		//the view responds to action_down and action_up so...
		writingView.onTouchEvent(startTouch());
		writingView.onTouchEvent(endTouch());
		
		Robolectric.idleMainLooper(500);
		
		verify(mockListener).writingFinished();
	}

	private MotionEvent startTouch() {
		return MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,0, 0, 0);
	}
	
	private MotionEvent endTouch() {
		return MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 1, 1, 0);
	}
}
