package example.com.beijingnews.utiles;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BitmapCacheUtils {

    //网络缓存工具
    private NetCacheUtils netCacheUtils;

    public BitmapCacheUtils(NetCacheUtils netCacheUtils) {
        netCacheUtils = new NetCacheUtils();
    }

    public Bitmap getBitmap(String imageUrl) {
        //1.从内存中取图片

        //2.从本地文件中取图片

        //3.从网络获取图片
        netCacheUtils.getBitmapFomNet(imageUrl);
        return null;
    }
}
