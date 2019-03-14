package lmkj.freetouch;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class DiskPresenter implements IDiskPresenter {
    private String TAG = "DiskPresenter";
    private IDiskView mDiskView;
    private DiskModel mDiskModel;
    private ArrayList<DiskButtonInfo> mDiskButtons =  new ArrayList<>();
    boolean isExchange = false;
    boolean isModify = false;

    public DiskPresenter(IDiskView diskView) {
        mDiskView = diskView;
        mDiskModel = new DiskModel(this);
        initViews();
    }

    private void initViews() {
        mDiskButtons.add(new DiskButtonInfo(0, R.drawable.ic_camera, "Camera", true));
        DiskButtonInfo mMore = new DiskButtonInfo(1, R.drawable.ic_more, "More", true);
        mMore.canDelete = false;
        mDiskButtons.add(mMore);
        mDiskButtons.add(new DiskButtonInfo(2, R.drawable.ic_home, "Home", true));
        mDiskButtons.add(new DiskButtonInfo(3, R.drawable.ic_calculator, "Calculator", true));
        mDiskButtons.add(new DiskButtonInfo(4, R.drawable.ic_light_off, "Light", true));
        mDiskButtons.add(new DiskButtonInfo(5, R.drawable.ic_lock, "Lock", true));
        mDiskButtons.add(new DiskButtonInfo(6, R.drawable.ic_setting, "Setting", true));
        mDiskButtons.add(new DiskButtonInfo(7, R.drawable.ic_ringling, "Ringling", true));
        mDiskButtons.add(new DiskButtonInfo(10, R.drawable.ic_hotspot_off, "Hot spot", false));
        mDiskButtons.add(new DiskButtonInfo(11, R.drawable.ic_back, "Back", false));
        mDiskButtons.add(new DiskButtonInfo(12, R.drawable.ic_home, "Home", false));
        mDiskButtons.add(new DiskButtonInfo(13, R.drawable.ic_file, "File Manager", false));
        mDiskButtons.add(new DiskButtonInfo(14, R.drawable.ic_network_on, "Network", false));
        mDiskButtons.add(new DiskButtonInfo(15, R.drawable.ic_rotation, "Rotation", false));
        mDiskButtons.add(new DiskButtonInfo(16, R.drawable.ic_bluetooth_on, "Bluetooth", false));
        mDiskButtons.add(new DiskButtonInfo(17, R.drawable.ic_wifi_on, "Wifi", false));
    }

    @Override
    public ArrayList<DiskButtonInfo> getLayoutView() {
        return mDiskButtons;
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
        isExchange =  !isExchange;
        Iterator<DiskButtonInfo> iter = mDiskButtons.iterator();
        while (iter.hasNext()) {
            DiskButtonInfo info = iter.next();
            info.isFront = !info.isFront;
        }
    }
}
