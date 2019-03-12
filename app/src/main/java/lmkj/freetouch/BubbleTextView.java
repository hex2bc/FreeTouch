package lmkj.freetouch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class BubbleTextView extends TextView {

    private Drawable mDeleteButtonDrawable;
    private boolean mDeleteButtonVisible = false;

    public BubbleTextView(Context context) {
        this(context, null);
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDeleteButtonDrawable = context.getResources().getDrawable(R.drawable.ic_delete);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawDeleteButton(canvas);
    }

    private void  drawDeleteButton(Canvas canvas) {
        if (mDeleteButtonVisible) {
            int deleteButtonWidth = mDeleteButtonDrawable.getIntrinsicWidth();
            int deleteButtonHeight = mDeleteButtonDrawable.getIntrinsicHeight();
            int deleteButtonPosX = getScrollX();
            int deleteButtonPosY = getScrollY();

            Rect deleteButtonBounds = new Rect(0, 0, deleteButtonWidth, deleteButtonHeight);
            mDeleteButtonDrawable.setBounds(deleteButtonBounds);
            canvas.save();
            canvas.translate(deleteButtonPosX, deleteButtonPosY);
            mDeleteButtonDrawable.draw(canvas);
            canvas.restore();
        }
    }
    public void setDeleteButtonVisibility(boolean visiable) {
        mDeleteButtonVisible = visiable;
    }

    public boolean getDeleteButtonVisibility() {
        return mDeleteButtonVisible;
    }
}
