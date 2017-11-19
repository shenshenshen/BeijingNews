package example.com.beijingnews.utiles;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * Created by Administrator on 2017/11/9.
 */

public class BitmapCacheUtils {

    //网络缓存工具
    private NetCacheUtils netCacheUtils;

    private LocalCacheUtils localCacheUtils;

    //构造方法初始化
    public BitmapCacheUtils(Handler handler) {
        localCacheUtils = new LocalCacheUtils();
        netCacheUtils = new NetCacheUtils(handler,localCacheUtils);

    }

    public Bitmap getBitmap(String imageUrl, int position) {
        //1.从内存中取图片

        //2.从本地文件中取图片
        if (localCacheUtils != null){
            Bitmap bitmap = localCacheUtils.getBitmapFroml(imageUrl);
            if (bitmap != null){
                LogUtil.e("本地加载图片成功=="+position);
                return bitmap;
            }
        }

        //3.从网络获取图片
        netCacheUtils.getBitmapFomNet(imageUrl,position);
        return null;
    }
}
