package org.zywx.wbpalmstar.plugin.uexwheel.util;

import org.zywx.wbpalmstar.plugin.uexwheel.EUExWheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 背景阴影view
 * 
 */
public class CircleView extends View {

    private String mColor;

	public CircleView(Context context, String color) {
		super(context);
		this.mColor = color;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		LayoutParams lp = EUExWheel.circleLp;
		super.onDraw(canvas);
		Paint paint = new Paint();
		if(!TextUtils.isEmpty(mColor)){
		    try {
                paint.setColor(Color.parseColor(mColor));
            } catch (Exception e) {
                e.printStackTrace();
                paint.setColor(Color.argb(127, 0, 0, 0));
            }
		}else{
		    paint.setColor(Color.argb(127, 0, 0, 0));
		}
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawRect(0, 0, lp.width, lp.height, paint);
	}

}
