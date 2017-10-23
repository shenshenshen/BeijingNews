package example.com.beijingnews.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import example.com.beijingnews.R;
import example.com.beijingnews.base.BaseFragment;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/20.
 */

public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

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
        rg_main.check(R.id.rb_home);

    }
}
