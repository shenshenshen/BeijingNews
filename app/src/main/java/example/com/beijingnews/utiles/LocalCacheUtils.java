package example.com.beijingnews.utiles;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */

class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    //根据Url获取图片
    public Bitmap getBitmapFroml(String imageUrl) {
        //判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //保存在/mnt/sdcard/beijingews/http://http://192.168.43.233:8080/xxxxx.png
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                //保存在/mnt/sdcard/beijingews/lkajdsflkjasdlkf
                File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews",fileName);

                if(file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap != null){
                        memoryCacheUtils.putBibtmap(imageUrl,bitmap);
                        LogUtil.e("从本地保存到内存中");
                    }
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片获取失败");
            }
        }
        return null;
    }

    public void putBitmap(String imageUrl, Bitmap bitmap) {
        //判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //保存在/mnt/sdcard/beijingews/http://http://192.168.43.233:8080/xxxxx.png
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                //保存在/mnt/sdcard/beijingews/lkajdsflkjasdlkf
                File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews",fileName);
                File parentFile = file.getParentFile();

                if (!parentFile.exists()){
                    parentFile.mkdirs();
                }

                if (!file.exists()){
                    file.createNewFile();
                }

                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地缓存失败");
            }
        }
    }
}
