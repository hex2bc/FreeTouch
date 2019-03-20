package lmkj.freetouch;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import java.util.ArrayList;

import lmkj.freetouch.local.SaveObjectDataSource;

public class FloatButtonService extends Service implements View.OnClickListener, View.OnLongClickListener,
        IDiskView{
    private static final String TAG = "FloatKeyService";
    private WindowManager wm = null;
    private float mOffset = 4f;
    private float mOffsetX, mOffsetY;
    private float mTStartsX, mTStartsY;
    private WindowManager.LayoutParams floatKeyParams = null;
    private WindowManager.LayoutParams moreKeyParams = null;

    private ImageView floatView;

    private CircularDiskLayout mCircularDiskLayout;
    private DiskPresenter mDiskPresenter;
    private Context mContext;
    private ImageView mMidBtn;
    private Drawable mMidBtnDef;
    private Drawable mMidBtnChange;

    private float mTouchStartX, mTouchStartY;
    private float x, y;
    boolean initViewPlace = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mDiskPresenter = new DiskPresenter(this, SaveObjectDataSource.getInstance(mContext));
        mMidBtnDef = mContext.getResources().getDrawable(R.drawable.ic_middle);
        mMidBtnChange = mContext.getResources().getDrawable(R.drawable.ic_change);
        createFloatButton();
    }

    private void createFloatButton() {
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        floatKeyParams = new WindowManager.LayoutParams();
        floatKeyParams.x = 0;
        floatKeyParams.y = 0;
        floatKeyParams.format = PixelFormat.RGBA_8888;
        floatKeyParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;

        floatKeyParams.type = LayoutParams.TYPE_TOAST;
        floatKeyParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        floatKeyParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        floatView = new ImageView(getApplicationContext());
        floatView.setImageResource(R.drawable.btn_floatkey);

        initViewPlace = false;
        floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!initViewPlace) {
                            initViewPlace = true;
                            mTouchStartX = event.getRawX();
                            mTouchStartY = event.getRawY();
                            x = event.getRawX();
                            y = event.getRawY();
                        } else {
                            mTouchStartX += (event.getRawX() - x);
                            mTouchStartY += (event.getRawY() - y);
                        }
                        mTStartsX = event.getRawX();
                        mTStartsY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = event.getRawX();
                        y = event.getRawY();
                        updateViewPosition();
                        break;

                    case MotionEvent.ACTION_UP:
                        mOffsetX = Math.abs(event.getRawX() - mTStartsX);
                        mOffsetY = Math.abs(event.getRawY() - mTStartsY);
                        if (mOffsetX < mOffset && mOffsetY < mOffset) {
//                            floatView.setVisibility(View.GONE);
//                            InitMoreKeyView();

                            initDiskView();

                        }

                        break;
                }
                return false;
            }
        });

        floatKeyParams.gravity = Gravity.LEFT | Gravity.TOP;
        wm.addView(floatView, floatKeyParams);
    }


    private void updateViewPosition() {
        floatKeyParams.x = (int) (x - mTouchStartX);
        floatKeyParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(floatView, floatKeyParams);
    }

    private void initDiskView() {
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
        mCircularDiskLayout.setOnBackPressListener(new CircularDiskLayout.OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                if (mDiskPresenter.onBackPress()) {
                    wm.removeView(mCircularDiskLayout);
                }
            }
        });

        WindowManager.LayoutParams diskParams = new WindowManager.LayoutParams();
        diskParams.x = 0;
        diskParams.y = 0;
        diskParams.format = PixelFormat.RGBA_8888;
        diskParams.flags = LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        diskParams.type = LayoutParams.TYPE_TOAST;
        diskParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        diskParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wm.addView(mCircularDiskLayout, diskParams);

    }

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
                    mDiskPresenter.addButtonClick(info.index);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.putExtra("index", info.index);
                    intent.setClass(mContext, SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    mDiskPresenter.deleteButtonClick(info);
                }
            } else {
                int id = info.drawableId;
                mDiskPresenter.buttonClick(mContext, info);
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

