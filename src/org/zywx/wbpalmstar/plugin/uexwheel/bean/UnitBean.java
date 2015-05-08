package org.zywx.wbpalmstar.plugin.uexwheel.bean;

import android.graphics.Bitmap;

public class UnitBean {

    private String title;
    private Bitmap icon;
    private boolean isValid = true;

    public boolean isValid() {
		return isValid;
	}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Bitmap getIcon() {
        return icon;
    }
    public void setIcon(Bitmap icon) {
    	if(icon == null){
    		this.isValid = false;
    		return;
    	}
        this.icon = icon;
    }
    
    
}
