package example.com.beijingnews;

import android.app.Application;

import org.xutils.x;

import example.com.beijingnews.volley.VolleyManager;

/**
 * Created by Administrator on 2017/10/23.
 */

    //xUtils3的初始化
public class BeijingNewsApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
        //初始化Volley
        VolleyManager.init(this);
    }
}
