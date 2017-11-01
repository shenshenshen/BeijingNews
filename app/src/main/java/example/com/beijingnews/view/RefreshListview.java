package example.com.beijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import example.com.beijingnews.R;

/**
 * Created by Administrator on 2017/11/1.
 */

public class RefreshListview extends ListView {
    //包含下拉刷新菜单和顶部轮播图
    private LinearLayout headerView;

    public RefreshListview(Context context) {
        super(context);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
    }

    private void initHeaderView(Context context) {
         headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header,null);
    }
}
