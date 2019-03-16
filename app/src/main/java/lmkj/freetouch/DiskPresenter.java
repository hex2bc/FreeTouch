package lmkj.freetouch;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import lmkj.freetouch.local.DiskLocalDataSource;

public class DiskPresenter implements IDiskPresenter {
    private String TAG = "DiskPresenter";
    private IDiskView mDiskView;
    boolean isExchange = false;
    boolean isModify = false;
    DiskLocalDataSource mDataDb;

    public DiskPresenter(IDiskView diskView, DiskLocalDataSource tasks) {
        mDiskView = diskView;
        mDataDb = tasks;
    }

    @Override
    public ArrayList<DiskButtonInfo> getLayoutView() {
        return mDataDb.getDisk();
    }

    @Override
    public void addButtonClick(int index) {
        Log.d(TAG, "huangqw addButtonClick: ");
    }

    @Override
    public void deleteButtonClick(DiskButtonInfo info) {
        Log.d(TAG, "huangqw deleteButtonClick: ");
        if (isModify) {
            if (info.canDelete) {
                info.remove();
                mDataDb.refreshDisk(info);
                mDiskView.updateDiskView();
            }
        }
    }

    @Override
    public void moreButtonClick() {
        Log.d(TAG, "huangqw moreButtonClick: ");
        if (!isExchange) {
            exchange();
            mDiskView.showChanged(true);
        }
    }

    @Override
    public void middleButtonClick() {
        if (isExchange) {
            exchange();
            mDiskView.showChanged(false);
        }

        Log.d(TAG, "huangqw middleButtonClick: ");
    }

    @Override
    public void longClick() {
        if (!isModify) {
            isModify = true;
            mDiskView.showModifyMode(true);
        }
        Log.d(TAG, "huangqw longClick: ");
    }

    @Override
    public void buttonClick(Context context, DiskButtonInfo info) {
        info.execute(context);
    }

    @Override
    public boolean onBackPress() {
        boolean result = true;
        if (isExchange) {
            exchange();
            mDiskView.showChanged(false);
            result = false;
        }
        if (isModify) {
            isModify = false;
            mDiskView.showModifyMode(false);
            result = false;
        }
        return result;
    }

    private void exchange() {
        isExchange = !isExchange;
        ArrayList<DiskButtonInfo> tasks = mDataDb.getDisk();
        Iterator<DiskButtonInfo> iter = tasks.iterator();
        while (iter.hasNext()) {
            DiskButtonInfo info = iter.next();
            info.isFront = !info.isFront;
            mDataDb.refreshDisk(info);
        }
    }
}
