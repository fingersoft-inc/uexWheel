package org.zywx.wbpalmstar.plugin.uexwheel.util;

import org.zywx.wbpalmstar.plugin.uexwheel.EUExWheel;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;

public class QuartercircleAnimation {

	public void startAnimationIN(ViewGroup viewGroup, View imagePlus,
			int duration, AnimationListener listener) {
		EUExWheel.isAnimationRun = true;
		viewGroup.setVisibility(View.VISIBLE);// 设置显示
		viewGroup.setFocusable(true);// 获得焦点
		viewGroup.setClickable(true);// 可以点击

		Animation animation;
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);// 停留在动画结束位置
		animation.setDuration(duration);
		viewGroup.startAnimation(animation);
		animation.setAnimationListener(listener);

		animation = new RotateAnimation(-180, 225, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);// 停留在动画结束位置
		animation.setDuration(duration);
		imagePlus.clearAnimation();
		imagePlus.startAnimation(animation);
	}

	// 出动画
	public void startAnimationOUT(ViewGroup layout, View imagePlus,
			int duration, int startOffSet, AnimationListener listener) {
		if (layout == null) {
			return;
		}
		EUExWheel.isAnimationRun = true;
		Animation animation;
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);// 停留在动画结束位置
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		layout.startAnimation(animation);
		animation.setAnimationListener(listener);

		animation = new RotateAnimation(225, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);// 停留在动画结束位置
		animation.setDuration(duration);
		imagePlus.startAnimation(animation);
	}
}
