package bytedance.android.com.myvideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * @Copyright: Copyright (c) 2020 王兴宇 All Rights Reserved.
 * @Project: My Video
 * @Package: bytedance.android.com.myvideo
 * @Description:
 * @Version:
 * @Author: 王兴宇
 * @Date: 2020-05-20 15:36
 * @LastEditors: 王兴宇
 * @LastEditTime: 2020-05-20 15:36
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<VideoInfo> list;
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.imageView.setVisibility(View.VISIBLE);
        holder.loading.setVisibility(View.GONE);
        holder.videoView.setVisibility(View.GONE);
        holder.like.setProgress(100);

        holder.descriptionTextView.setText(list.get(position).description);
        holder.nicknameTextView.setText("@" + list.get(position).nickname);
        int likeCount = Integer.parseInt(list.get(position).likeCount);
        if (likeCount >= 1000 && likeCount < 10000) {
            holder.likeCountTextView.setText((double) likeCount / 1000 + "k");
        } else if (likeCount >= 10000) {
            holder.likeCountTextView.setText((double) likeCount / 10000 + "w");
        } else {
            holder.likeCountTextView.setText(String.valueOf(likeCount));
        }

        Glide.with(context)
                .load(list.get(position).avatar)
                .error(R.mipmap.load_error)
                .transition(withCrossFade())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.videoView.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.loading.setVisibility(View.VISIBLE);
                holder.loading.playAnimation();
                holder.videoView.setVideoPath(list.get(position).url);
            }
        });

        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.loading.cancelAnimation();
                holder.loading.setVisibility(View.GONE);
                holder.videoView.requestFocus();
                holder.videoView.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<VideoInfo> list) {
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private VideoView videoView;
        private ImageView imageView;
        private TextView descriptionTextView;
        private TextView nicknameTextView;
        private TextView likeCountTextView;
        private LottieAnimationView loading;
        private LottieAnimationView like;
        private GestureDetector myGestureDetector;
        private Love love;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            imageView = itemView.findViewById(R.id.imageView);
            descriptionTextView = itemView.findViewById(R.id.description);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            likeCountTextView = itemView.findViewById(R.id.likeCount);
            loading = itemView.findViewById(R.id.loading);
            love = itemView.findViewById(R.id.loveLayout);
            like = itemView.findViewById(R.id.like);

            //实例化GestureDetector
            myGestureDetector = new GestureDetector(context, new MyOnGestureListener());
            //增加监听事件
            videoView.setOnTouchListener(new View.OnTouchListener() {

                @Override//可以捕获触摸屏幕发生的Event事件
                public boolean onTouch(View v, MotionEvent event) {
                    //使用GestureDetector转发MotionEvent对象给OnGestureListener
                    myGestureDetector.onTouchEvent(event);
                    return true;
                }
            });

        }

        class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                love.addLoveView(e.getRawX(), e.getRawY());
                like.playAnimation();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
                videoView.performClick();
                return super.onSingleTapConfirmed(e);
            }
        }

    }

}
