package example.com.beijingnews.menudetaipager;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import example.com.beijingnews.R;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.menudetaipager.tabdetailpager.TabDetailPager;
import example.com.beijingnews.menudetaipager.tabdetailpager.TopicDetailPager;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/25.
 */

public class TopicMenuDatailPager extends MenuDetaiBasePager {

    //传递过来的数据
    private final List<NewsCenterPagerBean.DataBean.ChildrenBean> Children;

    private ArrayList<TopicDetailPager> tabDetailPagers;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tablayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.next_table)
    private ImageButton next_table;

    public TopicMenuDatailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.Children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topicmenu_detail_pager,null);
        x.view().inject(this,view);
        next_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题详情页面内容被初始化了");
        tabDetailPagers = new ArrayList<>();
        //准备新闻详情页面的数据
        for (int i=0; i<Children.size();i++){
            //初始化子界面并且传递数据
            tabDetailPagers.add(new TopicDetailPager(context,Children.get(i)));
        }

        //设置ViewPager的适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());

        //ViewPager和TabLayout关联
        tabLayout.setupWithViewPager(viewPager);

        //注意以后监听页面的变化，用TabLayout来监听页面的变化
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置滑动或者固定
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //自定义Tab样式
        /*for (int i=0;i < tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }*/

    }

    //自定义Tab样式的布局加载
    /*private View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item,null);
        TextView tv = (TextView) view.findViewById(R.id.textview);
        tv.setText(Children.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageview);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }*/


    //viewPager页面变化监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0){
                //SlidingMenu可以全全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //SlidingMenu不可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private  void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    //PagerAdapter
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return Children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootview = tabDetailPager.rootView;
            tabDetailPager.initData();
            container.addView(rootview);
            return rootview;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
