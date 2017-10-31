package example.com.beijingnews.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import example.com.beijingnews.Listener.ContentOnCheckedChangeListener;
import example.com.beijingnews.Listener.ContentOnPageChangeListener;
import example.com.beijingnews.R;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.adapter.ContentFragmentAdapter;
import example.com.beijingnews.base.BaseFragment;
import example.com.beijingnews.base.BasePager;
import example.com.beijingnews.pager.GovaffairPager;
import example.com.beijingnews.pager.HomePager;
import example.com.beijingnews.pager.NewsCenterPager;
import example.com.beijingnews.pager.SettingPager;
import example.com.beijingnews.pager.SmartServicePager;
import example.com.beijingnews.utiles.LogUtil;
import example.com.beijingnews.view.NoScrollViewPager;



/**
 * Created by Administrator on 2017/10/20.
 */

public class ContentFragment extends BaseFragment  {

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewPager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    private ArrayList<BasePager> basePagers;



    @Override
    public View initview() {
        LogUtil.e("正文Fragment视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);
        //把视图注入到框架中，让ContentFragment和View关联起来。
        x.view().inject(this,view);
        return view;

    }

    @Override
    public void iniData() {
        super.iniData();
        LogUtil.e("正文数据被初始化了");

        //初始化五个页面，并且放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new SmartServicePager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));

        //设置ViewPager的适配器
        viewPager.setAdapter(new ContentFragmentAdapter(basePagers));

        //监听RadioGroup
        rg_main.setOnCheckedChangeListener(new ContentOnCheckedChangeListener(viewPager,context));

        //切换到相应的Pager就初始化数据
        viewPager.addOnPageChangeListener(new ContentOnPageChangeListener(basePagers));

        //默认选项
        rg_main.check(R.id.rb_home);

        basePagers.get(0).initData();

        //默认不可滑动

        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

    }


    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    private  void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
}
