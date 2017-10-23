package example.com.beijingnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import example.com.beijingnews.activity.GuideActivity;
import example.com.beijingnews.activity.MainActivity;
import example.com.beijingnews.utiles.CacheUtils;

public class SplashActivity extends Activity {

    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splash_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);

        //渐变动画，缩放动画，旋转动画。
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setFillAfter(true);

        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setFillAfter(true);

        //旋转动画
        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false );

        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.setDuration(2000);

        rl_splash_root.startAnimation(set);

        set.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //判断是否进入过主页面
            boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
            Intent intent;
            //如果进入过主页面，直接进入主页面
            if (isStartMain){
                intent = new Intent(SplashActivity.this,MainActivity.class);

            }else {
                 intent = new Intent(SplashActivity.this,GuideActivity.class);

            }
            //如果没有 进入过主页面，进入引导页面
            startActivity(intent);
           finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
