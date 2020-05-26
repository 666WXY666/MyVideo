package bytedance.android.com.myvideo;

import com.google.gson.annotations.SerializedName;

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

public class VideoInfo {
    @SerializedName("_id")
    public String id;
    @SerializedName("feedurl")
    public String url;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("description")
    public String description;
    @SerializedName("likecount")
    public String likeCount;
    @SerializedName("avatar")
    public String avatar;

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                ", likeCount='" + likeCount + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
