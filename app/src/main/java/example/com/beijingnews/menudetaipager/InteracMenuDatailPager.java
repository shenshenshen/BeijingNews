package example.com.beijingnews.menudetaipager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

import example.com.beijingnews.R;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.domain.PhotosMenuDetailPageBean;
import example.com.beijingnews.utiles.BitmapCacheUtils;
import example.com.beijingnews.utiles.CacheUtils;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;
import example.com.beijingnews.utiles.NetCacheUtils;
import example.com.beijingnews.volley.VolleyManager;

/**
 * Created by Administrator on 2017/10/25.
 */

public class InteracMenuDatailPager extends MenuDetaiBasePager {

    private final NewsCenterPagerBean.DataBean dataBean;
    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private InteracMenuDetailPageBeanAdapter adapter;

    private String url = "";
    //图组数据集合
    private List<PhotosMenuDetailPageBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPageBean.DataBean.NewsBean newdata;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SUCESS://图片请求成功
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (listview.isShown()){
                        ImageView iv_icon = (ImageView) listview.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    if (gridview.isShown()){
                        ImageView iv_icon = (ImageView) gridview.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    LogUtil.e("联网请求图片成功=="+position);
                    break;
                case NetCacheUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    LogUtil.e("联网请求图片失败="+position);
                    break;
            }
        }
    };


    //图片三级缓存工具
    private BitmapCacheUtils bitmapCacheUtils;

    public InteracMenuDatailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.interac_menudetail_pager,null);
        x.view().inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面内容被初始化了");
        url = Constants.BASE_URL + dataBean.getUrl();
        LogUtil.e("互动页面的url==="+url);

        String savaJson = CacheUtils.getString(context,url);
        if (!TextUtils.isEmpty(savaJson)){
            processData(savaJson);
        }
        //联网请求
        getDataFromNet();

        //设置适配器
    }

    //联网请求
    private void getDataFromNet() {
        //请求队列
        //RequestQueue queue = Volley.newRequestQueue(context);
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                LogUtil.e("使用Volley联网请求成功=="+result);

                CacheUtils.putString(context,url,result);
                //缓存数据

                processData(result);
                //设置适配器

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("使用Volley联网请求失败=="+ error.getMessage());
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        VolleyManager.getRequestQueue().add(request);
    }

    //解析显示数据
    private void processData(String savaJson) {
        PhotosMenuDetailPageBean bean = paraJson(savaJson);
        LogUtil.e("图组Title=="+bean.getData().getNews().get(0).getTitle());
        news =  bean.getData().getNews();

        adapter = new InteracMenuDetailPageBeanAdapter();
        listview.setAdapter(adapter);
    }

    private boolean isShowListView = true;
    //true 显示ListView
    //false 显示GridView
    //切换图片显示形式
    public void swichListAndGrid(ImageButton ib_swich_list_grid) {
        if (isShowListView){

            isShowListView = false;

            //显示GraidView,隐藏ListView
            //显示按钮 -- 隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            adapter = new InteracMenuDetailPageBeanAdapter();
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            //按钮显示 -- ListView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);

        }else{

            isShowListView = true;

            //显示隐藏ListView,隐藏显示GraidView
            listview.setVisibility(View.VISIBLE);
            adapter = new InteracMenuDetailPageBeanAdapter();
            listview.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示 -- GrideView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    //listAdapter
    class InteracMenuDetailPageBeanAdapter extends BaseAdapter {
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
            PhotosMenuDatailPager.ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_phots_menudetail_pager,null);
                viewHolder = new PhotosMenuDatailPager.ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (PhotosMenuDatailPager.ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            newdata = news.get(position);
            String imageUrl = Constants.BASE_URL+newdata.getSmallimage();
            //使用Glide加载图片
            //Glide.with(context).load(imageUrl).into(viewHolder.iv_icon);

            viewHolder.iv_icon.setTag(position);



            //使用自定义的三级缓存请求图片
            Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl,position);//内存或者本地，不可能是网络的
            if (bitmap != null){
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }

            viewHolder.tv_title.setText(newdata.getTitle());

            return convertView;
        }
    }

    //ViewHolder
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }

    //解析JSON
    private PhotosMenuDetailPageBean paraJson(String savaJson) {
        return new Gson().fromJson(savaJson,PhotosMenuDetailPageBean.class);
    }
}
