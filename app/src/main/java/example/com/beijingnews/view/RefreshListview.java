package example.com.beijingnews.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import example.com.beijingnews.R;
import example.com.beijingnews.utiles.LogUtil;

/**
 * Created by Administrator on 2017/11/1.
 */

public class RefreshListview extends ListView {
    //包含下拉刷新菜单和顶部轮播图
    private LinearLayout headerView;
    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;
    //下拉刷新控件的高度
    private int pulldownrefreshHeight;

    //下拉刷新
    public static final int PULL_DOWN_REFRESH = 0;

    //手松刷新
    public static final int RELEASE_REFRESH = 1;

    //正在刷新
    public static final int REFRESHING = 2;

    //当前的状态
    private int currentStatus = PULL_DOWN_REFRESH;

    private Animation upAnimaition;
    private Animation downAnimaition;
    public RefreshListview(Context context) {
        super(context);

    }

    public RefreshListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView(context);
        initAnimation();

    }



    private void initAnimation() {
        upAnimaition = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnimaition.setDuration(500);
        upAnimaition.setFillAfter(true);

        downAnimaition = new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnimaition.setDuration(500);
        downAnimaition.setFillAfter(true);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    private void initHeaderView(Context context) {
         headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header,null);
        //下拉刷新
        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView)headerView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        //测量
        ll_pull_down_refresh.measure(0,0);
        pulldownrefreshHeight = ll_pull_down_refresh.getMeasuredHeight();

        //默认隐藏下拉刷新控件
        //View.setPadding(0,-控件高,0,0);//完全隐藏
        ll_pull_down_refresh.setPadding(0,-pulldownrefreshHeight,0,0);

        //View.setPadding(0,0,0,0);//完全显示


        //添加头
        addHeaderView(headerView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录起始坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1){
                    startY = ev.getY();
                }
                //如果正在刷新，就不让再刷新了。
                if (currentStatus == REFRESHING){
                    break;
                }
                //记录新坐标
                float endY = ev.getY();
                //计算滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0){//下拉
                   // int paddingTop = -控件高 + distanceY;
                    int paddingTop = (int) (-pulldownrefreshHeight + distanceY);

                    if(paddingTop < 0 && currentStatus !=  PULL_DOWN_REFRESH){
                        //下拉刷新
                        currentStatus = PULL_DOWN_REFRESH ;
                        //更新状态
                        refreshViewState();
                    }else if(paddingTop > 0 && currentStatus != RELEASE_REFRESH ){
                        //手松刷新
                        currentStatus  = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                        ll_pull_down_refresh.setPadding(0,paddingTop,0,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(currentStatus == PULL_DOWN_REFRESH){
                    ll_pull_down_refresh.setPadding(0,-pulldownrefreshHeight,0,0);//完全隐藏
                }else if(currentStatus == RELEASE_REFRESH ){
                    //设置状态为正在刷新
                    currentStatus = REFRESHING;
                    refreshViewState();
                    //完全显示
                    ll_pull_down_refresh.setPadding(0,0,0,0);
                    //回调接口
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onPullDownRefresh();
                    }
            }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshViewState(){
        switch (currentStatus){
            case PULL_DOWN_REFRESH://下拉刷新
                iv_arrow.startAnimation(downAnimaition);
                tv_status.setText("下拉刷新...");
                pb_status.setVisibility(GONE);
                break;
            case RELEASE_REFRESH://手松刷新
                iv_arrow.startAnimation(upAnimaition);
                tv_status.setText("手松刷新...");
                pb_status.setVisibility(GONE);
                break;
            case REFRESHING://正在刷新
                tv_status.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }
    }


    public void onRefreshFinish(boolean sucess) {
        tv_status.setText("下拉刷新...");
        currentStatus = PULL_DOWN_REFRESH;
        iv_arrow.clearAnimation();
        pb_status.setVisibility(GONE);
        iv_arrow.setVisibility(VISIBLE);
        ll_pull_down_refresh.setPadding(0,-pulldownrefreshHeight,0,0);
        if (sucess){
            //设置最新更新时间
            tv_time.setText("上次更新时间:"+getSystemTime());
        }
    }

    private String getSystemTime() {
            SimpleDateFormat format = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
            return format.format(new Date());
    }

    public interface OnRefreshListener{

        //当下拉刷新的时候回调这个方法
        public void onPullDownRefresh();
    }

    private OnRefreshListener  mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener l){
        this.mOnRefreshListener = l;
    }
}
