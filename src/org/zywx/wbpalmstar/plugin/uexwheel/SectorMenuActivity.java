package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.plugin.uexwheel.EUExWheel.OnMenuSelectListener;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.QuartercircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.QuartercircleAnimation;
import org.zywx.wbpalmstar.plugin.uexwheel.util.TurnplateView;
import org.zywx.wbpalmstar.plugin.uexwheel.util.TurnplateView.OnTurnplateListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class SectorMenuActivity extends Activity implements OnTurnplateListener {

	private TurnplateView view;
	private int width, height;
	
	private RelativeLayout layout;
	private ImageView iv_bg;
	private RelativeLayout layout_in;
	private OnMenuSelectListener mSelectedListener;
	private static QuartercircleBean mBean;

	@Override
	protected void onStart() {
		super.onStart();
		// 执行动画
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		width = intent.getIntExtra(EUExWheel.INTENT_MENU_WIDTH, 0);
		height = intent.getIntExtra(EUExWheel.INTENT_MENU_HEIGHT, 0);
		layout = new RelativeLayout(getApplicationContext());
		layout_in = new RelativeLayout(getApplicationContext());
		init();
		setContentView(layout);
	}

    /**
	 * 旋转打开动画
	 * 
	 * @param imagePlus
	 */
	public void startAnimationIn(View imagePlus, final TextView tv) {
		new QuartercircleAnimation().startAnimationIN(layout, imagePlus, 500,
				new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						EUExWheel.isAnimationRun = false;
						tv.setText(mBean.getCloseTitle());
					}
				});
	}

	/**
	 * 旋转退出动画
	 * 
	 * @param imagePlus
	 *            悬浮窗口的加号控件
	 */
	public void startAnimationOUT(View imagePlus, final TextView tv) {
		new QuartercircleAnimation().startAnimationOUT(layout, imagePlus, 500, 0,
				new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if (mSelectedListener != null) {
							mSelectedListener.onClose();
						}
						EUExWheel.isAnimationRun = false;
						tv.setText(mBean.getOpenTitle());
					}
				});
	}

	/**
	 * 设置监听回调点击栏目
	 * 
	 * @param listener
	 */
	public void setOnMenuSelectedListener(OnMenuSelectListener listener) {
		mSelectedListener = listener;
	}

	private void init() {
		view = new TurnplateView(getApplicationContext(), width, height,
				2 * width / 3, mBean.getData());
		view.setOnTurnplateListener(this);
		// 背景图片加载
		iv_bg = new ImageView(getApplicationContext());
        iv_bg.setImageBitmap(mBean.getRootBg());
		// 设置布局规则
		LayoutParams lp = new RelativeLayout.LayoutParams(4 * width / 5,
				4 * width / 5);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout_in.addView(iv_bg, lp);
		layout_in.addView(view);
		layout.addView(layout_in);
		layout.setBackgroundColor(Color.BLACK);
		layout.getBackground().setAlpha(50);
	}

	@Override
	public void onPointTouch(int flag) {
		if (flag != 999) {
			if (mSelectedListener != null) {
				mSelectedListener.onSelect(flag);
			}
		}
	}

    public static void setDataBean(QuartercircleBean bean) {
        mBean = bean;
    }
}
