package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.plugin.uexwheel.FirstView.CircleParams;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView.OnTurnplateListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

/**
 * 主Activity
 */
public class SecondBaseView extends RelativeLayout implements OnTurnplateListener {

	private OnTurnplateListener mListener;

    public SecondBaseView(Context context, OnTurnplateListener listener,
                          Bitmap[] data, Bitmap iconBg, CircleParams params) {
        super(context);
        this.mListener = listener;
        SecondView myView = new SecondView(context,
                params.pointX, params.pointY,
                params.radius, data, iconBg);
        // 设置监听
        myView.setOnTurnplateListener(this);
        // 填充View
        addView(myView);
    }

	@Override
	public void onPointTouch(int index) {
		mListener.onPointTouch(index);
	}

}
