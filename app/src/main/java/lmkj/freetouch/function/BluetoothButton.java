package lmkj.freetouch.function;

import android.content.Context;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;

public class BluetoothButton extends DiskButtonInfo {

    private static int mBtOnIcon = R.drawable.ic_bluetooth_on;
    private static int mBtOffIcon = R.drawable.ic_bluetooth_off;

    public BluetoothButton(int i, boolean front) {
        this(i, mBtOnIcon, "Bluetooth", front);
        updateIcon();
    }

    public BluetoothButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    @Override
    public void execute(Context context) {

    }

    @Override
    public void updateIcon() {

    }
}
