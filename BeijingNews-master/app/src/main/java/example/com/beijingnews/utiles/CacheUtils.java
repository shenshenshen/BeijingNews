package example.com.beijingnews.utiles;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.text.NoCopySpan;

import example.com.beijingnews.SplashActivity;
import example.com.beijingnews.activity.GuideActivity;

/**
 * Created by Administrator on 2017/10/18.
 */

public class CacheUtils {
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
}
