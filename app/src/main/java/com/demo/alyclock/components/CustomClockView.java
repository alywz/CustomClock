package com.demo.alyclock.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Aly on 2019/12/18.
 * description:自定义时钟
 *
 * @Email:aly910202@163.com
 */
public class CustomClockView extends View {
    Paint paint;
    PathEffect pathEffect;
    private int mHcolor = 0xFF000000;//时针的颜色
    private int mMcolor = 0xFFFF0000;//分针的颜色
    private int mScolor = 0xFFFF0000;//秒针的颜色
    private int cicleColor = 0xFFFF0000;//外圆的颜色
    private int pointColor = 0xFFFF0000;//圆心的颜色
    private int centerX, centerY;
    private int mCircleWidth = 10;//钟表圆的宽度
    private int mCircleHWidth = 40;//时针圆的宽度
    private int mCircleMWidth = 20;//分针圆的宽度
    private int mHwidth = 16;//时针宽度
    private int mMwidth = 10;//分针宽度
    private int mSwidth = 5;//秒针宽度

    private int circleRadius = 300;//圆的半径
    private int circleHRadius;//时针的半径
    private int circleMRadius;//分针的半径

    private int Hour;
    private int Minute;
    private int Second;

    private float pi = (float) Math.PI;//圆周率

    public CustomClockView(Context context) {
        super(context);
    }

    public CustomClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        circleHRadius = circleRadius - mCircleHWidth / 2 - mCircleWidth / 2;
        circleMRadius = circleRadius - mCircleMWidth / 2 - mCircleWidth / 2;
        //初始化当前时间
        initCalender();

        initTimer();
    }

    /**
     * 时间
     */
    private void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //初始化当前时间
                initCalender();
                invalidate();
            }
        }, 1000, 1000);
    }


    /**
     * 初始化时间
     */
    private void initCalender() {
        Calendar calendar = Calendar.getInstance();
        Hour = calendar.get(calendar.HOUR);
        Minute = calendar.get(calendar.MINUTE);
        Second = calendar.get(calendar.SECOND);
        Log.d(getClass().getSimpleName(), "Hour=" + Hour);
        Log.d(getClass().getSimpleName(), "Minute=" + Minute);
        Log.d(getClass().getSimpleName(), "Second=" + Second);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先把画布移动到中心点
        canvas.translate(centerX, centerY);
        //画外部圆圈,和时针分针刻度
        initCircle(canvas);
        //画圆心和指针
        initPointer(canvas);
        //画数字
        initNums(canvas);

    }

    /**
     * 画圆心和指针
     *
     * @param canvas
     */
    private void initPointer(Canvas canvas) {
        //画时针指针
        canvas.save();
        canvas.rotate((float) (Hour * 30 + Minute * 0.5 - 90));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(mHcolor);
        paint.setStrokeWidth(mHwidth);
        canvas.drawLine(-25, 0, 130, 0, paint);
        canvas.restore();


        //画分针指针
        canvas.save();
        canvas.rotate(Minute * 6 - 90);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(mMcolor);
        paint.setStrokeWidth(mMwidth);
        canvas.drawLine(-30, 0, 170, 0, paint);
        canvas.restore();

        //画秒针指针
        canvas.save();
        canvas.rotate(Second * 6 - 90);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(mScolor);
        paint.setStrokeWidth(mSwidth);
        canvas.drawLine(-50, 0, 200, 0, paint);
        canvas.restore();

        //画时钟圆心
        paint.setColor(pointColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(30);
        canvas.drawPoint(0, 0, paint);
    }

    /**
     * 画外部圆圈和时针分针的刻度
     *
     * @param canvas
     */
    private void initCircle(Canvas canvas) {
        //画外圆
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mCircleWidth);
        paint.setColor(cicleColor);
        canvas.drawCircle(0, 0, circleRadius, paint);

        //画分针刻度
        pathEffect = new DashPathEffect(new float[]{4, (2 * circleMRadius * pi - 4 * 60) / 60}, 5);
        paint.setPathEffect(pathEffect);
        paint.setStrokeWidth(mCircleMWidth);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mHcolor);
        canvas.drawCircle(0, 0, circleMRadius, paint);
        paint.setPathEffect(null);

        //画时针刻度
        pathEffect = new DashPathEffect(new float[]{6, (2 * circleHRadius * pi - 6 * 12) / 12}, 5);
        paint.setPathEffect(pathEffect);
        paint.setStrokeWidth(mCircleHWidth);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mHcolor);
        canvas.drawCircle(0, 0, circleHRadius, paint);
        paint.setPathEffect(null);
    }

    /**
     * 画数字
     *
     * @param canvas
     */
    private void initNums(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setColor(mHcolor);
        paint.setTextSize(50);
        Rect rect = new Rect();
        for (int i = 0; i < 12; i++) {
            String text = "";
            if (i == 0) {
                text = "12";
            } else {
                text = i + "";
            }
            paint.getTextBounds(text, 0, text.length(), rect);
            canvas.save();
            canvas.rotate(i * 30);
            canvas.translate(0, -circleRadius + mCircleWidth + mCircleHWidth + rect.height() / 2);
            canvas.rotate(-i * 30);
            canvas.drawText(text, -rect.width() / 2, rect.height() / 2, paint);
            canvas.restore();
        }

    }


}
