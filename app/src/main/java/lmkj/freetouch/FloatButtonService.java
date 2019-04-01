package lmkj.freetouch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import lmkj.freetouch.local.SaveObjectDataSource;
import lmkj.freetouch.CircularDiskLayout.OnBackPressedListener;

public class FloatButtonService extends Service implements View.OnClickListener, View.OnLongClickListener,
        IDiskView {
    private static final String TAG = "FloatKeyService";
    private WindowManager wm = null;
    private WindowManager.LayoutParams floatKeyParams = null;

    private ImageView floatView;
    private int mScreenWidth;
    private int mScreenHeight;

    private CircularDiskLayout mCircularDiskLayout;
    private DiskPresenter mDiskPresenter;
    private Context mContext;
    private ImageView mMidBtn;
    private Drawable mMidBtnDef;
    private Drawable mMidBtnChange;

    private float mTouchStartX = 0, mTouchStartY = 0;
    private float mTouchOnBtnX = 0, mTouchOnBtnY = 0;
    private int mSlop;
    private boolean mOverMove = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean fromSetting = intent.getBooleanExtra("FromSettings", false);
        if (fromSetting) {
            initDiskView((int) mTouchStartY - 280);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mSlop = ViewConfiguration.get(mContext).getScaledWindowTouchSlop();
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        mDiskPresenter = new DiskPresenter(this, SaveObjectDataSource.getInstance(mContext));
        mMidBtnDef = mContext.getResources().getDrawable(R.drawable.ic_middle);
        mMidBtnChange = mContext.getResources().getDrawable(R.drawable.ic_change);
        createFloatButton();
    }

    private void createFloatButton() {

        floatKeyParams = new WindowManager.LayoutParams();
        floatKeyParams.x = mScreenWidth;
        floatKeyParams.y = mScreenHeight / 2;
        floatKeyParams.format = PixelFormat.RGBA_8888;
        floatKeyParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;

        floatKeyParams.type = LayoutParams.TYPE_TOAST;
        floatKeyParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        floatKeyParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatView = new ImageView(getApplicationContext());
        floatView.setLayoutParams(layoutParams);
        floatView.setImageResource(R.drawable.btn_floatkey);

        floatView.setOnTouchListener(mTouchListener);

        floatKeyParams.gravity = Gravity.LEFT | Gravity.TOP;
        wm.addView(floatView, floatKeyParams);
    }

    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = x;
                    mTouchStartY = y;
                    mTouchOnBtnX = event.getX();
                    mTouchOnBtnY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float offsetX = Math.abs(x - mTouchStartX);
                    float offsetY = Math.abs(y - mTouchStartY);
                    if (offsetX > mSlop || offsetY > mSlop) {
                        mOverMove = true;
                    }
                    updateViewPosition(x, y);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (!mOverMove) {
                        initDiskView(y - 280);
                    } else {
                        mOverMove = false;
                    }
                    autoKeepSide(x, y);
                    break;
            }
            return false;
        }
    };

    private void updateViewPosition(int x, int y) {
        floatKeyParams.x = x - (int) mTouchOnBtnX;
        floatKeyParams.y = y - (int) mTouchOnBtnY;
        wm.updateViewLayout(floatView, floatKeyParams);
    }

    private void autoKeepSide(int x, int y) {
        final int endY = y;
        int end = x < mScreenWidth / 2 ? 0 : mScreenWidth;
        final ValueAnimator anim = ValueAnimator.ofInt(x, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                updateViewPosition(x, endY);
            }
        });
        anim.start();
    }

    private void initDiskView(int y) {
        floatView.setVisibility(View.INVISIBLE);

        mCircularDiskLayout = new CircularDiskLayout(mContext);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCircularDiskLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_panel));
        mCircularDiskLayout.setLayoutParams(lp);

        mMidBtn = new ImageView(mContext);
        mMidBtn.setLayoutParams(lp);
        mMidBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_middle));
        mMidBtn.setClickable(true);
        mMidBtn.setOnClickListener(this);
        mCircularDiskLayout.addView(mMidBtn);

        createView(mDiskPresenter.getLayoutView());
        mCircularDiskLayout.setOnBackPressListener(mOnBackPressedListener);

        WindowManager.LayoutParams diskParams = new WindowManager.LayoutParams();
        diskParams.x = 0;
        diskParams.y = y;
        diskParams.format = PixelFormat.RGBA_8888;
        diskParams.flags = LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        diskParams.type = LayoutParams.TYPE_TOAST;
        diskParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        diskParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        diskParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        wm.addView(mCircularDiskLayout, diskParams);
    }

    private void removeDiskView(boolean showFloatView) {
        floatView.setVisibility(showFloatView ? View.VISIBLE : View.INVISIBLE);
        if (mCircularDiskLayout != null) {
            wm.removeView(mCircularDiskLayout);
        }
    }

    OnBackPressedListener mOnBackPressedListener = new OnBackPressedListener() {
        @Override
        public void onBackPressed() {
            if (mDiskPresenter.onBackPress()) {
                removeDiskView(true);
            }
        }
    };

    private void createView(ArrayList<DiskButtonInfo> DiskInfo) {

        for (DiskButtonInfo info : DiskInfo) {
            BubbleTextView tv = (BubbleTextView) LayoutInflater.from(this)
                    .inflate(R.layout.disk_icon_style, mCircularDiskLayout, false);
            tv.createButtonFromInfo(info);
            if (info.isFront) {
                tv.setOnClickListener(this);
                tv.setOnLongClickListener(this);
            }
            mCircularDiskLayout.addView(tv);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mMidBtn)) {
            mDiskPresenter.middleButtonClick();
        }
        if (v instanceof BubbleTextView) {
            DiskButtonInfo info = ((BubbleTextView) v).getButtonInfo();
            if (info.text.equals("More")) {
                mDiskPresenter.moreButtonClick();
            } else if (mCircularDiskLayout.isModifyMode()) {
                if (info.isRemove) {
                    mDiskPresenter.addButtonClick(mContext, info.index);
                    removeDiskView(false);
                } else {
                    mDiskPresenter.deleteButtonClick(info);
                }
            } else {
                int id = info.drawableId;
                mDiskPresenter.buttonClick(mContext, info);
                removeDiskView(true);
                if (id != info.drawableId) {
                    ((BubbleTextView) v).createButtonFromInfo(info);
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mDiskPresenter.longClick();
        return true;
    }

    @Override
    public void updateDiskView() {
        mCircularDiskLayout.removeViews(1, mCircularDiskLayout.getChildCount() - 1);
        createView(mDiskPresenter.getLayoutView());
    }

    @Override
    public void showChanged(boolean change) {
        if (change) {
            mMidBtn.setImageDrawable(mMidBtnChange);
        } else {
            mMidBtn.setImageDrawable(mMidBtnDef);
        }
        updateDiskView();
    }

    @Override
    public void showModifyMode(boolean show) {
        mCircularDiskLayout.setModifyMode(show);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatView != null)
            wm.removeView(floatView);
    }
}

