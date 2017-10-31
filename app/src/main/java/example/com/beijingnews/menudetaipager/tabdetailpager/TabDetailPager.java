package example.com.beijingnews.menudetaipager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import example.com.beijingnews.R;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/26.
 */

public class TabDetailPager extends MenuDetaiBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;

    private String url;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.tabdetail_pager,null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        LogUtil.e(childrenBean.getTitle()+"联网地址=="+url);
    }
}
