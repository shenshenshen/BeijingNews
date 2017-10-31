package example.com.beijingnews.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/10/25.
 */

public abstract class MenuDetaiBasePager {

    public final Context context;

    public View rootView;

    public MenuDetaiBasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    //抽象方法，强制孩子实现该方法，每个页面实现不同的效果。
    public abstract View initView();

    public void initData(){

    }

}
