package example.com.beijingnews.menudetaipager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/27.
 */

public class ToupiaoMenuDatailPager extends MenuDetaiBasePager {

    private TextView textView;

    public ToupiaoMenuDatailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("投票详情页面内容被初始化了");
        textView.setText("投票详情页面内容");

    }
}
