package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.plugin.uexwheel.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.CircleView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

/**
 * 背景activity
 */
public class BackGroundView extends RelativeLayout {

	public static final String TAG = "BackGroundView";

    public BackGroundView(Context context, String color) {
        super(context);
        CircleView myView = new CircleView(context, color);
        addView(myView);
    }

}
