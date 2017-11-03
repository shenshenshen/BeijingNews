package example.com.beijingnews.menudetaipager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import example.com.beijingnews.R;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.menudetaipager.tabdetailpager.TabDetailPager;
import example.com.beijingnews.utiles.LogUtil;



/**
 * Created by Administrator on 2017/10/25.
 */

public class NewsMenuDatailPager extends MenuDetaiBasePager {

    //传递过来的数据
    private final List<NewsCenterPagerBean.DataBean.ChildrenBean> Children;

    //子页面数据集合
    private ArrayList<TabDetailPager> tabDetailPagers;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;

    @ViewInject(R.id.next_table)
    private ImageButton next_table;

    public NewsMenuDatailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.Children = dataBean.getChildren();
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.newsmenu_detail_pager,null);
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
        LogUtil.e("新闻详情页面内容被初始化了");
        tabDetailPagers = new ArrayList<>();
        //准备新闻详情页面的数据
        for (int i=0; i<Children.size();i++){
            //初始化子界面并且传递数据
            tabDetailPagers.add(new TabDetailPager(context,Children.get(i)));
        }

        //设置ViewPager的适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());

        //ViewPager和TabPageIndicator关联
        tabPageIndicator.setViewPager(viewPager);

        //注意以后监听页面的变化，用TabPageIndicator来监听页面的变化
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }

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
    //是否可以侧滑
    private  void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity)context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
    //PagerAdapter
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return Children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
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
