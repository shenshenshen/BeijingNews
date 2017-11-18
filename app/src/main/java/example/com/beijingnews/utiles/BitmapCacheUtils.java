package example.com.beijingnews.utiles;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * Created by Administrator on 2017/11/9.
 */

public class BitmapCacheUtils {

    //网络缓存工具
    private NetCacheUtils netCacheUtils;

    //构造方法初始化
    public BitmapCacheUtils(Handler handler) {
        netCacheUtils = new NetCacheUtils(handler);
    }

    public Bitmap getBitmap(String imageUrl, int position) {
        //1.从内存中取图片

        //2.从本地文件中取图片

        //3.从网络获取图片
        netCacheUtils.getBitmapFomNet(imageUrl,position);
        return null;
    }
}
