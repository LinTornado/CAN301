package com.example.pair_click.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pair_click.R;

import java.util.ArrayList;
import java.util.List;

public class GestureCanvas extends View {
    private Bitmap error,normal,press;
    private ArrayList<Dot> pointlist=new ArrayList<>();
    private  Dot[][] points = new Dot[3][3];
    private Paint  paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint errorPaint = new Paint();
    private Paint pressPaint = new Paint();
    private OnDrawFinsihListner listner;
    private float radius;
    private int mTotalWidth, mTotalHeight;
    private Rect mSrcRect, mDestRect;
    private int mBitWidth, mBitHeight;
    private boolean flag = false;
    private  float mouseX,mouseY;
    private  ArrayList<Integer> sequence = new ArrayList<>();
    private  boolean Drawing = false;
    private boolean end = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }

    public GestureCanvas(Context context) {
        super(context);
    }

    public GestureCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GestureCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!flag){
            init();
        }

        drawPoints(canvas);

        if (pointlist.size() > 0)
        {
            Dot a = pointlist.get(0);
            for (int i = 1;i < pointlist.size(); i++)
            {
                Dot b = pointlist.get(i);
                drawLine(canvas, a, b);
                a = b;
            }

            if (Drawing)
            {
                drawLine(canvas, a, new Dot(mouseX, mouseY));
            }
        }
    }

    private  void drawPoints(Canvas canvas){
        for (int i = 0;i < points.length; i++ ) {
            for (int j = 0; j < points[i].length; j++) {
                mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
                mDestRect = new Rect((5*i+3)*mTotalWidth/20, (j+1)*(mTotalHeight/8), (5*i+3)*mTotalWidth/20+mBitWidth/5, (j+1) * (mTotalHeight/8)+mBitHeight/5);
                if (points[i][j].state == Dot.STATE_NORMAL) {
                    canvas.drawBitmap(normal, mSrcRect, mDestRect, paint);
                }
                else  if (points[i][j].state== Dot.STATE_PRESS)
               {
                   canvas.drawBitmap(press, mSrcRect, mDestRect, paint);
               }else {
                    canvas.drawBitmap(error, mSrcRect, mDestRect, paint);
               }
            }
        }
    }

    private  void init(){
        error = BitmapFactory.decodeResource(getResources(), R.drawable.dot_error);
        press = BitmapFactory.decodeResource(getResources(),R.drawable.dot_pressed);
        normal = BitmapFactory.decodeResource(getResources(),R.drawable.dot_unpress);
        mBitWidth = press.getWidth();
        mBitHeight = press.getHeight();
        pressPaint.setColor(Color.BLUE);
        pressPaint.setStrokeWidth(10);
        errorPaint.setColor(Color.RED);
        errorPaint.setStrokeWidth(5);
        radius = error.getHeight() / 10;
        for (int i = 0;i<3;i++)
        {
            for (int j = 0; j < 3; j++){
                points[i][j] = new Dot((i*mTotalWidth/20)+(i+1)*(mTotalWidth/5)-mTotalWidth/20+radius,(j+1)*(mTotalHeight/8)+radius);
            }
        }
        flag = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mouseX = event.getX();
        mouseY = event.getY();
        int[] ij;
        int i,j;
        if(!end) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ij = getPosition();
                    if (ij != null) {
                        Drawing = true;
                        i = ij[0];
                        j = ij[1];
                        points[i][j].state = 1;
                        pointlist.add(points[i][j]);
                        sequence.add(i * 3 + j + 1);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Drawing) {
                        ij = getPosition();
                        if (ij != null) {
                            i = ij[0];
                            j = ij[1];
                            if (!pointlist.contains(points[i][j])) {
                                points[i][j].state = 1;
                                pointlist.add(points[i][j]);
                                sequence.add(i * 3 + j + 1);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    boolean valid = false;
                    if (listner != null && Drawing) {
                        valid = listner.OnDrawFinished(sequence);
                    }
                    if (!valid) {
                        for (Dot point : pointlist)
                            point.state = 2;
                    }
                    Drawing = false;
                    ij = getPosition();
                    if(ij != null) {
                        end = true;
                    }
                    break;
            }
        }
        this.postInvalidate();
        return true;
    }

    private int[] getPosition() {
        Dot pMouse = new Dot(mouseX,mouseY);
        for (int i = 0; i < points.length; i++){
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].inornot(mouseX, mouseY, radius)) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return  null;
    }

    private  void drawLine(Canvas canvas, Dot a, Dot b) {
        if (a.state == 1) {
            canvas.drawLine(a.x,a.y,b.x,b.y,pressPaint);
        }
        else if (a.state == 2){
            canvas.drawLine(a.x,a.y,b.x,b.y,errorPaint);
        }
    }

    public  void  resetPoint(){
        pointlist.clear();
        sequence.clear();
        end = false;
        for (int i = 0; i < points.length; i++)
        {
            for (int k = 0; k < points[i].length; k++)
                points[i][k].state = 0;
        }
        this.postInvalidate();
    }

    public interface OnDrawFinsihListner
    {
        boolean OnDrawFinished(List<Integer> sequence);
    }

    public  void setOnDrawFinishedListener(OnDrawFinsihListner listener)
    {
        this.listner = listener;
    }
}