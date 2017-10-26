package example.com.beijingnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import example.com.beijingnews.R;
import example.com.beijingnews.SplashActivity;
import example.com.beijingnews.adapter.GuideAdapter;
import example.com.beijingnews.utiles.CacheUtils;
import example.com.beijingnews.utiles.DensityUtil;

import static android.content.ContentValues.TAG;
import static example.com.beijingnews.SplashActivity.START_MAIN;

public class GuideActivity extends Activity {
    private ViewPager viewPager;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int leftMax;
    private int widthdip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //初始化控件
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);

        //准备数据
        int[] ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        //像素转dp
        widthdip = DensityUtil.dip2px(this,10);

        for (int i = 0; i < ids.length; i++) {
            ImageView imageview = new ImageView(this);
            //设置背景
            imageview.setBackgroundResource(ids[i]);
            //添加到集合中
            imageViews.add(imageview);

            //创建点 添加到线性布局 像素
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_nomal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdip,widthdip);
            if (i != 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new GuideAdapter(imageViews));

        //视图树
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListner());

        //设置按钮的点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存曾经进入过主界面
                CacheUtils.putBoolean(GuideActivity.this,SplashActivity.START_MAIN,true);

                //跳转到主页面
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);

                //关闭引导页面
                finish();
            }
        });

    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {


        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            //两点的间距
            leftMax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();


        }
    }

    class MyOnPageChangeListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //当前滑动页面的位置position
            //页面滑动的百分比positionOffset
            //滑动的像素positionOffsetPixels
            int lefrmargin = (int) (position * leftMax + (positionOffset * leftMax));


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();

            //两点间滑动距离对应的坐标
            params.leftMargin = lefrmargin;
            iv_red_point.setLayoutParams(params);

        }

        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size()-1){
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                btn_start_main.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}


