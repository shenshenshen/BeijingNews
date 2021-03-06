package example.com.beijingnews.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.base.BasePager;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.domain.NewsCenterPagerBean2;
import example.com.beijingnews.fragment.LeftmenuFragment;
import example.com.beijingnews.menudetaipager.InteracMenuDatailPager;
import example.com.beijingnews.menudetaipager.NewsMenuDatailPager;
import example.com.beijingnews.menudetaipager.PhotosMenuDatailPager;
import example.com.beijingnews.menudetaipager.TopicMenuDatailPager;
import example.com.beijingnews.menudetaipager.ToupiaoMenuDatailPager;
import example.com.beijingnews.utiles.CacheUtils;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;
import example.com.beijingnews.volley.VolleyManager;

/**
 * Created by Administrator on 2017/10/23.
 */

public class NewsCenterPager extends BasePager {
    //左侧菜单对应的数据集合
    List<NewsCenterPagerBean.DataBean> beanDatalist;

    //子页面的数据集合
    private ArrayList<MenuDetaiBasePager> menuDetaiBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心被初始化了");
        ib_menu.setVisibility(View.VISIBLE);
        //1.设置标题
        tv_title.setText("新闻中心");
        //缓存数据
        String saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);//默认空字符串。
        if (!TextUtils.isEmpty(saveJson)){//不能写 savaJson != null
           processData(saveJson);
        }
            //联网请求数据
         getDataFromNet();
        //getDataFromNetByVolley();
    }

    private void getDataFromNetByVolley() {
        //请求队列
        //RequestQueue queue = Volley.newRequestQueue(context);
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, Constants.NEWSCENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                LogUtil.e("使用Volley联网请求成功=="+result);

                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);
                //缓存数据

                processData(result);
                //设置适配器
            }
        }, new ErrorListener() {
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


    //使用xUtils3联网请求数据
    public void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功=="+result);

                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);
                //缓存数据

                processData(result);
                //设置适配器
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    //解析json数据和显示数据
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);

        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        //给左侧菜单传递数据
        beanDatalist = bean.getData();
        MainActivity mainActivity = (MainActivity)context;
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftMenuFragment();

        menuDetaiBasePagers = new ArrayList<>();
        menuDetaiBasePagers.add(new NewsMenuDatailPager(context,beanDatalist.get(0)));//新闻详情页面
        menuDetaiBasePagers.add(new TopicMenuDatailPager(context,beanDatalist.get(0)));//专题详情页面
        menuDetaiBasePagers.add(new PhotosMenuDatailPager(context,beanDatalist.get(2)));//图组详情页面
        menuDetaiBasePagers.add(new InteracMenuDatailPager(context,beanDatalist.get(2)));//互动详情页面
        menuDetaiBasePagers.add(new ToupiaoMenuDatailPager(context));//投票详情页面

        //左侧菜单显示数据
        leftmenuFragment.setData(beanDatalist);

    }

    //手动解析json数据
    private NewsCenterPagerBean2 parsedJson2(String json) {
        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject object = new JSONObject(json);

            int retcode = object.optInt("retcode");
            bean2.setRetcode(retcode);//retcode字段解析成功

            JSONArray data = object.optJSONArray("data");

            if (data != null && data.length() >0){

                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);
                //for循环，解析每条数据
                for (int i=0 ; i<data.length(); i++){

                    JSONObject jsonObject = (JSONObject) data.get(i);

                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
                    //添加到集合中
                    detailPagerDatas.add(detailPagerData);

                    int id =jsonObject.optInt("id");
                    detailPagerData.setId(id);
                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);
                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);
                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObject.optJSONArray("children");

                    if (children != null && children.length() >0){

                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();

                        detailPagerData.setChildren(childrenDatas);

                        for (int j=0; j<children.length(); j++){
                            JSONObject childrenItem = (JSONObject)children.get(j);

                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();

                            childrenDatas.add(childrenData);

                            int childId = childrenItem.optInt("id");
                            childrenData.setId(childId);
                            int chiildtype = childrenItem.getInt("type");
                            childrenData.setType(chiildtype);
                            String childtitle = childrenItem.optString("title");
                            childrenData.setTitle(childtitle);
                            String childUrl = childrenItem.optString("url");
                            childrenData.setUrl(childUrl);

                        }
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean2;
    }

    //解析json数据
    private NewsCenterPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,NewsCenterPagerBean.class);
    }

    //响应LeftFragment的页面切换
    public void swichPager(int position) {
        //1.设置标题
        tv_title.setText(beanDatalist.get(position).getTitle());
        //2.移除内容
        fl_content.removeAllViews();
        //3.添加新内容
        MenuDetaiBasePager menudetaiBasePager = menuDetaiBasePagers.get(position);
        View rootView = menudetaiBasePager.rootView;
        menudetaiBasePager.initData();//初始化
        fl_content.addView(rootView);

        if (position == 2){
            ib_swich_list_grid.setVisibility(View.VISIBLE);
            ib_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.得到图组对象详情页面对象
                    PhotosMenuDatailPager detailPager = (PhotosMenuDatailPager)menuDetaiBasePagers.get(2);
                    //2.调用图组对象的切换ListView和GridView对象
                    detailPager.swichListAndGrid(ib_swich_list_grid);
                }
            });
        }else if (position == 3){
            ib_swich_list_grid.setVisibility(View.VISIBLE);
            ib_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.得到互动对象详情页面对象
                    InteracMenuDatailPager detailPager = (InteracMenuDatailPager) menuDetaiBasePagers.get(3);
                    //2.调用互动对象的切换ListView和GridView对象
                    detailPager.swichListAndGrid(ib_swich_list_grid);
                }
            });
        }
        else{
            ib_swich_list_grid.setVisibility(View.GONE);
        }

    }
}
