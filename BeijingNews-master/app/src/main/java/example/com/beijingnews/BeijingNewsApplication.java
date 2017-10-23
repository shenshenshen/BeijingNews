package example.com.beijingnews;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/10/23.
 */

public class BeijingNewsApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
    }
}
