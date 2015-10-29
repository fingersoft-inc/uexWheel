package org.zywx.wbpalmstar.plugin.uexwheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.QuartercircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.SemicircleBean;
import org.zywx.wbpalmstar.plugin.uexwheel.bean.UnitBean;
import org.zywx.wbpalmstar.plugin.uexwheel.util.ImageUtil;
import org.zywx.wbpalmstar.plugin.uexwheel.util.SecondView.OnTurnplateListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EUExWheel extends EUExBase {

    private static final String CALLBACK_SELECT = "uexWheel.cbSelect";
    private static final String CALLBACK_ONCLICK = "uexWheel.onClick";
    private static int left = 0, top = 0, width = -1, height = -1, btnWidth = 0;
    private static boolean isQuartercircleOpen = false;// 菜单打开状态
    private static boolean isCircleOpen = false;// 菜单打开状态
    private static boolean isCircleIconOpen = false;//圆形控件是否显示
    private static Context sContext;
    private static WindowManager wm;
    private String mDataJson = "";
    private static OnTurnplateListener mListener;

    /**
     * 记录是否正在进行动画（锁定按钮点击）
     */
    public static boolean isAnimationRun = false;
    /**
     * 记录悬浮按钮是否显示
     */
    private static boolean buttonShowStatus = false;

    /**
     * 记录扇形页面是否打开
     */
    private static boolean isSectorOpen = false;

    /**
     * 悬浮按钮点击回调
     */
    public OnPopClickListener onPopClickListener;

    private static QuartercircleBean mQuartercircleBean;
    private CircleBean mCircleBean;
    private SemicircleBean mSemicircleBean;
    public static RelativeLayout.LayoutParams circleLp;
    public static CircleCallback circleCallback;

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN_SEMICIRCLE = 1;
    private static final int MSG_OPEN_CIRCLE = 2;
    private static final int MSG_OPEN_QUARTERCIRCLE = 3;
    private static final int MSG_CLOSE_SEMICIRCLE = 4;
    private static final int MSG_CLOSE_CIRCLE = 5;
    private static final int MSG_CLOSE_QUARTERCIRCLE = 6;

    private PopButtonView popButtonView;//TAG_QUARTERCIRCLE_POPMENU
    private SectorMenuView sectorMenuView;//TAG_QUARTERCIRCLE_SECTOR
    private SemicircleMenuView semicircleMenuView;//TAG_SEMICIRCLE
    private FirstView firstView;//TAG_CIRCLE_TAB

    public EUExWheel(Context context, EBrowserView view) {
        super(context, view);
        onPopClickListener = new OnPopClickListener() {
            @Override
            public void onClickOpen(ImageView imagePlus, TextView tv) {
                try {
                    openPopMenu(imagePlus, tv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClickClose(ImageView imagePlus, TextView tv) {
                try {
                    sectorMenuView.startAnimationOUT(imagePlus, tv);
                    isSectorOpen = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void openSemicircle(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN_SEMICIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openSemicircleMsg(String[] parm) {
        resetParams();
        closeSemicircle();
        if (isQuartercircleOpen) {
            // 如果菜单已经开启，则不再启动
            return;
        }
        isQuartercircleOpen = true;
        sContext = mContext;
        if (parm.length < 5) {
            return;
        }
        try {
            if(!TextUtils.isEmpty(parm[0]) && TextUtils.isDigitsOnly(parm[0])){
                left = Integer.parseInt(parm[0]);
            }
            if(!TextUtils.isEmpty(parm[1]) && TextUtils.isDigitsOnly(parm[1])){
                top = Integer.parseInt(parm[1]);
            }
            if(!TextUtils.isEmpty(parm[2]) && TextUtils.isDigitsOnly(parm[2])){
                width = Integer.parseInt(parm[2]);
            }
            if(!TextUtils.isEmpty(parm[3]) && TextUtils.isDigitsOnly(parm[3])){
                height = Integer.parseInt(parm[3]);
            }
            mDataJson = parm[4];
            if(TextUtils.isEmpty(mDataJson)){
                errorCallback(0, 0, "传入参数错误");
                return;
            }
        } catch (Exception e) {
            errorCallback(0, 0, "传入参数错误");
            return;
        }
        try {
            getSemicircleBeanData(mDataJson);
            if(!mSemicircleBean.isValid()){
                errorCallback(0, 0, "传入参数错误");
                return;
            }
            semicircleMenuView = new SemicircleMenuView(mContext,
                    width, mSemicircleBean);
            // 将EuexWheel的Context设置给要跳转到的activity
            semicircleMenuView.setMyEuexWheel(EUExWheel.this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    width, height);
            lp.leftMargin = left;
            lp.topMargin = top;
            if (semicircleMenuView != null) {
                //startAnimationIN((ViewGroup) view, 500);
                addView2CurrentWindow(semicircleMenuView, lp);
                //addTopLayer(view, lp);
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCircle(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN_CIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openCircleMsg(String[] parm) {
        resetParams();
        getCallback();
        closeCircle();
        if (isCircleOpen) {
            // 防止被多次打开
            return;
        }
        isCircleOpen = true;
        if (parm.length < 5) {
            return;
        }
        try {
            if(!TextUtils.isEmpty(parm[0]) && TextUtils.isDigitsOnly(parm[0])){
                left = Integer.parseInt(parm[0]);
            }
            if(!TextUtils.isEmpty(parm[1]) && TextUtils.isDigitsOnly(parm[1])){
                top = Integer.parseInt(parm[1]);
            }
            if(!TextUtils.isEmpty(parm[2]) && TextUtils.isDigitsOnly(parm[2])){
                width = Integer.parseInt(parm[2]);
            }
            if(!TextUtils.isEmpty(parm[3]) && TextUtils.isDigitsOnly(parm[3])){
                height = Integer.parseInt(parm[3]);
            }
            mDataJson = parm[4];
            if(TextUtils.isEmpty(mDataJson)){
                errorCallback(0, 0, "传入参数错误");
                return;
            }
        } catch (Exception e) {
            errorCallback(0, 0, "传入参数错误");
            return;
        }
        getCircleBeanData(mDataJson);
        if(!mCircleBean.isValid()){
            errorCallback(0, 0, "传入参数错误");
            return;
        }
        int type = 0;
        if(parm.length > 5){
            type = Integer.parseInt(parm[5]);
        }
        mCircleBean.setType(type);
        circleLp = new RelativeLayout.LayoutParams(width, height);
        circleLp.leftMargin = left;
        circleLp.topMargin = top;
        firstView = new FirstView(mContext, mCircleBean);
        try {
            addView2CurrentWindow(firstView, circleLp);
            //addCircleTopLayer(view, circleLp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openQuartercircle(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN_QUARTERCIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openQuartercircleMsg(String[] parm) {
        resetParams();
        closeQuartercircle();
        if (!buttonShowStatus) {
            buttonShowStatus = true;
            if (parm.length < 5) {
                return;
            }
            try {
                if(!TextUtils.isEmpty(parm[0]) && TextUtils.isDigitsOnly(parm[0])){
                    left = Integer.parseInt(parm[0]);
                }
                if(!TextUtils.isEmpty(parm[1]) && TextUtils.isDigitsOnly(parm[1])){
                    top = Integer.parseInt(parm[1]);
                }
                if(!TextUtils.isEmpty(parm[2]) && TextUtils.isDigitsOnly(parm[2])){
                    width = Integer.parseInt(parm[2]);
                }
                if(!TextUtils.isEmpty(parm[3]) && TextUtils.isDigitsOnly(parm[3])){
                    height = Integer.parseInt(parm[3]);
                }
                mDataJson = parm[4];
                if(parm.length > 5 && !TextUtils.isEmpty(parm[5]) && TextUtils.isDigitsOnly(parm[5])){
                    btnWidth = Integer.parseInt(parm[5]);
                }
                if(TextUtils.isEmpty(mDataJson)){
                    errorCallback(0, 0, "传入参数错误");
                    return;
                }
            } catch (Exception e) {
                errorCallback(0, 0, "传入参数错误");
                return;
            }
            getQuartercircleData();
            if(!mQuartercircleBean.isValid()){
                errorCallback(0, 0, "传入参数错误");
                return;
            }
            if(btnWidth < 1){
                btnWidth = width / 5;
            }
            popButtonView = new PopButtonView(mContext, btnWidth);
            popButtonView.setListener(onPopClickListener);
            popButtonView.setDataBean(mQuartercircleBean);
            int x = width + left - btnWidth;
            int y = height + top - btnWidth;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    btnWidth, btnWidth);
            lp.leftMargin = x;
            lp.topMargin = y;
            //addView2CurrentWindow(view, lp);
            addTopLayer(popButtonView, x, y, btnWidth, btnWidth);
        }
    }

    public void closeSemicircle(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE_SEMICIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeSemicircleMsg(String[] params) {
        closeSemicircle();
    }

    public void closeCircle(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE_CIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeCircleMsg(String[] params) {
        closeCircle();
    }

    public void closeQuartercircle(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE_QUARTERCIRCLE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeQuartercircleMsg(String[] params) {
        closeQuartercircle();
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN_SEMICIRCLE:
                openSemicircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_OPEN_CIRCLE:
                openCircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_OPEN_QUARTERCIRCLE:
                openQuartercircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE_SEMICIRCLE:
                closeSemicircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE_CIRCLE:
                closeCircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE_QUARTERCIRCLE:
                closeQuartercircleMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    /**
     * 打开扇形菜单
     */
    private void openPopMenu(final ImageView imagePlus, final TextView tv) {
        int menuWidth = width;
        int menuHeight = height;
        sectorMenuView = new SectorMenuView(mContext, menuWidth, menuHeight,
                mQuartercircleBean);
        sectorMenuView
                .setOnMenuSelectedListener(new OnMenuSelectListener() {

                    @Override
                    public void onSelect(int flag) {
                        sectorMenuView.startAnimationOUT(imagePlus, tv);
                        popButtonView.setMenuShowStatus(false);
                        String js = SCRIPT_HEADER + "if(" + CALLBACK_SELECT + "){"
                                + CALLBACK_SELECT + "(" + flag + SCRIPT_TAIL;
                        mBrwView.loadUrl(js);
                    }

                    @Override
                    public void onClose() {
                        removeViewFromCurrentWindow(sectorMenuView);
                        isSectorOpen = false;
                    }
                });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                menuWidth, menuHeight);
        lp.leftMargin = left;
        lp.topMargin = top;
        addView2CurrentWindow(sectorMenuView, lp);
        sectorMenuView.startAnimationIn(imagePlus, tv);
        isSectorOpen = true;
    }

    /**
     * 绕开引擎的添加view的方法，防止被webview控制大小
     *
     * @param child
     * @param parms
     */
    private void addView2CurrentWindow(View child,
                                       RelativeLayout.LayoutParams parms) {
        int l = (int) (parms.leftMargin);
        int t = (int) (parms.topMargin);
        int w = parms.width;
        int h = parms.height;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
        lp.gravity = Gravity.NO_GRAVITY;
        lp.leftMargin = l;
        lp.topMargin = t;
        adptLayoutParams(parms, lp);
        mBrwView.addViewToCurrentWindow(child, lp);
    }

    // 添加到最上层
    /**
     * 添加扇形开关
     *
     * @param view
     * @param left
     * @param top
     * @param width
     * @param height
     */
    private void addTopLayer(View view, int left, int top, int width, int height) {
        wm = ((Activity) mContext).getWindowManager();
        WindowManager.LayoutParams parms = new WindowManager.LayoutParams();
        parms.height = height;
        parms.width = width;
        parms.format = PixelFormat.TRANSPARENT;
        parms.x = left;
        parms.y = top;
        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        parms.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        view.setLayoutParams(parms);
        wm.addView(view, parms);
    }

    private void resetParams() {
        left = 0;
        top = 0;
        width = -1;
        height = -1;
    }

    private void getQuartercircleData() {
        mQuartercircleBean = new QuartercircleBean();
        try {
            JSONObject json = new JSONObject(mDataJson);
            Bitmap rootBg = ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(QuartercircleBean.ROOTBG_TAG));
            Bitmap subBg = ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(QuartercircleBean.SUBBG_TAG));
            Bitmap closeImg = ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(QuartercircleBean.CLOSEIMG_TAG));
            Bitmap openImg = ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(QuartercircleBean.OPENIMG_TAG));
            String textColor = json.getString(QuartercircleBean.TEXTCOLOR_TAG);
            mQuartercircleBean.setRootBg(rootBg);
            mQuartercircleBean.setSubBg(subBg);
            mQuartercircleBean.setCloseImg(closeImg);
            mQuartercircleBean.setOpenImg(openImg);
            mQuartercircleBean.setOpenTitle(json.getString(QuartercircleBean.OPENTITLE_TAG));
            mQuartercircleBean.setCloseTitle(json.getString(QuartercircleBean.CLOSETITLE_TAG));
            mQuartercircleBean.setTextColor(textColor);
            List<UnitBean> list = new ArrayList<UnitBean>();
            JSONArray array = json.getJSONArray(QuartercircleBean.DATA_TAG);
            for (int i = 0; i < array.length(); i++) {
                UnitBean item = new UnitBean();
                JSONObject itemJson = (JSONObject) array.opt(i);
                item.setTitle(itemJson.get(QuartercircleBean.TITLE_TAG).toString());
                item.setIcon(ImageUtil.getLocalImg(mContext,mBrwView,
                        itemJson.get(QuartercircleBean.ICON_TAG).toString()));
                list.add(item);
            }
            mQuartercircleBean.setData(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCircleBeanData(String jsonData){
        mCircleBean = new CircleBean();
        try {
            JSONObject json = new JSONObject(jsonData);
            mCircleBean.setButton(ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(CircleBean.BUTTON_TAG)));
            mCircleBean.setMenuBg(ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(CircleBean.MENUBG_TAG)));
            mCircleBean.setSubMenuBg(ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(CircleBean.SUBMENUBG_TAG)));
            mCircleBean.setIconLeft(ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(CircleBean.ICON_LEFT_TAG)));
            mCircleBean.setIconSelect(ImageUtil.getLocalImg(mContext,mBrwView,
                    json.getString(CircleBean.ICON_SELECT_TAG)));
            mCircleBean.setBgColor(json.getString(CircleBean.BGCOLOR_TAG));
            JSONArray jsonArray = json.getJSONArray(CircleBean.DATA_TAG);
            Bitmap[] tabs = new Bitmap[jsonArray.length()];
            List<HashMap<String,Object>> tabIcons = new ArrayList<HashMap<String,Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, Object> data = new HashMap<String, Object>();
                JSONObject itemjson = (JSONObject) jsonArray.opt(i);
                String iconTab = itemjson.getString(CircleBean.TABICON_TAG);
                tabs[i] = ImageUtil.getLocalImg(mContext,mBrwView,
                        iconTab);
                JSONArray iconsArray = itemjson.getJSONArray(CircleBean.ICONS_TAG);
                int length = iconsArray.length();
                Bitmap[] icons = new Bitmap[length];
                for (int j = 0; j < length; j++) {
                    icons[j] = ImageUtil.getLocalImg(mContext,mBrwView,
                            iconsArray.optString(j));
                }
                data.put(CircleBean.ICONS_TAG, icons);
                tabIcons.add(data);
            }
            mCircleBean.setTabs(tabs);
            mCircleBean.setData(tabIcons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSemicircleBeanData(String jsonData) {
        mSemicircleBean = new SemicircleBean();
        try {
            JSONObject json = new JSONObject(jsonData);
            Bitmap bg = ImageUtil.getLocalImg(mContext,mBrwView,
                    json.get(SemicircleBean.BGIMG_TAG).toString());
            mSemicircleBean.setBgImg(bg);
            List<UnitBean> list = new ArrayList<UnitBean>();
            JSONArray array = json.getJSONArray(SemicircleBean.DATA_TAG);
            for (int i = 0; i < array.length(); i++) {
                UnitBean item = new UnitBean();
                JSONObject itemJson = (JSONObject) array.opt(i);
                item.setTitle(itemJson.get(SemicircleBean.TITLE_TAG).toString());
                item.setIcon(ImageUtil.getLocalImg(mContext,mBrwView,
                        itemJson.get(SemicircleBean.ICON_TAG).toString()));
                list.add(item);
            }
            mSemicircleBean.setData(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 添加到最上层
    private void addTopLayer(View view, RelativeLayout.LayoutParams parm) {
        wm = ((Activity) mContext).getWindowManager();
        WindowManager.LayoutParams parms = new WindowManager.LayoutParams();
        parms.height = parm.height;
        parms.width = parm.width;
        parms.format = PixelFormat.TRANSPARENT;
        parms.x = parm.leftMargin;
        parms.y = parm.topMargin;
        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        parms.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        view.setLayoutParams(parms);
        wm.addView(view, parms);
    }

    /**
     * 移除菜单
     */
    private static void removeTopLayer(View view) {
        wm.removeView(view);
        wm = null;
    }

    public void closeSemicircle() {
        if (isQuartercircleOpen && sContext != null) {
            if (semicircleMenuView != null) {
                semicircleMenuView.clean();
                removeViewFromCurrentWindow(semicircleMenuView);
                semicircleMenuView = null;
            }
            isQuartercircleOpen = false;
        }
    }

    private void closeQuartercircle() {
        if(isSectorOpen){
            if (sectorMenuView != null) {
                sectorMenuView.clean();
                removeViewFromCurrentWindow(sectorMenuView);
                sectorMenuView = null;
            }
            isSectorOpen = false;
        }
        if (buttonShowStatus) {
            if (popButtonView != null) {
                removeTopLayer(popButtonView);
                popButtonView = null;
            }
            buttonShowStatus = false;
        }
    }

    public void getTag(String tag) {
        String js = SCRIPT_HEADER + "if(" + CALLBACK_SELECT + "){"
                + CALLBACK_SELECT + "(" + tag + SCRIPT_TAIL;
        mBrwView.loadUrl(js);
        closeSemicircle();
    }

    // clean something
    @Override
    protected boolean clean() {
        return false;
    }

    public void startAnimationIN(ViewGroup viewGroup, int duration) {
        Animation animation;
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        viewGroup.startAnimation(animation);
    }

    // 出动画
    public void startAnimationOUT(final ViewGroup viewGroup, int duration,
                                  int startOffSet) {
        Animation animation;
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        viewGroup.startAnimation(animation);
    }

    public void getCallback() {
        circleCallback = new CircleCallback() {
            @Override
            public void addView(View v, RelativeLayout.LayoutParams parm) {
                isCircleIconOpen = true;
                addView2CurrentWindow(v, parm);
            }

            @Override
            public void removeView(View v) {
                isCircleIconOpen = false;
                removeViewFromCurrentWindow(v);
            }

            @Override
            public void getTag(int i, int j) {
                String js;
                if(i == -1){
                    js = SCRIPT_HEADER + "if(" + CALLBACK_ONCLICK + "){"
                            + CALLBACK_ONCLICK + "(" + 0 + SCRIPT_TAIL;
                }else{
                    js = SCRIPT_HEADER + "if(" + CALLBACK_SELECT + "){"
                            + CALLBACK_SELECT + "(" + i + ", " + j + SCRIPT_TAIL;
                }
                mBrwView.loadUrl(js);
            }
        };
    }

    private void closeCircle() {
        if(isCircleIconOpen){
            if(mListener != null){
                mListener.onPointTouch(-2);
            }
            isCircleIconOpen = false;
        }

        if (isCircleOpen) {
            if (firstView != null) {
                firstView.clean();
                removeViewFromCurrentWindow(firstView);
            }
            isCircleOpen = false;
        }
    }

    // 添加到最上层
    public void addCircleTopLayer1(View view, RelativeLayout.LayoutParams parm) {
        wm = ((Activity) mContext).getWindowManager();
        WindowManager.LayoutParams parms = new WindowManager.LayoutParams();
        parms.height = parm.height / 5;
        parms.width = parm.width;
        parms.format = PixelFormat.TRANSPARENT;
        parms.x = parm.leftMargin;
        parms.y = parm.topMargin;
        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        parms.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wm.addView(view, parms);
    }

    public interface OnMenuSelectListener {
        /**
         * 回调相应数据，并且关闭扇形窗口
         *
         * @param flag
         */
        public void onSelect(int flag);

        /**
         * 关闭扇形窗口
         */
        public void onClose();
    }

    public interface OnPopClickListener {
        public void onClickOpen(ImageView imageView, TextView tv);
        public void onClickClose(ImageView imageView, TextView tv);
    }
    public interface CircleCallback {
        public void addView(View v, RelativeLayout.LayoutParams parm);
        public void removeView(View v);
        public void getTag(int iconIndex, int circleIndex);
    }
    public static void onPointTouchListener(OnTurnplateListener listener) {
        mListener = listener;
    }
}
