package example.com.beijingnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import example.com.beijingnews.R;

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private WebView webview;
    private ProgressBar pbLoading;
    private String url;
    private WebSettings webSettings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-11-06 19:37:45 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    //初始化控件
    private void findViews() {
        tvTitle = (TextView)findViewById( R.id.tv_title );
        ibMenu = (ImageButton)findViewById( R.id.ib_menu );
        ibBack = (ImageButton)findViewById( R.id.ib_back );
        ibTextsize = (ImageButton)findViewById( R.id.ib_textsize );
        ibShare = (ImageButton)findViewById( R.id.ib_share );
        webview = (WebView)findViewById( R.id.webview );
        pbLoading = (ProgressBar)findViewById( R.id.pb_loading );

        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibBack.setOnClickListener(this);
        ibTextsize.setOnClickListener(this);
        ibShare.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();
    }

    //得到intent传递过来的url,以及对webView的一些初始化
    private void getData() {
        url = getIntent().getStringExtra("url");

        webSettings =  webview.getSettings();
        //设置加载完成
        webSettings.setJavaScriptEnabled(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //不让从当前网页跳转到系统的浏览器中
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }

    //退出 分享 和字体大小的点击事件
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.ib_back:

                break;
            case R.id.ib_textsize:
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:

                break;
        }
    }


    //显示单选对话框并且缓存选择
    private int tempSize = 2;
    private int realSize = tempSize;
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();
    }

    //改变字体大小
    private void changeTextSize(int realSize) {
            switch (realSize){
                case 0://超大字体
                    webSettings.setTextZoom(200);
                    break;
                case 1://大字体
                    webSettings.setTextZoom(150);
                    break;
                case 2://正常字体
                    webSettings.setTextZoom(100);
                    break;
                case 3://小字体
                    webSettings.setTextZoom(75);
                    break;
                case 4://超小字体
                    webSettings.setTextZoom(50);
                    break;
            }
    }
}
