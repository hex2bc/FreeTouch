package lmkj.freetouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CircularDiskView extends View {

    private static final String TAG = "CircularDiskView";
    private static final boolean DEBUG = false;
    private Paint mPaint = new Paint();
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private static final float mRadius = 280;
    private float mStartAngle = 0;
    private float mPieces = 7;
    private float mDownAngle;
    private float mPointX, mPointY;
    private Bitmap bg_panel, bg_small_panel, bg_mini_panel;
    private ArrayList<RotateButton> mButtonList = new ArrayList<>();


    public CircularDiskView(Context context) {
        super(context, null);
    }

    public CircularDiskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        bg_panel = BitmapFactory.decodeResource(getResources(), R.drawable.bg_panel);
        bg_small_panel = BitmapFactory.decodeResource(getResources(), R.drawable.bg_small_panel);
        bg_mini_panel = BitmapFactory.decodeResource(getResources(), R.drawable.bg_mini_panel);
        mButtonList.add(new RotateButton(0, BitmapFactory.decodeResource(getResources(), R.drawable.ic_back), "Back"));
        mButtonList.add(new RotateButton(1, BitmapFactory.decodeResource(getResources(), R.drawable.ic_bluetooth_on), "Bluetooth"));
        mButtonList.add(new RotateButton(2, BitmapFactory.decodeResource(getResources(), R.drawable.ic_calculator), "Calculator"));
        mButtonList.add(new RotateButton(3, BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera), "Camera"));
        mButtonList.add(new RotateButton(4, BitmapFactory.decodeResource(getResources(), R.drawable.ic_hotspot_on), "Hot spot"));
        mButtonList.add(new RotateButton(5, BitmapFactory.decodeResource(getResources(), R.drawable.ic_rotation), "Rotation"));
        mButtonList.add(new RotateButton(6, BitmapFactory.decodeResource(getResources(), R.drawable.ic_more), "More"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bg_panel,0, 0, mPaint);
        canvas.drawBitmap(bg_small_panel,146, 146, mPaint);
        canvas.drawBitmap(bg_mini_panel,196, 196, mPaint);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        canvas.drawCircle(mRadius, mRadius, 134, mPaint);
        canvas.drawCircle(mRadius, mRadius, 84, mPaint);
        float piece = 360 / mButtonList.size();

        for (RotateButton btn : mButtonList) {
            float sin= (float) Math.sin(Math.toRadians(mStartAngle + piece * btn.index));
            float cos = (float) Math.cos(Math.toRadians(mStartAngle + piece * btn.index));

            float drawX = (cos * 208) + mRadius;
            float drawY = (sin * 208) + mRadius;

            canvas.drawBitmap(btn.bitmap, drawX - 38, drawY - 52, mPaint);

            // 字体居中
            StaticLayout staticLayout = new StaticLayout(btn.text, mTextPaint,
                    128, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
            canvas.save();
            canvas.translate(drawX - 64, drawY + 28);
            staticLayout.draw(canvas);
            canvas.restore();

            // 画辅助线
            if (DEBUG) {
                float startX = (cos * 134) + mRadius;
                float startY = (sin * 134) + mRadius;
                float endX = (cos * mRadius) + mRadius;
                float endY = (sin * mRadius) + mRadius;
                canvas.drawLine(startX, startY, endX, endY, mPaint); // 中线
                canvas.drawRect(drawX - 64, drawY - 64, drawX + 64, drawY + 64, mPaint); // 大框
                canvas.drawRect(drawX - 38, drawY - 52, drawX + 38, drawY + 28, mPaint); // 图片框
                canvas.drawRect(drawX - 64, drawY + 28, drawX + 64, drawY + 52, mPaint); // 标题框
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownAngle = mStartAngle;
                break;
            case MotionEvent.ACTION_MOVE:
                mStartAngle = computeCurrentAngle(x, y) - mDownAngle;
                Log.d(TAG, "huangqw onTouchEvent: " + mStartAngle);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                mDownAngle = 0;
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) mRadius *2, (int) mRadius *2);
    }

    private float computeCurrentAngle(float x, float y) {

        double degree = Math.atan2(y - mRadius, x - mRadius);


        return (float) Math.toDegrees(degree);
    }

    class RotateButton {
        int index;
        Bitmap bitmap;
        String text;

        public RotateButton(int index, Bitmap bitmap, String text) {
            this.index = index;
            this.bitmap = bitmap;
            this.text = text;
        }
    }
}
