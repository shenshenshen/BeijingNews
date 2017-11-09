package example.com.beijingnews.menudetaipager.tabdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import example.com.beijingnews.R;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.domain.TabDetailPagerBean;
import example.com.beijingnews.utiles.CacheUtils;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;
import example.com.beijingnews.view.HorizontalScrollViewPager;
import example.com.refreshl.RefreshListview;

/**
 * Created by Administrator on 2017/10/26.
 */

public class TopicDetailPager extends MenuDetaiBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;

    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private ListView listview;
    private String url;
    //顶部新闻数据集合
    private List<TabDetailPagerBean.DataBean.TopnewsData> topnews;
    //新闻数据
    private List<TabDetailPagerBean.DataBean.NewsData> news;
    private TabDetailPagerListAdapter adpter;
    private ImageOptions imageOption;
    //之前点的位置
    private int prePosition = 0 ;
    private boolean isRefresh = false;
    //下一页的联网路径
    private String moreUrl;
    //是否加载更多
    private boolean isLoadMore = false;
    //PullToRefreshListView
    private PullToRefreshListView mPullRefreshListView;

    public TopicDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
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
        View view = View.inflate(context,R.layout.topic_detail_pager,null);

        mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
        listview = mPullRefreshListView.getRefreshableView();

        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        mPullRefreshListView.setOnPullEventListener(soundListener);

        View topNewsView = View.inflate(context,R.layout.topnews,null);
        viewpager = (HorizontalScrollViewPager) topNewsView.findViewById(R.id.viewpager);
        tv_title = (TextView)topNewsView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout)topNewsView.findViewById(R.id.ll_point_group);
        //把顶部轮播图加入listview
        listview.addHeaderView(topNewsView);

        //listview.addTopNewsView(topNewsView);

        //设置监听下拉刷新
       // listview.setOnRefreshListener(new MyOnRefreshListener());
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                getDataFromNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (TextUtils.isEmpty(moreUrl)){
                    Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                  //  listview.onRefreshFinish(false);
                    mPullRefreshListView.onRefreshComplete();
                }else{
                    getMoreDataFromNet();
                }
            }
        });
        return view;
    }

    /*class MyOnRefreshListener implements RefreshListview.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            isRefresh = true;
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(moreUrl)){
                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                listview.onRefreshFinish(false);
            }else{
                getMoreDataFromNet();
            }

        }
    }*/
    //加载更多的网络请求
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功=="+result);
              //  listview.onRefreshFinish(false);
                mPullRefreshListView.onRefreshComplete();
                //把这个放在前面
                isLoadMore = true;
                //解析数据
                ProcessData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败=="+ex.getMessage());
              //  listview.onRefreshFinish(false);
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网onFinished");
                Toast.makeText(context,"加载完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        String saveJson = CacheUtils.getString(context,url);
        prePosition = 0;
        if (!TextUtils.isEmpty(saveJson)){
            //解析数据和显示数据
            ProcessData(saveJson);
        }
        //LogUtil.e(childrenBean.getTitle()+"联网地址=="+url);
        //联网请求
        getDataFromNet();

    }

    //显示数据
    private void ProcessData(String Json) {
        TabDetailPagerBean bean = parsedJson(Json);
        moreUrl = "";
        if (TextUtils.isEmpty(bean.getData().getMore())){
            moreUrl = "";
        }else{
            moreUrl = Constants.BASE_URL+bean.getData().getMore();
        }
        //默认和加载更多
        if (!isLoadMore){//默认
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
        }else{
            //加载更多
            isLoadMore = false;
            //添加到原来的集合中
            news.addAll(bean.getData().getNews());
            //刷新适配器
            adpter.notifyDataSetChanged();
        }
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

    //添加灰点和红点
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
        prePosition = 0;
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求成功");
                //缓存数据
                CacheUtils.putString(context,url,result);
                //解析和处理显示数据
                ProcessData(result);
                if (isRefresh){
                    Toast.makeText(context,"刷新成功",Toast.LENGTH_SHORT).show();
                }
                //隐藏下拉刷新控件 - 更新时间
               // listview.onRefreshFinish(true);
                mPullRefreshListView.onRefreshComplete();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求失败=="+ex.getMessage());
                //隐藏下拉刷新控件 - 不更新时间，只是隐藏
                if (isRefresh){
                    Toast.makeText(context,"服务器没有开启或者没有连接网络",Toast.LENGTH_SHORT).show();
                }
                //listview.onRefreshFinish(false);
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                isRefresh = false;
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onFinished");

            }
        });
    }


}
