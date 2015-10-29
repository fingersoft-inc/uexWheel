package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.plugin.uexwheel.EUExWheel.OnMenuSelectListener;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.QuartercircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.QuartercircleAnimation;
import org.zywx.wbpalmstar.plugin.uexwheel.util.TurnplateView;
import org.zywx.wbpalmstar.plugin.uexwheel.util.TurnplateView.OnTurnplateListener;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SectorMenuView extends RelativeLayout implements OnTurnplateListener {

    private int width, height;

    private RelativeLayout layout;
    private RelativeLayout layout_in;
    private OnMenuSelectListener mSelectedListener;
    private QuartercircleBean mBean;
    private Context mContext;

    public SectorMenuView(Context context) {
        super(context);
    }

    public SectorMenuView(Context context, int width,
                          int height, QuartercircleBean data) {
        super(context);
        this.mBean = data;
        this.width = width;
        this.height = height;
        this.mContext = context;
        initView();
    }

    private void initView() {
        layout = new RelativeLayout(mContext);
        layout_in = new RelativeLayout(mContext);
        init();
        addView(layout);
    }

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

    public void setOnMenuSelectedListener(OnMenuSelectListener listener) {
        mSelectedListener = listener;
    }

    private void init() {
        TurnplateView view;
        ImageView iv_bg;
        view = new TurnplateView(mContext, width, height,
                2 * width / 3, mBean.getData());
        view.setOnTurnplateListener(this);
        // 背景图片加载
        iv_bg = new ImageView(mContext);
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

    public void clean() {

    }
}
