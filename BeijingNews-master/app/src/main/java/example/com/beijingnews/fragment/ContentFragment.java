package example.com.beijingnews.fragment;

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

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import example.com.beijingnews.R;
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
        viewPager.setAdapter(new ContentFragmentAdapter());

        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认选项
        rg_main.check(R.id.rb_home);
        basePagers.get(0).initData();

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i){
                case R.id.rb_home://主页面
                    viewPager.setCurrentItem(0,false);
                    break;
                case R.id.rb_newscenter://新闻
                    viewPager.setCurrentItem(1,false);
                    break;
                case R.id.rb_smater://智慧
                    viewPager.setCurrentItem(2,false);
                    break;
                case R.id.rb_govaffair://政要
                    viewPager.setCurrentItem(3,false);
                    break;
                case R.id.rb_setting://设置
                    viewPager.setCurrentItem(4,false);
                    break;
                default:
                    break;
            }
        }
    }



    class ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager  = basePagers.get(position);//各个页面的实例
            View rootView = basePager.rootView;//各个子页面
            //调用各个页面的niniData()

            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
