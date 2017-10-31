package example.com.beijingnews.Listener;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import example.com.beijingnews.R;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.view.NoScrollViewPager;



/**
 * Created by Administrator on 2017/10/24.
 */

public class ContentOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
    private final NoScrollViewPager viewPager;
    private Activity context;

    public ContentOnCheckedChangeListener(NoScrollViewPager viewPager, Activity context) {

        this.context = context;

        if (viewPager != null){
            this.viewPager = viewPager;
        }else{
            this.viewPager = new NoScrollViewPager(context);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.rb_home://主页面
                viewPager.setCurrentItem(0,false);//false代表滑动动画。
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                break;
            case R.id.rb_newscenter://新闻
                viewPager.setCurrentItem(1,false);
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_smater://智慧
                viewPager.setCurrentItem(2,false);
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                break;
            case R.id.rb_govaffair://政要
                viewPager.setCurrentItem(3,false);
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                break;
            case R.id.rb_setting://设置
                viewPager.setCurrentItem(4,false);
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                break;
        }
    }

    private  void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
}
