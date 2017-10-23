package example.com.beijingnews.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/10/20.
 */

public abstract class BaseFragment extends Fragment {

    public Activity context;
    //Fragment被创建的时候回调这个方法
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initview();
    }

    //让孩子实现自己的视图,达到自己的特有的效果
    public abstract View initview();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniData();
    }

    //如果子页面没有数据，联网请求数据，并且绑定到initview初始化的视图上
    //有数据直接绑定到initview初始化的视图上
    public void iniData(){

    }
}
