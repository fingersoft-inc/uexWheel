package org.zywx.wbpalmstar.plugin.uexwheel.util;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout.LayoutParams;

public class CircleAnimation {

	private LayoutParams lp;

    public void setLp(LayoutParams lp) {
        this.lp = lp;
    }

    // 进入动画
	public void startAnimationIN(ViewGroup viewGroup, int duration) {
		Animation animation;
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		viewGroup.startAnimation(animation);
	}

	// 退出动画
	public void startAnimationOUT(final ViewGroup viewGroup, int duration,
			int startOffSet) {
		Animation animation;
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -3f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		viewGroup.startAnimation(animation);
	}

	// 添加view动画
	public void startAnimationAdd(ViewGroup viewGroup, int duration,
			int startOffSet) {
		Animation animation;
		animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
				Animation.ABSOLUTE, lp.width / 2, Animation.ABSOLUTE,
				lp.height);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		viewGroup.startAnimation(animation);
	}

	// 移除view动画
	public void startAnimationRemove(final ViewGroup viewGroup, int duration,
			int startOffSet) {
		Animation animation;
		animation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
				Animation.ABSOLUTE, lp.width / 2, Animation.ABSOLUTE,
				lp.height);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		viewGroup.startAnimation(animation);
	}

	// 先移除在添加view动画
	public void startAnimationRemoveThenAdd(final ViewGroup viewGroup, final ViewGroup vg,
			final int duration, int startOffSet) {
		Animation animation;
		animation = new ScaleAnimation(1.0f, 0, 1.0f, 0, Animation.ABSOLUTE,
				lp.width / 2, Animation.ABSOLUTE, lp.height);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		viewGroup.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
						Animation.ABSOLUTE, lp.width / 2, Animation.ABSOLUTE,
						lp.height);
				animation.setFillAfter(true);
				animation.setDuration(duration);
				animation.setStartOffset(200);
				vg.startAnimation(animation);
			}
		});
	}
}
