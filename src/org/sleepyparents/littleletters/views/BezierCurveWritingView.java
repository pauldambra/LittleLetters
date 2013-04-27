package org.sleepyparents.littleletters.views;

import java.util.concurrent.CopyOnWriteArrayList;

import no.geosoft.cc.geometry.spline.SplineFactory;

import org.sleepyparents.littleletters.events.WritingFinishedListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BezierCurveWritingView extends View {
	private static final float STROKE_WIDTH = 10f;

	/** Need to track this so the dirty region can accommodate the stroke. **/
	private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

	private static final int TOUCH_FINISHED_NOTIFICATION_DELAY = 350; // milliseconds

	private Paint paint = new Paint();
	private Path path = new Path();

	/**
	 * Optimizes painting by invalidating the smallest possible area.
	 */
	private float lastTouchX;
	private float lastTouchY;
	private final RectF dirtyRect = new RectF();

	private CopyOnWriteArrayList<WritingFinishedListener> writingListeners = new CopyOnWriteArrayList<WritingFinishedListener>();
	
	public void addListener(WritingFinishedListener listener) {
		writingListeners.add(listener);
	}
	
	public void removeListener(WritingFinishedListener listener) {
		writingListeners.remove(listener);
	}
	
	private Handler writingFinishedHandler = new Handler();
	private Runnable writingFinishedRunnable = new Runnable() {
		public void run() {
			for (WritingFinishedListener listener : writingListeners) {
				listener.writingFinished();
			}
		}
	};

	private void cancelTouchFinishedRunnable() {
		writingFinishedHandler.removeCallbacks(writingFinishedRunnable);
	}

	private void setTouchFinishedRunbale() {
		writingFinishedHandler.postDelayed(writingFinishedRunnable,
				TOUCH_FINISHED_NOTIFICATION_DELAY);
	}

	private void restartTouchFinishedCountdown() {
		cancelTouchFinishedRunnable();
		setTouchFinishedRunbale();
	}

	private Canvas lastCanvas;

	public BezierCurveWritingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(STROKE_WIDTH);
	}

	public Bitmap getCanvasAsBitmap(Resources res) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(res);
		bitmapDrawable.draw(lastCanvas);
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Erases the signature.
	 */
	public void clear() {
		path.reset();

		// Repaints the entire view.
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(path, paint);
		lastCanvas = canvas;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(eventX, eventY);
			lastTouchX = eventX;
			lastTouchY = eventY;
			// There is no end point yet, so don't waste cycles invalidating.
			return true;

		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			// Start tracking the dirty region.
			resetDirtyRect(eventX, eventY);

			double lastTouchPointX = lastTouchX;
			double lastTouchPointY = lastTouchY;
			// When the hardware tracks events faster than they are delivered,
			// the
			// event will contain a history of those skipped points.
			int historySize = event.getHistorySize();
			for (int i = 0; i < historySize; i++) {
				float historicalX = event.getHistoricalX(i);
				float historicalY = event.getHistoricalY(i);
				expandDirtyRect(historicalX, historicalY);
				getCubicBezierPath(lastTouchPointX, lastTouchPointY,
						historicalX, historicalY);
				lastTouchPointX = historicalX;
				lastTouchPointY = historicalY;
				// update the handler delay to notify listeners of TouchFinished
				restartTouchFinishedCountdown();
			}

			// After replaying history, connect the line to the touch point.
			getCubicBezierPath(lastTouchPointX, lastTouchPointY, eventX, eventY);
			break;

		default:
			Log.d(this.getClass().getName(),
					"Ignored touch event: " + event.toString());
			return false;
		}

		// Include half the stroke width to avoid clipping.
		invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
				(int) (dirtyRect.top - HALF_STROKE_WIDTH),
				(int) (dirtyRect.right + HALF_STROKE_WIDTH),
				(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

		lastTouchX = eventX;
		lastTouchY = eventY;

		return true;
	}

	private void getCubicBezierPath(double startX, double startY, float endX,
			float endY) {
		double[] touchPoints = new double[6];
		touchPoints[0] = startX;
		touchPoints[1] = startY;
		touchPoints[2] = 0.0;
		touchPoints[3] = endX;
		touchPoints[4] = endY;
		touchPoints[5] = 0.0;
		double[] splineResult = SplineFactory.createCatmullRom(touchPoints, 3);
		// path.lineTo(historicalX, historicalY);
		path.cubicTo((float) splineResult[3], (float) splineResult[4],
				(float) splineResult[6], (float) splineResult[7], endX, endY);
	}

	/**
	 * Called when replaying history to ensure the dirty region includes all
	 * points.
	 */
	private void expandDirtyRect(float historicalX, float historicalY) {
		if (historicalX < dirtyRect.left) {
			dirtyRect.left = historicalX;
		} else if (historicalX > dirtyRect.right) {
			dirtyRect.right = historicalX;
		}
		if (historicalY < dirtyRect.top) {
			dirtyRect.top = historicalY;
		} else if (historicalY > dirtyRect.bottom) {
			dirtyRect.bottom = historicalY;
		}
	}

	/**
	 * Resets the dirty region when the motion event occurs.
	 */
	private void resetDirtyRect(float eventX, float eventY) {

		// The lastTouchX and lastTouchY were set when the ACTION_DOWN
		// motion event occurred.
		dirtyRect.left = Math.min(lastTouchX, eventX);
		dirtyRect.right = Math.max(lastTouchX, eventX);
		dirtyRect.top = Math.min(lastTouchY, eventY);
		dirtyRect.bottom = Math.max(lastTouchY, eventY);
	}

}
