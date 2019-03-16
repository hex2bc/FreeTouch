package lmkj.freetouch.function;

import android.content.Context;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;

public class FlashlightButton extends DiskButtonInfo {

    private static int mOn = R.drawable.ic_light_on;
    private static int mOff = R.drawable.ic_light_off;

    public FlashlightButton(int i, boolean front) {
        this(i, mOff, "Light", front);
        updateIcon();
    }

    public FlashlightButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    @Override
    public void execute(Context context) {

    }

    @Override
    public void updateIcon() {

    }
}
