package org.zywx.wbpalmstar.plugin.uexwheel.bean;

import java.util.List;

import android.graphics.Bitmap;

public class SemicircleBean {

    public static final String DATA_TAG = "data";
    public static final String TITLE_TAG = "title";
    public static final String ICON_TAG = "image";
    public static final String BGIMG_TAG = "background";
    
    private Bitmap bgImg;
    private List<UnitBean> data;
    private boolean isValid = true;

    public boolean isValid() {
		return isValid;
	}
    public List<UnitBean> getData() {
        return data;
    }
    public void setData(List<UnitBean> data) {
    	for (int i = 0; i < data.size(); i++) {
    		if(!data.get(i).isValid()){
        		this.isValid = false;
        		return;
        	}
		}
        this.data = data;
    }
    public Bitmap getBgImg() {
        return bgImg;
    }
    public void setBgImg(Bitmap bgImg) {
    	if(bgImg == null){
    		this.isValid = false;
    		return;
    	}
        this.bgImg = bgImg;
    }
    public String getTitle(int index) {
        return getData().get(index).getTitle();
    }
    public Bitmap getIcon(int index) {
        return getData().get(index).getIcon();
    }
}
