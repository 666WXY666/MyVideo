package bytedance.android.com.myvideo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Copyright: Copyright (c) 2020 王兴宇 刘凯鑫 All Rights Reserved.
 * @Project: My Video
 * @Package: bytedance.android.com.myvideo
 * @Description:
 * @Version:
 * @Author: 王兴宇 刘凯鑫
 * @Date: 2020-05-25 18:30
 * @LastEditors: 王兴宇 刘凯鑫
 * @LastEditTime: 2020-05-25 18:30
 */

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private RefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    private List<VideoInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏、隐藏状态栏
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager2);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setEnableLoadMore(true);

        myAdapter = new MyAdapter(MainActivity.this);
        viewPager2.setAdapter(myAdapter);

        list = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                getData();
                refreshLayout.finishRefresh();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
                refreshLayout.finishLoadMore();
            }
        });

        getData();
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideoInfo().enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                if (response.body() != null) {
                    List<VideoInfo> videoInfoList = response.body();
                    Log.d("wxy", videoInfoList.toString());
                    if (videoInfoList.size() != 0) {
                        list.addAll(videoInfoList);
                        myAdapter.setData(list);
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });

    }

}
