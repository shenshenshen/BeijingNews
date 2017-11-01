package example.com.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/11/1.
 */

public class HorizontalScrollViewPager extends ViewPager {
    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //起始坐标
    private float startx;
    private float starty;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录起始坐标
                startx = ev.getX();
                starty = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //新的坐标
                float endx = ev.getX();
                float endy = ev.getY();
                //计算偏移量
                float distanceX = endx - startx;
                float distanceY = endy - starty;
                //判断滑动方向
                if (Math.abs(distanceX) > Math.abs(distanceY)){
                    //水平方向滑动
                    //当滑动到viewPager的第0个页面，并且是从左到右滑动
                    if (getCurrentItem() == 0 && distanceX > 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //当滑动到viewPager的最后一个页面，并且是从右到左滑动
                    else if((getCurrentItem() == getAdapter().getCount()-1) && distanceX < 0 ){
                           getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //其他
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
