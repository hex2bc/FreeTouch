package lmkj.freetouch.function;

import android.content.Context;
import android.content.Intent;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.FloatButtonService;
import lmkj.freetouch.R;

public class WifiButton extends DiskButtonInfo {

    private static int mWifiOnIcon = R.drawable.ic_wifi_on;
    private static int mWifiOffIcon = R.drawable.ic_wifi_off;
    private boolean mWifiOn = true;

    public WifiButton(int i, boolean front) {
        this(i, mWifiOnIcon, "Wifi", front);
        updateIcon();
    }

    public WifiButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    @Override
    public void execute(Context context) {
        mWifiOn = !mWifiOn;
        updateIcon();
    }

    @Override
    public void updateIcon() {
        if (mWifiOn) {
            super.drawableId = mWifiOnIcon;
        } else {
            super.drawableId = mWifiOffIcon;
        }
    }
}
