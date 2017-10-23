package example.com.beijingnews.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import example.com.beijingnews.R;
import example.com.beijingnews.base.BaseFragment;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/20.
 */

public class ContentFragment extends BaseFragment {

    private ViewPager viewPager;
    private RadioGroup rg_main;

    @Override
    public View initview() {
        LogUtil.e("正文Fragment视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        rg_main = (RadioGroup)view.findViewById(R.id.rg_main);
        return view;

    }

    @Override
    public void iniData() {
        super.iniData();
        LogUtil.e("正文数据被初始化了");
        rg_main.check(R.id.rb_home);

    }
}
