package example.com.beijingnews.is;

import android.app.Activity;

import example.com.beijingnews.activity.MainActivity;

/**
 * Created by Administrator on 2017/10/24.
 */

public  class isEnableSlidingMenu {
    public static void isEnableSlidingMenu(int touchmodeFullscreen,Activity context){
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
}
