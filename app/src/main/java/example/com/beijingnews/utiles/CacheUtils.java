package example.com.beijingnews.utiles;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.NoCopySpan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import example.com.beijingnews.SplashActivity;
import example.com.beijingnews.activity.GuideActivity;

/**
 * Created by Administrator on 2017/10/18.
 */


//缓存类
public class CacheUtils {
        //获取布尔类型
        public static boolean getBoolean(Context context, String key) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            return sp.getBoolean(key,false);
        }

        //缓存布尔类型
        public static void putBoolean(Context context, String key, boolean value) {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            sp.edit().putBoolean(key,value).commit();
        }

        //缓存文本
        public static void putString(Context context, String key, String value) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                try {
                    String fileName = MD5Encoder.encode(key);
                    File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews/files",fileName);
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()){
                        parentFile.mkdirs();
                    }
                    if (!file.exists()){
                        file.createNewFile();
                    }
                    //保存文本数据
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(value.getBytes());
                    fileOutputStream.close();//记得关闭流
                    LogUtil.e("用文件缓存String");
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("String数据缓存失败");
                }
            }else {
                SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
                sp.edit().putString(key,value).commit();
            }
        }

        //获取文本
        public static String getString(Context context, String key) {
            String result = "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                try {
                    String fileName = MD5Encoder.encode(key);
                    File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews/files",fileName);
                    if (file.exists()){
                        FileInputStream fileInputStream = new FileInputStream(file);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) != -1 ){//读文件的长度
                            stream.write(buffer,0,length);//写到这个流里面
                        }
                        fileInputStream.close();//记得关闭流
                        stream.close();
                         result = stream.toString();
                        LogUtil.e("用文件读取String");
                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("获取缓存String失败");
                }
            }else {
                SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
                return sp.getString(key,"");//给空字符串，不能给null
            }
            return result;
        }
}
