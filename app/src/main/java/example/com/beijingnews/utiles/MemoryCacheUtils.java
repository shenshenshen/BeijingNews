package example.com.beijingnews.utiles;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.bumptech.glide.load.engine.cache.MemoryCache;

/**
 * Created by Administrator on 2017/11/19.
 */

class MemoryCacheUtils {
    private LruCache<String,Bitmap> lruCache;

    public MemoryCacheUtils(){
        //使用应用的8分之一
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/1024/8);
        lruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };
    }

    public Bitmap getBitmapFroml(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    public void putBibtmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
