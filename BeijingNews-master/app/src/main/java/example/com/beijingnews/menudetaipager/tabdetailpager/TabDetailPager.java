package example.com.beijingnews.menudetaipager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;

/**
 * Created by Administrator on 2017/10/26.
 */

public class TabDetailPager extends MenuDetaiBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    private TextView textView;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
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
        textView.setText(childrenBean.getTitle());

    }
}
