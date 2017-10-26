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

public class SettingPager extends BasePager {
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("设置中心被初始化了");
        //1.设置标题
        tv_title.setText("设置");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //3.把视图添加到BasePager的FrameLayout
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("设置内容");
    }
}
