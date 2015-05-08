package org.zywx.wbpalmstar.plugin.uexwheel.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SecondView extends View implements OnTouchListener {

	private Paint paintForLine = new Paint();// 点和线
	private Paint paintForCircle = new Paint();// 圆
	private Paint paintForBitmap;// 图形
	/*
	 * 图标列表
	 */
	private Bitmap[] icons = new Bitmap[18];

	private Point[] AllPoints;// 所有点列表
	private static final int PONIT_NUM = 18;// 点的数目
	private int mPointX = 0, mPointY = 0;// 圆心坐标
	/**
	 * 半径
	 */
	private int mRadius = 0;
	private int mDegreeDelta;// 每两个点相隔的角度
	private int tempDegree = 0;// 每次转动的角度差
	private int degree;// 偏转角度

	private static int mAlpha = 255;
	// 动作监听
	private OnTurnplateListener onTurnplateListener;
	private Bitmap bitmap;
	private Bitmap[] mData;
	private Bitmap iconBg;
	private int mCount;
	private float x1 = 0f, y1 = 0f, x2 = 0f, y2 = 0f;
	private boolean isClick = true;
	private long time = 0l;

	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}

	public SecondView(Context context, int w, int h, int radius, Bitmap[] data, Bitmap bm) {
		super(context);
		mPointX = w;
		mPointY = h;
		mRadius = radius;
	    this.mData = data;
	    this.iconBg = bm;
	    if(mData.length <= 6){
	        mCount = mData.length; 
	    }else{
	        mCount = 6;
	    }
		loadIcons();// 初始化图标
		initPaints();// 初始化画笔
		initPoints();
		computeCoordinates();// 计算每个点的坐标
	}

	/**
	 * 获取所有图片
	 */
	public void loadIcons() {
		Resources r = getResources();
		for (int i = 0; i < mCount; i++) {
		    loadBitmaps(i, new BitmapDrawable(r, mData[i]));
        }
		initDraw();
	}

	/**
	 * 加载bitmap
	 */
	public void loadBitmaps(int key, Drawable d) {
		Bitmap bitmap = Bitmap.createBitmap(mRadius * 3 /5, mRadius * 3 /5, Bitmap.Config.ARGB_8888);
		Matrix m = new Matrix();
		m.setRotate(180);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, true);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, mRadius * 3 /5, mRadius * 3 /5);
		d.draw(canvas);
		icons[key] = bitmap;
	}

	public void initDraw() {
		bitmap = iconBg;
		bitmap = Bitmap.createScaledBitmap(bitmap, mRadius * 3, mRadius * 3, true);
	}

	/**
	 * 初始化画笔
	 */
	private void initPaints() {
		paintForLine = new Paint();// 点和线
		paintForCircle = new Paint();// 圆
		paintForBitmap = new Paint();// 图形
		paintForLine.setColor(Color.RED);

		paintForLine.setStrokeWidth(2);
		paintForCircle.setAntiAlias(true);
		paintForCircle.setColor(Color.WHITE);
		paintForBitmap.setAlpha(mAlpha);
	}

	/**
	 * 初始化所有的点 参数 点的角度，偏移角度，位图，标志
	 */
	private void initPoints() {
		AllPoints = new Point[PONIT_NUM];
		Point point;

		int sizeOfPoint = mCount;
		mDegreeDelta = 360 / sizeOfPoint;
	    int angle = 270;// 起始角度
		for (int index = 0; index < sizeOfPoint; index++) {
			point = new Point();
			point.angle = angle;
			angle += mDegreeDelta;
			if (angle > 360 || angle < -360) {
				angle = angle % 360;
			}// 保证angle在0~360范围内
			point.bitmap = icons[index];
			point.flag = index;
			AllPoints[index] = point;
		}
	}

	/**
	 * 重新计算每个点的角度 参数：
	 */
	private void resetPointAngle(float x, float y) {
		// 每次转动的角度
		degree = computeMigrationAngle(x, y);
		for (int index = 0; index < mCount; index++) {
			if (AllPoints[index] != null) {
				AllPoints[index].angle += degree;
				if (AllPoints[index].angle > 360) {
					AllPoints[index].angle -= 360;
				} else if (AllPoints[index].angle < 0) {
					AllPoints[index].angle += 360;
				}
			}
		}
	}

	/**
	 * 计算每个点的坐标 参数： 点的坐标,点和圆心之间中心点的坐标
	 */
	private void computeCoordinates() {
		Point point;
		for (int index = 0; index < mCount; index++) {
			point = AllPoints[index];
			if (point != null) {
				point.x = mPointX
						+ (float) (mRadius * Math.cos(point.angle * Math.PI
								/ 180));
				point.y = mPointY
						+ (float) (mRadius * Math.sin(point.angle * Math.PI
								/ 180));
				point.x_c = mPointX + (point.x - mPointX) / 2;
				point.y_c = mPointY + (point.y - mPointY) / 2;
			}
		}
	}

	/**
	 * 计算每个点的停下来时的坐标 参数： 点的坐标,点和圆心之间中心点的坐标
	 */
	private void computeState() {
		Point point;
		for (int index = 0; index < mCount; index++) {
			point = AllPoints[index];
			switch (mCount) {
            case 3:
            	if( point.angle <= 0){
            		point.angle = 270;
            	}else if (point.angle <= 120) {
                    point.angle = 30;
                } else if (point.angle <= 240) {
                    point.angle = 150;
                } else {
                    point.angle = 270;
                }
                break;
            case 4:
            	if( point.angle <= 0){
            		point.angle = 360;
            	}else if (point.angle <= 90) {
                    point.angle = 90;
                } else if (point.angle <= 180) {
                    point.angle = 180;
                } else if (point.angle <= 270) {
                    point.angle = 270;
                } else {
                    point.angle = 360;
                }
                break;
            case 5:
            	if( point.angle <= 0){
            		point.angle = 342;
            	}else if (point.angle <= 72) {
                    point.angle = 54;
                } else if (point.angle <= 144) {
                    point.angle = 126;
                } else if (point.angle <= 216) {
                    point.angle = 198;
                } else if (point.angle <= 288) {
                    point.angle = 270;
                } else {
                    point.angle = 342;
                }
                break;
            case 6:
            	if( point.angle <= 0){
            		point.angle = 330;
            	}else if (point.angle <= 60) {
                    point.angle = 30;
                } else if (point.angle <= 120) {
                    point.angle = 90;
                } else if (point.angle <= 180) {
                    point.angle = 150;
                } else if (point.angle <= 240) {
                    point.angle = 210;
                } else if (point.angle <= 300) {
                    point.angle = 270;
                } else {
                    point.angle = 330;
                }
                break;

            default:
                break;
            }
			if (point != null) {
				point.x = mPointX
						+ (float) (mRadius * Math.cos(point.angle * Math.PI
								/ 180));
				point.y = mPointY
						+ (float) (mRadius * Math.sin(point.angle * Math.PI
								/ 180));
				point.x_c = mPointX + (point.x - mPointX) / 2;
				point.y_c = mPointY + (point.y - mPointY) / 2;
			}
		}
	}

	/**
	 * 计算偏移角度 参数： 每次转动的角度差
	 */
	private int computeMigrationAngle(float x, float y) {
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

	/**
	 * 选中处理
	 */
	private void switchScreen(MotionEvent event) {
		for (Point point : AllPoints) {
			if (point != null && point.angle == 270) {
				// 找到最顶端的点
				onTurnplateListener.onPointTouch(point.flag);
				break;
			}
		}
	}

	/**
	 * 调度触摸事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		    time = System.currentTimeMillis();
		    isClick = true;
		    x1 = event.getX();
		    y1 = event.getY();
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
            x2 = event.getX();
            y2 = event.getY();
            if(isClick && ((Math.abs(x2 - x1) > 20 || Math.abs(y2 - y1) > 20))){
                isClick = false;
            }
            if(!isClick){
                computeCoordinates();
                resetPointAngle(event.getX(), event.getY());
                invalidate();
            }
			break;
		case MotionEvent.ACTION_UP:
		    if((System.currentTimeMillis() - time) < 100){
		        isClick = true;
		    }
		    if(isClick){
		        if(Math.abs(x2 - mPointX) < mRadius * 3 / 8 && Math.abs(y2 - mPointY) < mRadius * 3 / 8){
		            onTurnplateListener.onPointTouch(-1);
		            return true;
		        }
                if(Math.abs(x2 - mPointX) < mRadius * 3 / 8 && mPointY - y2 > mRadius * 3 / 8){
                    switchScreen(event);
                }
		        return true;
		    }
			resetPointAngle(event.getX(), event.getY());
			computeState();
			invalidate();
			//switchScreen(event);
	        tempDegree = 0;
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, mPointX - bitmap.getWidth() / 2, mPointY
				- bitmap.getHeight() / 2 - 11, null);
		for (int index = 0; index < mCount; index++) {
			if (AllPoints[index] != null) {
				drawInCenter(canvas, AllPoints[index].bitmap,
						AllPoints[index].x, AllPoints[index].y,
						AllPoints[index].flag);
			}
		}
	}

	/**
	 * 绘图,并把点放到图片中心处
	 */
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,
			int flag) {
		canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2,
				top - bitmap.getHeight() / 2, paintForBitmap);
	}

	public class Point {
		public int flag;// 位置标识
		Bitmap bitmap;// 图片
		int angle;// 角度
		float x;// x坐标
		float y;// y坐标
		float x_c;// 点与圆心的中心x坐标
		float y_c;// 点与圆心的中心y坐标
		int acrossDegree = 360/mCount;// 每个图标活动的范围为60度
	}

	// 选中处理的接口
	public static interface OnTurnplateListener {
		public void onPointTouch(int index);
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			Log.i("onTouch_ACTION_MOVE", "onTouch_MOVE");
			return true;
		}
		return false;
	}
}
