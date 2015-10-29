package org.zywx.wbpalmstar.plugin.uexwheel;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexwheel.EUExWheel.OnPopClickListener;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.QuartercircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.ImageUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopButtonView extends RelativeLayout {
	private ImageView imagePlus;
	private TextView buttonText;
	private LinearLayout parent;
    private Context mContext;
	/**
	 * 记录扇形是否打开
	 */
	private boolean menuShowStatus = false;
	private QuartercircleBean bean;
	private int width;

    public PopButtonView(Context context) {
        super(context);
    }

    public PopButtonView(Context context, int width) {
        super(context);
        this.width = width;
        this.mContext = context;
        init();
    }

    public void setMenuShowStatus(boolean menuShowStatus) {
        this.menuShowStatus = menuShowStatus;
    }

	private void init() {
        RelativeLayout firstView;
        ImageView image;
		int myViewID = EUExUtil.getResLayoutID("plugin_uexwheel2_first");
		if (myViewID <= 0) {
			Toast.makeText(mContext,
					"找不到名为:plugin_uexwheel2_first的layout文件!",
					Toast.LENGTH_LONG).show();
			return;
		}
		firstView = (RelativeLayout) ViewGroup.inflate(mContext, myViewID, null);
		parent = (LinearLayout) firstView.findViewById(EUExUtil.getResIdID("ib_parent"));
		int imageViewID = EUExUtil.getResIdID("ib_open");
		int buttonId = EUExUtil.getResIdID("ib_button_txt");
		image = (ImageView) firstView.findViewById(imageViewID);
		buttonText = (TextView) firstView.findViewById(buttonId);
	    imagePlus = image;
		addView(firstView);
	}

	public void setListener(final OnPopClickListener listener) {
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (EUExWheel.isAnimationRun) {
					return;
				}
				try {
					if (!menuShowStatus) {
						menuShowStatus = true;
                        listener.onClickOpen(imagePlus, buttonText);
					} else {
						menuShowStatus = false;
                        listener.onClickClose(imagePlus, buttonText);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    public void setDataBean(QuartercircleBean bean) {
        this.bean = bean;
        updateData();
    }

    private void updateData() {
        buttonText.getLayoutParams().width = width / 2;
        imagePlus.getLayoutParams().width = width / 2;
        imagePlus.getLayoutParams().height = width / 2;
        ImageUtil.setBackgroundBitmap(mContext, parent, bean.getSubBg());
        parent.getLayoutParams().width = width;
        parent.getLayoutParams().height = width;
        buttonText.setText(bean.getOpenTitle());
        imagePlus.setImageBitmap(bean.getOpenImg());
        String color = bean.getTextColor();
        if(!TextUtils.isEmpty(color)){
            try {
                buttonText.setTextColor(Color.parseColor(color));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
