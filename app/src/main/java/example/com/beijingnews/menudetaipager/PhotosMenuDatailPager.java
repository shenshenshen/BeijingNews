package example.com.beijingnews.menudetaipager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import example.com.beijingnews.R;
import example.com.beijingnews.base.MenuDetaiBasePager;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/25.
 */

public class PhotosMenuDatailPager extends MenuDetaiBasePager {

    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    public PhotosMenuDatailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
       View view = View.inflate(context, R.layout.photos_menudetail_pager,null);
        x.view().inject(this,view);
       return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面内容被初始化了");


    }
}
