package example.com.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.beijingnews.base.BasePager;

/**
 * Created by Administrator on 2017/10/24.
 */

public class ContentFragmentAdapter extends PagerAdapter {
    private final ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers) {
        if (basePagers != null){
            this.basePagers = basePagers;
        }else{
            this.basePagers = new ArrayList<>();
        }

    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager  = basePagers.get(position);//各个页面的实例
        View rootView = basePager.rootView;//各个子页面
        //调用各个页面的niniData()

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

