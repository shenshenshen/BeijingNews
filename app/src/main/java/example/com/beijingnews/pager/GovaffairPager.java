package example.com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import example.com.beijingnews.base.BasePager;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/23.
 */

public class GovaffairPager extends BasePager {
    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要中心被初始化了");
        //1.设置标题
        tv_title.setText("政要指南");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //3.把视图添加到BasePager的FrameLayout
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("政要指南内容");
    }
}
