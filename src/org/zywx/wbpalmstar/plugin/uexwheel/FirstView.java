package org.zywx.wbpalmstar.plugin.uexwheel;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.CircleAnimation;
import org.zywx.wbpalmstar.plugin.uexwheel.util.ImageUtil;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView.OnTurnplateListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FirstView extends RelativeLayout implements OnClickListener,
		OnTurnplateListener {

    private LinearLayout rl_bg;
	private LinearLayout rl_bg1;
	private HorizontalScrollView mSroll_bg;
	private LinearLayout rl_bg2;
	private Boolean flag = true;
	private List<LinearLayout> linears;
	private View secondview;
	private CircleAnimation mAnimation;
	private View translucentView;
	private LayoutParams mLp;
	private CircleBean bean;
	private int mTabCount;
    private Bitmap[] mTabs;
    private int mIconW, mIconH;
    private int mSize, mTabWidth;
    private final static int MAXRADIUS = 300;
    private final static int maxSize = 400;
    private int mCurrentIndex = -1;
    private final static int mBaseIconCount = 5;
    private boolean mIsCircle = true;
    private ImageView icon_left;
    private CircleParams params;
    private int mSecondViewW,mSecondViewH;
    private Context mContext;

    public FirstView(Context context, CircleBean data) {
        super(context);
        this.mContext = context;
        this.bean = data;
        init();
    }

	private void init() {
        LayoutInflater.from(mContext).inflate(EUExUtil.getResLayoutID("plugin_uexwheel3_main"),
                this, true);
        mLp = EUExWheel.circleLp;
        mAnimation = new CircleAnimation();
        if(bean.getType() == 0){
            mIsCircle = true;
        }else{
            mIsCircle = false;
        }
		initView();
        params =  getCircleParams();
		rl_bg.setVisibility(View.GONE);

        translucentView = new BackGroundView(mContext, bean.getBgColor());
		RelativeLayout.LayoutParams lParam = getTranslucentViewlp();
        EUExWheel.circleCallback.addView(translucentView,
                lParam);
		translucentView.setVisibility(View.GONE);
        mAnimation.setLp(getAnimationlp());
        setViewDisplayOrHide(true);
	}

	public void clean() {
		flag = true;
		secondview = null;
	}

	private void initView() {
	    mTabs = bean.getTabs();
	    mTabCount = mTabs.length;
	    int count = mTabCount > mBaseIconCount ? mBaseIconCount : mTabCount;
        int maxWidth = mLp.width / (count + 1);
        int maxHeight = mLp.height / 5;
        int min = maxHeight < maxWidth ? maxHeight : maxWidth;
        mSize = min < maxSize ? min : maxSize;
        mTabWidth = (mLp.width - mSize - mSize / 3) / count;
        int iconWidth = (int)(mSize*0.6);
        int iconHeight = (int)(mSize*0.6);
        mIconW = maxWidth < iconWidth ? maxWidth : iconWidth;
        mIconH = maxHeight < iconHeight ? maxHeight : iconHeight;

        rl_bg = (LinearLayout) findViewById(EUExUtil.getResIdID("rl_bg"));
        icon_left = (ImageView) findViewById(EUExUtil.getResIdID("icon_left"));
        icon_left.getLayoutParams().width = mSize / 3;
        icon_left.getLayoutParams().height = mSize;
        icon_left.setScaleType(ScaleType.FIT_XY);
        ImageUtil.setBackgroundBitmap(mContext, icon_left, bean.getIconLeft());
	    linears = new ArrayList<LinearLayout>();
		rl_bg1 = (LinearLayout) findViewById(EUExUtil.getResIdID("rl_bg1"));
		rl_bg1.getLayoutParams().width = mSize;
		rl_bg1.getLayoutParams().height = mSize;
        ImageUtil.setBackgroundBitmap(mContext, rl_bg1, bean.getButton());
		rl_bg1.setOnClickListener(this);
		rl_bg2 = (LinearLayout) findViewById(EUExUtil.getResIdID("rl_bg2"));
		mSroll_bg = (HorizontalScrollView) findViewById(EUExUtil.getResIdID("scroll_bg"));
		mSroll_bg.getLayoutParams().height = mSize;
		rl_bg2.getLayoutParams().height = mSize;
		ImageUtil.setBackgroundBitmap(mContext, mSroll_bg, bean.getMenuBg());
		for (int i = 0; i < mTabCount; i++) {
		    final int index = i;
            LinearLayout ll = new LinearLayout(mContext);
            ll.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    mTabWidth, mSize);
            ll.setLayoutParams(lp);
            rl_bg2.addView(ll);
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams ivlp = new LinearLayout.LayoutParams(mIconW, mIconH);
            iv.setLayoutParams(ivlp);
            iv.setImageBitmap(mTabs[i]);
            ll.addView(iv);
            ll.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    updateButtonStatus(index);
                }
            });
            linears.add(ll);
        }
		updateButtonStatus(0);
	}

	protected void updateButtonStatus(int index) {
	    if(mCurrentIndex == index){
	        return;
	    }
	    mCurrentIndex = index;
	    switch (index) {
            case 0:
                ImageUtil.setBackgroundBitmap(mContext, linears.get(index), bean.getIconSelect());
                for (int i = 1; i < mTabCount; i++) {
                    ImageUtil.setBackgroundBitmap(mContext, linears.get(i), null);
                }
                break;
    
            default:
                for (int i = 0; i < mTabCount; i++) {
                    if(index == i){
                        ImageUtil.setBackgroundBitmap(mContext, linears.get(i), bean.getIconSelect());
                    }else{
                        ImageUtil.setBackgroundBitmap(mContext, linears.get(i), null);
                    }
                }
                break;
        }
	    changeViewAnim();
    }

    @SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v.getId() == EUExUtil.getResIdID("rl_bg1")) {
			if (flag) {
			    setViewDisplayOrHide(true);
			} else {
			    setViewDisplayOrHide(false);
			}
		}
	}

    private void setViewDisplayOrHide(boolean isDisplay){
        if(isDisplay){
            secondview = getSecondView(mCurrentIndex);
            rl_bg.setVisibility(View.VISIBLE);
            mAnimation.startAnimationIN(rl_bg, 500);
            mAnimation.startAnimationAdd((ViewGroup) secondview, 500, 0);
            if (translucentView.getVisibility() != View.VISIBLE) {
                translucentView.setVisibility(View.VISIBLE);
            }
            RelativeLayout.LayoutParams lparm = getSecondViewlp();
            EUExWheel.circleCallback.addView(secondview, lparm);
            flag = false;
        }else{
            mAnimation.startAnimationRemove((ViewGroup) secondview, 200, 0);
            mAnimation.startAnimationOUT(rl_bg, 500, 0);
            //EUExWheel.mgr.destroyActivity(EUExWheel.TAG_CIRCLE_CIRCLE, true);
            EUExWheel.circleCallback.removeView(secondview);
            //EUExWheel.circleCallback.removeView(translucentView);
            if (translucentView.getVisibility() == View.VISIBLE) {
                translucentView.setVisibility(View.GONE);
            }
            flag = true;
            secondview = null;
        }
    }
    
    private View getSecondView(int index){
        // 获取下一个activity的view
        View view = new SecondBaseView(mContext, this, bean.getMenuIcons(index),
                bean.getSubMenuBg(), params);
        EUExWheel.onPointTouchListener(this);
        return view;
    }
    

    private CircleParams getCircleParams() {
        CircleParams param = new CircleParams();
        int w = mLp.width;
        int h = mLp.height - mSize;
        int size = w < h ? w : h;
        int radius = w * 2 / 7 < h / 2 ? w * 2 / 7 : h / 2;
        int px = ImageUtil.dip2px(mContext, MAXRADIUS);
        if(mIsCircle){
            param.radius = size * 2 / 7 < px ? size * 2 / 7 : px;
            mSecondViewW = param.radius * 7 / 2;
            mSecondViewH = mSecondViewW;
            param.pointY = mSecondViewH / 2;
        }else{
            param.radius = radius < px ? radius : px;
            mSecondViewW = param.radius * 3;
            mSecondViewH = param.radius * 5 / 3;
            param.pointY = mSecondViewH;
        }
        param.pointX = mSecondViewW / 2;
        return param;
    }

    private RelativeLayout.LayoutParams getTranslucentViewlp(){
        RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
                mLp.width, mLp.height);
        lparm.leftMargin = mLp.leftMargin;
        lparm.topMargin = mLp.topMargin;
        return lparm;
    }

    private RelativeLayout.LayoutParams getSecondViewlp(){
        RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
                mSecondViewW, mSecondViewH);
        lparm.leftMargin = mLp.leftMargin + mLp.width/2 - mSecondViewW/2;
        lparm.topMargin = mLp.topMargin + mLp.height - mSecondViewH;
        return lparm;
    }

    private LayoutParams getAnimationlp(){
        LayoutParams lp;
        if(mIsCircle){
            lp = new LayoutParams(getSecondViewlp().width, getSecondViewlp().height / 2);
        }else{
            lp = new LayoutParams(getSecondViewlp().width, getSecondViewlp().height);
        }
        return lp;
    } 

	// 点击导航的视图切换动画
	public void changeViewAnim() {
	    if(secondview == null){
	        return;
	    }
		startAnimationRemoveThenAdd(200, 0);
	}

    public void startAnimationRemoveThenAdd(final int duration, int startOffSet) {
        Animation animation;
        //EUExWheel.mgr.destroyActivity(EUExWheel.TAG_CIRCLE_CIRCLE, true);
        EUExWheel.circleCallback.removeView(secondview);
        secondview = getSecondView(mCurrentIndex);
        EUExWheel.circleCallback.addView(secondview, getSecondViewlp());
        animation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                Animation.ABSOLUTE, getAnimationlp().width / 2, Animation.ABSOLUTE,
                getAnimationlp().height);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        animation.setStartOffset(200);
        secondview.startAnimation(animation);
    }

	/**
	 * 移除轮盘菜单
	 */
	public void removeView() {
		mAnimation.startAnimationOUT(rl_bg, 500, 0);
		mAnimation.startAnimationRemove((ViewGroup) secondview, 200, 0);
		//EUExWheel.mgr.destroyActivity(EUExWheel.TAG_CIRCLE_CIRCLE, true);
		EUExWheel.circleCallback.removeView(secondview);
		EUExWheel.circleCallback.removeView(translucentView);
		flag = true;
		secondview = null;
	}

	@Override
	public void onPointTouch(int index) {
	    if(index == -1){
	        EUExWheel.circleCallback.getTag(-1, -1);
	    }else if(index == -2){
	        removeView();
	    }else{
	        EUExWheel.circleCallback.getTag(mCurrentIndex, index);
	    }
	}

    public class CircleParams{
        int pointX;
        int pointY;
        int radius;
    }

}
