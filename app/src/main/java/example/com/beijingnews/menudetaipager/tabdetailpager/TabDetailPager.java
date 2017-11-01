package example.com.beijingnews.menudetaipager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.Request;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import example.com.beijingnews.R;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.domain.TabDetailPagerBean;
import example.com.beijingnews.utiles.CacheUtils;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;
import example.com.beijingnews.view.HorizontalScrollViewPager;

/**
 * Created by Administrator on 2017/10/26.
 */

public class TabDetailPager extends MenuDetaiBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;

    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private ListView listview;
    private String url;
    //顶部新闻数据集合
    private List<TabDetailPagerBean.DataBean.TopnewsData> topnews;
    private List<TabDetailPagerBean.DataBean.NewsData> news;
    private TabDetailPagerListAdapter adpter;
    private ImageOptions imageOptions;
    private int prePosition = 0 ;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        /*imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(90),DensityUtil.dip2px(90))
                .setRadius(DensityUtil.dip2px(5))
                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();*/
    }


    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.tabdetail_pager,null);
        listview = (ListView)view.findViewById(R.id.listview);
        View topNewsView = View.inflate(context,R.layout.topnews,null);
        viewpager = (HorizontalScrollViewPager) topNewsView.findViewById(R.id.viewpager);
        tv_title = (TextView)topNewsView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout)topNewsView.findViewById(R.id.ll_point_group);
        //把顶部轮播图加入listview
        listview.addHeaderView(topNewsView);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        String saveJson = CacheUtils.getString(context,url);
        if (!TextUtils.isEmpty(saveJson)){
            //解析数据和显示数据
            ProcessData(saveJson);
        }
        //LogUtil.e(childrenBean.getTitle()+"联网地址=="+url);
        //联网请求
        getDataFromNet();

    }
    //之前点的位置


    //显示数据
    private void ProcessData(String Json) {
        TabDetailPagerBean bean = parsedJson(Json);
        //添加红点
        addPoint(bean);
        //监听页面的改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        tv_title.setText(topnews.get(prePosition).getTitle());
        
        //准备listview对应的集合数据
        news =  bean.getData().getNews();
        
        //设置listview的适配器

        adpter = new TabDetailPagerListAdapter();

        listview.setAdapter(adpter);


    }

    //listviewadpater
    class TabDetailPagerListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            TabDetailPagerBean.DataBean.NewsData newdata = news.get(position);
            //请求图片xUtils3
            //x.image().bind(viewHolder.iv_icon,Constants.BASE_URL+newdata.getListimage(),imageOptions);
            //请求图片使用Glide
            Glide.with(context).load(Constants.BASE_URL+newdata.getListimage()).into(viewHolder.iv_icon);
            //设置标题
            viewHolder.tv_title.setText(newdata.getTitle());
            //设置时间
            viewHolder.tv_time.setText(newdata.getPubdate());
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView  tv_title;
        TextView  tv_time;
    }

    private void addPoint(TabDetailPagerBean bean) {
        topnews = bean.getData().getTopnews();
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

        ll_point_group.removeAllViews();//移除所有的红点

        for (int i=0;i<topnews.size();i++){
            ImageView imageview = new ImageView(context);
            imageview.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5),DensityUtil.dip2px(5));

            if (i == 0){
                imageview.setEnabled(true);
            }else{
                imageview.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }

            imageview.setLayoutParams(params);

            ll_point_group.addView(imageview);
        }
    }

    //viewpager页面的变化
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //对应页面的点高亮-红色
            //把之前的设置灰色 把之前的变成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //viewpager
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageview  = new ImageView(context);
            imageview.setBackgroundResource(R.drawable.home_scroll_default);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            //联网请求图片
            //x.image().bind(imageview,Constants.BASE_URL+topnews.get(position).getTopimage());
            Glide.with(context).load(Constants.BASE_URL+topnews.get(position).getTopimage()).into(imageview);
            container.addView(imageview);
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //解析数据
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,TabDetailPagerBean.class);
    }
    //请求网络
    private void getDataFromNet(){
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求成功");
                //缓存数据
                CacheUtils.putString(context,url,result);
                //解析和处理显示数据
                ProcessData(result);
                prePosition = 0;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onFinished");
            }
        });
    }


}
