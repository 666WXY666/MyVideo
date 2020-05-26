package bytedance.android.com.myvideo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @Copyright: Copyright (c) 2020 刘凯鑫 All Rights Reserved.
 * @Project: My Video
 * @Package: bytedance.android.com.myvideo
 * @Description:
 * @Version:
 * @Author: 刘凯鑫
 * @Date: 2020-05-25 18:30
 * @LastEditors: 刘凯鑫
 * @LastEditTime: 2020-05-25 18:30
 */

public interface ApiService {
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoInfo>> getVideoInfo();
}
