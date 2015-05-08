package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.plugin.uexwheel.FirstActivity.CircleParams;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView.OnTurnplateListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * 主Activity
 */
public class SecondActivity extends Activity implements OnTurnplateListener {

	private static Bitmap[] mData;
	private static Bitmap iconBg;
	private static OnTurnplateListener mListener;
	private static CircleParams mParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 创建View
		SecondView myView = new SecondView(this, mParams.pointX, mParams.pointY, mParams.radius, mData, iconBg);
		// 设置监听
		myView.setOnTurnplateListener(this);
		// 填充View
		setContentView(myView);
	}

	@Override
	public void onPointTouch(int index) {
		mListener.onPointTouch(index);
	}

    public static void setBitmaps(Bitmap[] menuIcons) {
        mData = menuIcons;
    }

    public static void setIconBg(Bitmap subMenuBg) {
        iconBg = subMenuBg;
    }

    public static void setOnTouchListener(OnTurnplateListener listener) {
        mListener = listener;
    }

    public static void setCircleParams(CircleParams params) {
        mParams = params;
    }
}
