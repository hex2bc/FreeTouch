package lmkj.freetouch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


public class DiskFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener,
        IDiskView {
    private String TAG = "DiskFragment";

    private CircularDiskLayout mCircularDiskLayout;
    private DiskPresenter mDiskPresenter;
    private Context mContext;
    private ImageView mMidBtn;
    private Drawable mMidBtnDef;
    private Drawable mMidBtnChange;

    public DiskFragment() {
        // Required empty public constructor
    }

    public static DiskFragment newInstance() {
        DiskFragment fragment = new DiskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDiskPresenter = new DiskPresenter(this);
        mContext = getActivity();
        mMidBtnDef = mContext.getResources().getDrawable(R.drawable.ic_middle);
        mMidBtnChange = mContext.getResources().getDrawable(R.drawable.ic_change);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCircularDiskLayout = new CircularDiskLayout(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCircularDiskLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_panel));
        mCircularDiskLayout.setLayoutParams(lp);

        mMidBtn = new ImageView(getActivity());
        mMidBtn.setLayoutParams(lp);
        mMidBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_middle));
        mMidBtn.setClickable(true);
        mMidBtn.setOnClickListener(this);
        mCircularDiskLayout.addView(mMidBtn);

        createView(mDiskPresenter.getLayoutView());
        return mCircularDiskLayout;
    }


    private void createView(ArrayList<DiskButtonInfo> DiskInfo) {

        for (DiskButtonInfo info : DiskInfo) {
            LayoutInflater mInflater = getLayoutInflater();
            BubbleTextView tv = (BubbleTextView) mInflater.inflate(R.layout.disk_icon_style,
                    mCircularDiskLayout, false);
            tv.createButtonFromInfo(info);
            if (info.isFront) {
                tv.setOnClickListener(this);
                tv.setOnLongClickListener(this);
            }
            mCircularDiskLayout.addView(tv);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (v.equals(mMidBtn)) {
            mDiskPresenter.middleButtonClick();
        }
        if (tag == null) return;
        if (tag instanceof DiskButtonInfo) {
            DiskButtonInfo info = ((DiskButtonInfo) tag);
            if (info.text.equals("More")) {
                mDiskPresenter.moreButtonClick();
            } else if (mCircularDiskLayout.isModifyMode()) {
                if (info.isRemove) {
                    mDiskPresenter.addButtonClick(info.index);
                } else {
                    mDiskPresenter.deleteButtonClick(info);
                }
            } else if (tag instanceof DiskButtonInfo) {
                Toast.makeText(getActivity(), info.text, Toast.LENGTH_SHORT).show();
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

    public boolean allowBackPressed() {
        Log.d(TAG, "huangqw allowBackPressed: ");
        return mDiskPresenter.onBackPress();
    }
}
