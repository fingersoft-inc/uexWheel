package org.zywx.wbpalmstar.plugin.uexwheel.util;

import java.util.List;

import org.zywx.wbpalmstar.plugin.uexwheel.bean.UnitBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TurnplateView extends View implements OnTouchListener {

	private OnTurnplateListener onTurnplateListener;

	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}

	private Paint mPaint = new Paint();

	private Paint paintCircle = new Paint();

	private Bitmap[] icons = new Bitmap[15];

	private Point[] points;

	private static final int PONIT_NUM = 10;

	private int mPointX = 0, mPointY = 0;

	private int mRadius = 0;

	private int mDegreeDelta;

	private int tempDegree = 0;

	private int chooseBtn = 999;
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	private List<UnitBean> mData;
	private int mIconNum;

	public TurnplateView(Context context, int px, int py, int radius, List<UnitBean> data) {
		super(context);
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(2);
		paintCircle.setAntiAlias(true);
		paintCircle.setColor(Color.RED);
		paintCircle.setAlpha(0);
		mPointX = px;
		mPointY = py;
		mRadius = radius;
		this.mData = data;
		if(this.mData.size() < 5){
		    mIconNum = this.mData.size();
		}else{
		    mIconNum = 5;
		}
		
		loadIcons();
		initPoints();
		computeCoordinates();
	}

	public void loadBitmaps(int key, Drawable d) {
		Bitmap bitmap = Bitmap.createBitmap(mRadius / 6, mRadius / 6,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, mRadius / 6, mRadius / 6);
		d.draw(canvas);
		icons[key] = bitmap;
	}

	public void loadIcons() {
	    for (int i = 0; i < PONIT_NUM; i++) {
            int index = i % mData.size();
            loadBitmaps(i, new BitmapDrawable(getResources(), mData.get(index).getIcon()));
        }
	}

	private void initPoints() {
		points = new Point[PONIT_NUM];
		Point point;
		int angle;
		mDegreeDelta = 90 / mIconNum;
	    switch (mIconNum) {
	        case 3:
	            angle = 165 + mDegreeDelta;
	            break;
	        case 4:
	            angle = 169 + mDegreeDelta;
	            break;
	        case 5:
	            angle = 170 + mDegreeDelta;
	            break;
	        default:
	            angle = 170 + mDegreeDelta;
	            break;
	    }
		for (int index = 0; index < PONIT_NUM; index++) {
		    int i = index % mData.size();
			point = new Point();
			point.angle = angle;
			angle += mDegreeDelta;
			point.bitmap = icons[index];
			point.flag = index;
			point.title = mData.get(i).getTitle();
			point.index = i;
			points[index] = point;
		}
	}

	private void resetPointAngle(float x, float y) {
		int degree = computeMigrationAngle(x, y);
		for (int index = 0; index < PONIT_NUM; index++) {
			points[index].angle += degree;
			if (points[index].angle > 270) {
				points[index].angle -= PONIT_NUM * 90 / mIconNum;
			} else if (points[index].angle < (270 - PONIT_NUM * 90 / mIconNum)) {
				points[index].angle += PONIT_NUM * 90 / mIconNum;
			}
		}
	}

	private void computeCoordinates() {
		Point point;
		for (int index = 0; index < PONIT_NUM; index++) {
			point = points[index];
			point.x = mPointX
					+ (float) (mRadius * Math.cos(point.angle * Math.PI / 180));
			point.y = mPointY
					+ (float) (mRadius * Math.sin(point.angle * Math.PI / 180));
			point.x_c = mPointX + (point.x - mPointX) / 2;
			point.y_c = mPointY + (point.y - mPointY) / 2;
		}
	}

	public int computeMigrationAngle(float x, float y) {
		int a = 0;
		float distance = (float) Math
				.sqrt(((x - mPointX) * (x - mPointX) + (y - mPointY)
						* (y - mPointY)));
		int degree = (int) (Math.acos((x - mPointX) / distance) * 180 / Math.PI);
		if (y < mPointY) {
			degree = -degree;
		}
		if (tempDegree != 0) {
			a = degree - tempDegree;
		}
		tempDegree = degree;
		return a;
	}

	private void computeCurrentDistance(float x, float y) {
		int degree = computeMigrationAngle(x, y);
		for (Point point : points) {
			float distance = (float) Math
					.sqrt(((x - point.x) * (x - point.x) + (y - point.y)
							* (y - point.y)));
			if (distance < 50 && degree < 10) {
				chooseBtn = point.flag;
				break;
			} else {
				chooseBtn = 999;
			}
		}
	}

	private void switchScreen(MotionEvent event) {
		computeCurrentDistance(event.getX(), event.getY());
		if (x1 != x2 || y1 != y2) {
			chooseBtn = 999;
			return;
		}
		if(chooseBtn < PONIT_NUM){
		    onTurnplateListener.onPointTouch(points[chooseBtn].index);	    
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			x1 = event.getX();
			y1 = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			resetPointAngle(event.getX(), event.getY());
			computeCoordinates();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			x2 = event.getX();
			y2 = event.getY();
			switchScreen(event);
			tempDegree = 0;
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawCircle(mPointX, mPointY, mRadius, paintCircle);
		for (int index = 0; index < PONIT_NUM; index++) {
			drawInCenter(canvas, points[index].bitmap, points[index].x,
					points[index].y, points[index].flag, points[index].title);
		}
	}

	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,
			int flag, String title) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(35);
        paint.setColor(0xFFFFFFFF);
        paint.setTextAlign(Align.CENTER);
        FontMetrics fm = paint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top;
        canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2,
             top - bitmap.getHeight()/2 - fontHeight/2, null);
        canvas.drawText(title, left, top + bitmap.getHeight()/2, paint);
        canvas.save();
	}

	class Point {
		int flag;
		Bitmap bitmap;
		int angle;
		float x;
		float y;
		float x_c;
		float y_c;
		String title;
		int index;
	}

	public static interface OnTurnplateListener {

		public void onPointTouch(int flag);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}

}
