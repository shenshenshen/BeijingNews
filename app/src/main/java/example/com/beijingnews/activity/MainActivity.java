package example.com.beijingnews.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.githang.statusbar.StatusBarCompat;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import example.com.beijingnews.R;
import example.com.beijingnews.fragment.ContentFragment;
import example.com.beijingnews.fragment.LeftmenuFragment;
import example.com.beijingnews.utiles.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFT_MENU_TAG = "left_menu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);

        //初始化SlidingMenu
        iniSlidingMenu();

        //初始化Fragment
        initFragment();

    }

    private void iniSlidingMenu() {
        //设置主页面
        setContentView(R.layout.activity_main);

        //设置状态栏颜色
        //StatusBarCompat.setStatusBarColor(this, Color.RED,false );

        //设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);

        //设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();

        //设置模式:左侧菜单+主页，左侧菜单+主页+右侧菜单，左侧菜单+主页
        slidingMenu.setMode(SlidingMenu.LEFT);

        //设置滑动的模式，滑动边缘，全屏滑动,不可以滑动
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_MARGIN);

        //设置主页占据的边缘
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));
    }

    private void initFragment() {
        //得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //替换
        ft.replace(R.id.fl_main_content,new ContentFragment(),MAIN_CONTENT_TAG);//主页
        ft.replace(R.id.fl_left_content,new LeftmenuFragment(),LEFT_MENU_TAG);//左侧菜单
        //提交
        ft.commit();

       // getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_content,new ContentFragment(),MAIN_CONTENT_TAG).replace(R.id.fl_left_content,new LeftmenuFragment(),LEFT_MENU_TAG).commit();

    }

    public LeftmenuFragment getLeftMenuFragment() {
        return (LeftmenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_MENU_TAG);
    }

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
