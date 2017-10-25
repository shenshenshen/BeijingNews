package example.com.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import example.com.beijingnews.R;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.base.BaseFragment;
import example.com.beijingnews.base.BasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.utiles.DensityUtil;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/20.
 */

public class LeftmenuFragment extends BaseFragment {

    private ListView listview;

    private List<NewsCenterPagerBean.DataBean> beanDatalist;

    private leftmenuFragmentAdapter adpter;

    //点击的位置
    private int prePosition;

    @Override
    public View initview() {
        LogUtil.e("左侧菜单视图被初始化了");
        listview = new ListView(context);
        listview.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        listview.setDividerHeight(0);//设置分割线高度为0
        listview.setCacheColorHint(Color.TRANSPARENT);//按下变灰，透明。
        //设置按下listview的item不变色
        listview.setSelector(android.R.color.transparent);

        //设置item的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.记录点击的位置，变成红色
                prePosition = i;
                adpter.notifyDataSetChanged();

                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

                //3.切换到对应的详情页面:新闻详情页面，专题详情页面，图组详情页面，互动详情页面
            }
        });

        return listview;

    }

    @Override
    public void iniData() {
        super.iniData();
        LogUtil.e("左侧数据被初始化了");

    }

    public void setData(List<NewsCenterPagerBean.DataBean> beanDatalist) {
        this.beanDatalist = beanDatalist;
        for (int i=0;i<beanDatalist.size();i++){
            LogUtil.e("title=="+beanDatalist.get(i).getTitle());
        }
        //设置适配器
        adpter = new leftmenuFragmentAdapter();
        listview.setAdapter(adpter);
    }

    class leftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return beanDatalist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textview = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textview.setText(beanDatalist.get(i).getTitle());
            textview.setEnabled(i == prePosition);
            return textview;
        }
    }
}
