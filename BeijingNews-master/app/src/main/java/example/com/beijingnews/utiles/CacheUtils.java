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


//缓存类
public class CacheUtils {
        public static boolean getBoolean(Context context, String key) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            return sp.getBoolean(key,false);
        }


        public static void putBoolean(Context context, String key, boolean value) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            sp.edit().putBoolean(key,value).commit();
        }

        //缓存文本
        public static void putString(Context context, String key, String value) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            sp.edit().putString(key,value).commit();

        }

        //获取文本
        public static String getString(Context context, String key) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            return sp.getString(key,"");//给空字符串，不能给null
        }
}
