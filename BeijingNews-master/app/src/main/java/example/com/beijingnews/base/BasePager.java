package example.com.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import example.com.beijingnews.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class BasePager{
    public final Context context;//MainActivity

    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;
    public FrameLayout fl_content;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    //初始化视图
    private View initView() {
        View view = View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }

    //初始化数据
    private void initData(){

    }
}
