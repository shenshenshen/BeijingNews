package example.com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.solver.Goal;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.base.BasePager;
import example.com.beijingnews.domain.NewsCenterPagerBean;
import example.com.beijingnews.fragment.LeftmenuFragment;
import example.com.beijingnews.utiles.Constants;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/10/23.
 */

public class NewsCenterPager extends BasePager {
    //左侧菜单对应的数据集合
    List<NewsCenterPagerBean.DataBean> beanDatalist;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心被初始化了");
        ib_menu.setVisibility(View.VISIBLE);
        //1.设置标题
        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //3.把视图添加到BasePager的FrameLayout
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");

        getDataFromNet();
    }

    //使用xUtils3联网请求数据
    public void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功=="+result);

                processData(result);
                //设置适配器
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    //解析json数据和显示数据
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用gson解析json数据成功-title=="+title);

        //给左侧菜单传递数据
        beanDatalist = bean.getData();

        MainActivity mainActivity = (MainActivity)context;
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftMenuFragment();
        leftmenuFragment.setData(beanDatalist);

    }

    //解析json数据
    private NewsCenterPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,NewsCenterPagerBean.class);
    }
}
