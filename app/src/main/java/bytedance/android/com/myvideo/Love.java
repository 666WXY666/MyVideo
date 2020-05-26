package bytedance.android.com.myvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * @Copyright: Copyright (c) 2020 王兴宇 刘凯鑫 All Rights Reserved.
 * @Project: My Video
 * @Package: bytedance.android.com.myvideo
 * @Description:
 * @Version:
 * @Author: 王兴宇 刘凯鑫
 * @Date: 2020-05-20 15:36
 * @LastEditors: 王兴宇 刘凯鑫
 * @LastEditTime: 2020-05-20 15:36
 */

public class Love extends RelativeLayout {

    private Context context;
    private int width;
    private int height;
    private LayoutParams params;
    private Drawable[] icons = new Drawable[4];
    private Interpolator[] interpolator = new Interpolator[4];


    public Love(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        // 图片资源
        icons[0] = ContextCompat.getDrawable(context, R.drawable.heart_red);
        icons[1] = ContextCompat.getDrawable(context, R.drawable.heart_cyan);
        icons[2] = ContextCompat.getDrawable(context, R.drawable.heart_yellow);
        icons[3] = ContextCompat.getDrawable(context, R.drawable.heart_pink);
        // 插值器
        // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        interpolator[0] = new AccelerateDecelerateInterpolator();
        // 在动画开始的地方速率改变比较慢，然后开始加速
        interpolator[1] = new AccelerateInterpolator();
        // 在动画开始的地方快然后慢
        interpolator[2] = new DecelerateInterpolator();
        // 以常量速率改变
        interpolator[3] = new LinearInterpolator();
    }

    public void addLoveView(float x, float y) {
        if (x < 100) {
            x = 101;
        }
        if (y < 100) {
            y = 101;
        }
        // 获取心形图片中心位置
        width = (int) (x - 100);
        height = (int) (y - 100);
        // 新建心形图片
        final ImageView imageView = new ImageView(context);
        params = new LayoutParams(200, 200);
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(icons[new Random().nextInt(4)]);
        addView(imageView);
        // 开启动画，动画结束移除图片
        AnimatorSet set = getAnimatorSet(imageView);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(imageView);
            }
        });
    }

    private AnimatorSet getAnimatorSet(ImageView imageView) {

        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1f);

        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 0.2f, 1f);

        // 动画集合
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setDuration(2000);

        // 贝塞尔曲线动画
        ValueAnimator bezier = getBezierAnimator(imageView);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(set, bezier);
        set2.setTarget(imageView);
        return set2;
    }

    private ValueAnimator getBezierAnimator(final ImageView imageView) {
        // 贝塞尔动画
        // 4个点的坐标
        PointF[] PointFs = getPointFs(imageView);
        Evaluator evaluator = new Evaluator(PointFs[1], PointFs[2]);
        ValueAnimator valueAnim = ValueAnimator.ofObject(evaluator, PointFs[0], PointFs[3]);
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF p = (PointF) animation.getAnimatedValue();
                imageView.setX(p.x);
                imageView.setY(p.y);
                imageView.setAlpha(1 - animation.getAnimatedFraction()); // 透明度

            }
        });
        valueAnim.setTarget(imageView);
        valueAnim.setDuration(2000);
        valueAnim.setInterpolator(interpolator[new Random().nextInt(4)]);
        return valueAnim;
    }

    private PointF[] getPointFs(ImageView iv) {
        PointF[] pointFs = new PointF[4];
        pointFs[0] = new PointF();
        pointFs[0].x = width;
        pointFs[0].y = height;

        pointFs[1] = new PointF();
        pointFs[1].x = new Random().nextInt(width);
        pointFs[1].y = new Random().nextInt(height / 2) + height / 2 + params.height;

        pointFs[2] = new PointF();
        pointFs[2].x = new Random().nextInt(width);
        pointFs[2].y = new Random().nextInt(height / 2);

        pointFs[3] = new PointF();
        pointFs[3].x = new Random().nextInt(width);
        pointFs[3].y = 0;
        return pointFs;
    }

    public class Evaluator implements TypeEvaluator<PointF> {
        //估值器
        private PointF p1;
        private PointF p2;

        public Evaluator(PointF p1, PointF p2) {
            super();
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public PointF evaluate(float fraction, PointF p0, PointF p3) {
            PointF pointf = new PointF();

            // 贝塞尔曲线公式: p0*(1-t)^3 + 3p1*t*(1-t)^2 + 3p2*t^2*(1-t) + p3^3
            pointf.x = p0.x * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + 3 * p1.x * fraction * (1 - fraction) * (1 - fraction)
                    + 3 * p2.x * fraction * fraction * (1 - fraction)
                    + p3.x * fraction * fraction * fraction;
            pointf.y = p0.y * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + 3 * p1.y * fraction * (1 - fraction) * (1 - fraction)
                    + 3 * p2.y * fraction * fraction * (1 - fraction)
                    + p3.y * fraction * fraction * fraction;
            return pointf;
        }
    }

}