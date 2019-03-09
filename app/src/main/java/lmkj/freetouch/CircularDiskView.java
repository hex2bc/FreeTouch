package lmkj.freetouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircularDiskView extends View {

    private static final String TAG = "CircularDiskView";
    private Paint mPaint = new Paint();
    private static final float mRadius = 300;
    private float mStartAngle = 0;
    private float mPieces = 6;
    private float mDownAngle;
    private float mPointX, mPointY;

    public CircularDiskView(Context context) {
        super(context, null);
    }

    public CircularDiskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        canvas.drawCircle(mRadius, mRadius, mRadius / 3, mPaint);
        canvas.drawCircle(mRadius, mRadius, mRadius / 3 + 50, mPaint);

        float piece = 360 / mPieces;
        for (int i = 0; i < mPieces; i++) {
            float sin= (float) Math.sin(Math.toRadians(mStartAngle + piece * i));
            float cos = (float) Math.cos(Math.toRadians(mStartAngle + piece * i));
            float startX = (cos * 150) + mRadius;
            float startY = (sin * 150) + mRadius;
            float endX = (cos * mRadius) + mRadius;
            float endY = (sin * mRadius) + mRadius;

            float drawX = (cos * 225) + mRadius;
            float drawY = (sin * 225) + mRadius;
//            canvas.drawLine(startX, startY, endX, endY, mPaint);
            canvas.drawText(Integer.toString(i), drawX, drawY, mPaint);
            canvas.drawCircle(drawX, drawY, 50, mPaint);
            canvas.drawRect(drawX - 50, drawY - 50, drawX + 50, drawY + 50, mPaint);
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
                mDownAngle = 0;
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
}
