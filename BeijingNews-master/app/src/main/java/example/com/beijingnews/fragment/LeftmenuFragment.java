package example.com.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.com.beijingnews.base.BaseFragment;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/20.
 */

public class LeftmenuFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View initview() {
        LogUtil.e("左侧菜单视图被初始化了");
        textView = new TextView(context);
        textView.setTextSize(23);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void iniData() {
        super.iniData();
        LogUtil.e("左侧数据被初始化了");
        textView.setText("左侧菜单页面");
    }
}
