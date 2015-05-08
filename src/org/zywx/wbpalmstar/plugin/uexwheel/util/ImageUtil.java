package org.zywx.wbpalmstar.plugin.uexwheel.util;

import java.io.IOException;
import java.io.InputStream;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ImageUtil {

    public static Bitmap getLocalImg(Context ctx, EBrowserView eBrw, String imgUrl) {
        
        if (imgUrl == null || imgUrl.length() == 0) {
            return null;
        }
        String url = BUtility.makeRealPath(
                BUtility.makeUrl(eBrw.getCurrentUrl(), imgUrl),
                eBrw.getCurrentWidget().m_widgetPath,
                eBrw.getCurrentWidget().m_wgtType);
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            if (url.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
                is = BUtility.getInputStreamByResPath(ctx, url);
                bitmap = BitmapFactory.decodeStream(is);
            } else if (url.startsWith(BUtility.F_FILE_SCHEMA)) {
                url = url.replace(BUtility.F_FILE_SCHEMA, "");
                bitmap = BitmapFactory.decodeFile(url);
            } else if (url.startsWith(BUtility.F_Widget_RES_path)) {
                try {
                    is = ctx.getAssets().open(url);
                    if (is != null) {
                        bitmap = BitmapFactory.decodeStream(is);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (url.startsWith("/")) {
                bitmap = BitmapFactory.decodeFile(url);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static void setBackgroundBitmap(Context ctx, View view, Bitmap bitmap) {
        Drawable d = null;
        if(bitmap != null){
            d = new BitmapDrawable(ctx.getResources(), bitmap);
        }
        int version = Build.VERSION.SDK_INT;
        if(version < 16){
            view.setBackgroundDrawable(d);
        }else{
            view.setBackground(d); 
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
