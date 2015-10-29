package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.SemicircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SemicircleMenuView extends RelativeLayout implements OnTouchListener {

	private int bgWidth, bgHeight;
	private Button bt_text;
	private EUExWheel myEuexWheel;
	private SemicircleBean bean;
	private int mCount;
    private Context mContext;

    public SemicircleMenuView(Context context) {
        super(context);
    }

    public SemicircleMenuView(Context context, int width, SemicircleBean data) {
        super(context);
        this.mContext = context;
        this.bean = data;
        init(width);
    }

	public void setMyEuexWheel(EUExWheel myEuexWheel) {
		this.myEuexWheel = myEuexWheel;
	}

	private void init(int width) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
	    initData(width);
	    if(mCount < 3){
	        Toast.makeText(mContext,
	                "传入参数错误", Toast.LENGTH_SHORT).show();
	        return;
	    }
	    switch (mCount) {
        case 3:
            inflater.inflate(EUExUtil
                            .getResLayoutID("plugin_uexwheel1_3"),
                    this, true);
            initView(3);
            break;
        case 4:
            inflater.inflate(EUExUtil
                            .getResLayoutID("plugin_uexwheel1_4"),
                    this, true);
            initView(4);
            break;
        case 5:
            inflater.inflate(EUExUtil
                            .getResLayoutID("plugin_uexwheel1_5"),
                    this, true);
            initView(5);
            break;
        default:
            inflater.inflate(EUExUtil
                            .getResLayoutID("plugin_uexwheel1_5"),
                    this, true);
            initView(5);
            break;
        }
	}

	private void initData(int width) {
        if(width <= 0){
            width = getScreenWidth();
        }
        Bitmap bg = bean.getBgImg();
        int w = bg.getWidth();
        int h = bg.getHeight();
        bgWidth = width;
        bgHeight = h * width / w;
        mCount = bean.getData().size();
    }

    private int getScreenWidth() {
        return getScreenDisplayMetrics().widthPixels;
    }
    
    private DisplayMetrics getScreenDisplayMetrics()
    {
      return mContext.getResources().getDisplayMetrics();
    }

    private void initView(int count) {
        LinearLayout rl;
        RelativeLayout mParent;
        ImageView ib_1;
        ImageView ib_2;
        ImageView ib_3;
        ImageView ib_4;
        ImageView ib_5;

        mParent = (RelativeLayout) findViewById(EUExUtil.getResIdID("rl"));
        mParent.getLayoutParams().width = bgWidth;
        mParent.getLayoutParams().height = bgHeight;
        ImageUtil.setBackgroundBitmap(mContext, mParent, bean.getBgImg());
        bt_text = (Button) findViewById(EUExUtil.getResIdID("bt_text"));
        bt_text.setText("请选择：");
        rl = (LinearLayout) findViewById(EUExUtil.getResIdID("rl_bg"));
        rl.getLayoutParams().height = bgHeight;
        rl.getLayoutParams().width = bgWidth;
        rl.setOnTouchListener(this);
        
		ib_1 = (ImageView) findViewById(EUExUtil.getResIdID("c1"));
        ib_1.setImageBitmap(bean.getIcon(0));
        ib_1.setTag(0);
        ib_1.setOnTouchListener(this);
        
		ib_2 = (ImageView) findViewById(EUExUtil.getResIdID("c2"));
        ib_2.setImageBitmap(bean.getIcon(1));
        ib_2.setTag(1);
        ib_2.setOnTouchListener(this);
        
		ib_3 = (ImageView) findViewById(EUExUtil.getResIdID("c3"));
        ib_3.setImageBitmap(bean.getIcon(2));
        ib_3.setTag(2);
        ib_3.setOnTouchListener(this);
        
        if(count > 3){
            ib_4 = (ImageView) findViewById(EUExUtil.getResIdID("c4"));
            ib_4.setImageBitmap(bean.getIcon(3));
            ib_4.setTag(3);
            ib_4.setOnTouchListener(this);
        }
        
        if(count > 4){
            ib_5 = (ImageView) findViewById(EUExUtil.getResIdID("c5"));
            ib_5.setImageBitmap(bean.getIcon(4));
            ib_5.setTag(4);
            ib_5.setOnTouchListener(this);
        }
        
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == EUExUtil.getResIdID("c1")) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
			    
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				myEuexWheel.getTag(v.getTag().toString());
			}
		} else if (v.getId() == EUExUtil.getResIdID("c2")) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				myEuexWheel.getTag(v.getTag().toString());
			}
		} else if (v.getId() == EUExUtil.getResIdID("c3")) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				myEuexWheel.getTag(v.getTag().toString());
			}
		} else if (v.getId() == EUExUtil.getResIdID("c4")) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				myEuexWheel.getTag(v.getTag().toString());
			}
		} else if (v.getId() == EUExUtil.getResIdID("c5")) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
				bt_text.setText(bean.getTitle(Integer.valueOf(v.getTag().toString())));
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				myEuexWheel.getTag(v.getTag().toString());
			}
		}
		return true;
	}

    public void clean() {

    }
}
