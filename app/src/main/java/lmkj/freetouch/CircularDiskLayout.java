package lmkj.freetouch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CircularDiskLayout extends ViewGroup {

    private static final int mRadius = 280;
    private static final String TAG = "CircularDiskLayout" ;
    private float mStartAngle = 0, mDownAngle, mTempAngle;
    private Paint mPaint = new Paint();
    protected enum State { NONE, NORMAL, MODIFY }
    protected State mState = State.NORMAL;
    private OnBackPressedListener mListener;
    private boolean isTouchOutOfBounds = false;

    public CircularDiskLayout(Context context) {
        this(context, null);
    }

    public CircularDiskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
//        setWillNotDraw(false);
    }

    public void setModifyMode(boolean yes) {
        mState = yes ? State.MODIFY : State.NORMAL;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof BubbleTextView) {
                DiskButtonInfo info = ((BubbleTextView) child).getButtonInfo();
                if (info.isRemove) {
                    if (yes) {
                        child.setVisibility(VISIBLE);
                    } else {
                        child.setVisibility(GONE);
                    }
                }
            }
        }
        requestLayout();
    }

    @Override
    public void onViewAdded(View child) {
        if (child instanceof BubbleTextView) {
            DiskButtonInfo info = ((BubbleTextView) child).getButtonInfo();
            if (info.isRemove) {
                if (isModifyMode()) {
                    child.setVisibility(VISIBLE);
                } else {
                    child.setVisibility(GONE);
                }
            }
        }
        super.onViewAdded(child);
    }

    public boolean isModifyMode() {
        return mState == State.MODIFY;
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

        int front_count = 0, front_index = 0;
        int back_count = 0, back_index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof BubbleTextView) {
                DiskButtonInfo info = ((BubbleTextView) view).getButtonInfo();
                if (info.isRemove) continue;
                if (info.isFront) {
                    front_count++;
                } else {
                    back_count ++;
                }
            }
        }
        if (front_count < 1 || back_count < 1) return;
        float front_piece = 360 / front_count;
        float back_piece = 360 / back_count;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof BubbleTextView) {
                if (view.getVisibility() == GONE) continue;
                DiskButtonInfo info = ((BubbleTextView) view).getButtonInfo();
                if (info.isFront) {
                    float angle = 0;
                    if (isModifyMode()) {
                        angle = 45 * info.index;
                        ((BubbleTextView) view).setDeleteButtonVisibility(info.canDelete ? true : false);
                    } else {
                        angle = front_piece * front_index++;
                        ((BubbleTextView) view).setDeleteButtonVisibility(false);
                    }

                    float sin= (float) Math.sin(Math.toRadians(mStartAngle + angle));
                    float cos = (float) Math.cos(Math.toRadians(mStartAngle + angle));
                    int drawX = (int) (cos * 208) + mRadius;
                    int drawY = (int) (sin * 208) + mRadius;
                    view.layout(drawX - 64, drawY - 64, drawX - 64 + 128, drawY - 64 + 128);
                } else if (!info.isFront && !info.isRemove) {
                    float angle = back_piece * front_index++;
                    float sin = (float) Math.sin(Math.toRadians(angle));
                    float cos = (float) Math.cos(Math.toRadians(angle));
                    int drawX = (int) (cos * 109) + mRadius;
                    int drawY = (int) (sin * 109) + mRadius;

                    view.layout(drawX - 20, drawY - 20, drawX - 20 + 40, drawY - 20 + 40);
                }
            }

            if (view instanceof ImageView) {
                view.layout(mRadius - 46, mRadius - 46, mRadius + 46, mRadius + 46);
            }
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        super.onDraw(canvas);
//        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
//        canvas.drawCircle(mRadius, mRadius, 134, mPaint);
//        canvas.drawCircle(mRadius, mRadius, 84, mPaint);
//    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownAngle = computeCurrentAngle(x, y);
                mTempAngle = mStartAngle;
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

    public void setOnBackPressListener(OnBackPressedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "huangqw dispatchKeyEvent: KEYCODE_BACK");
            if (mListener != null && event.getAction() == KeyEvent.ACTION_UP) {
                mListener.onBackPressed();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isOutOfBounds(getContext(), event)) {
                    isTouchOutOfBounds = true;
                } else {
                    mDownAngle = computeCurrentAngle(x, y);
                    mTempAngle = mStartAngle;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isTouchOutOfBounds) {
                    float diff = computeCurrentAngle(x, y) - mDownAngle;
                    mStartAngle = mTempAngle + diff;
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isTouchOutOfBounds) {
                    isTouchOutOfBounds = false;
                    if (mListener != null) {
                        mListener.onBackPressed();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        int width = getWidth() + slop;
        int height = getHeight() + slop;
        if((x < -slop) || (y < -slop) || (x > width) || (y > height)) {
            return true;
        } else if ((x <= width) || (y <= height)) {
            int a = x - mRadius;
            int b = y - mRadius;
            double radius = Math.sqrt(a * a + b * b);
            if (radius > mRadius) {
                Log.d(TAG, "huangqw onTouchEvent: isOutOfBounds radius = " + radius);
                return true;
            }
        }
        return false;
    }

    private float computeCurrentAngle(float x, float y) {
        double degree = Math.atan2(y - mRadius, x - mRadius);
        return (float) Math.toDegrees(degree);
    }

    public void setDeleteButtonVisibility(boolean visible) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof BubbleTextView) {
                if (((BubbleTextView) view).getButtonInfo().isFront) {
                    ((BubbleTextView) view).setDeleteButtonVisibility(visible);
                    ((BubbleTextView) view).invalidate();
                }
            }
        }
    }

    interface OnBackPressedListener {
        void onBackPressed();
    }
}
