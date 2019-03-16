package lmkj.freetouch.function;

import android.content.Context;
import android.widget.Toast;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;

public class CommonButton extends DiskButtonInfo {

    public CommonButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    public CommonButton(int index, int drawableId, String text, boolean front, boolean remove, boolean del) {
        super(index, drawableId, text, front, remove, del);
    }

    @Override
    public void execute(Context context) {
        Toast.makeText(context, super.text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateIcon() {

    }
}
