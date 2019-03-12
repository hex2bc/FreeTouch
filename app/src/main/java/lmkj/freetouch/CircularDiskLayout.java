package lmkj.freetouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CircularDiskLayout extends ViewGroup {

    private static final int mRadius = 280;
    private static final String TAG = "CircularDiskLayout" ;
    private float mStartAngle = 0, mDownAngle, mTempAngle;
    private Paint mPaint = new Paint();

    public CircularDiskLayout(Context context) {
        super(context, null);
    }

    public CircularDiskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
//        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = child.getLayoutParams();
//            final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(76, MeasureSpec.EXACTLY);
//            final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(76, MeasureSpec.EXACTLY);
            child.measure(getMeasuredWidth(), getMeasuredHeight());
        }
        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        float piece = 360 / count;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            float sin= (float) Math.sin(Math.toRadians(mStartAngle + piece * i));
            float cos = (float) Math.cos(Math.toRadians(mStartAngle + piece * i));

            int drawX = (int) (cos * 208) + mRadius;
            int drawY = (int) (sin * 208) + mRadius;

            view.layout(drawX - 64, drawY - 64, drawX - 64 + 128, drawY - 64 + 128);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        canvas.drawCircle(mRadius, mRadius, 134, mPaint);
        canvas.drawCircle(mRadius, mRadius, 84, mPaint);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownAngle = computeCurrentAngle(x, y);
                mTempAngle = mStartAngle;
                Log.d(TAG, "huangqw onInterceptTouchEvent: ACTION_DOWN " + mDownAngle);
                break;
            case MotionEvent.ACTION_MOVE:
                float diff = computeCurrentAngle(x, y) - mDownAngle;
                if (Math.abs(diff) > 3) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownAngle = computeCurrentAngle(x, y);
                Log.d(TAG, "huangqw onTouchEvent: " + mDownAngle);
                mTempAngle = mStartAngle;
                return true;
            case MotionEvent.ACTION_MOVE:
                float diff = computeCurrentAngle(x, y) - mDownAngle;
                mStartAngle = mTempAngle + diff;
                Log.d(TAG, "huangqw onTouchEvent: start = " + mStartAngle);
                requestLayout();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                mDownAngle = 0;
                break;
        }

        return super.onTouchEvent(event);
    }

    private float computeCurrentAngle(float x, float y) {
        double degree = Math.atan2(y - mRadius, x - mRadius);
        return (float) Math.toDegrees(degree);
    }

    public void setDeleteButtonVisibility(boolean visible) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof BubbleTextView) {
                ((BubbleTextView) view).setDeleteButtonVisibility(visible);
                ((BubbleTextView) view).invalidate();
            }
        }
    }
}
