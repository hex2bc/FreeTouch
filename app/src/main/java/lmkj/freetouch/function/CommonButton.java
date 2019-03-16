package lmkj.freetouch.function;

import android.content.Context;
import android.widget.Toast;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;

public class CommonButton extends DiskButtonInfo {

    public CommonButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    @Override
    public void execute(Context context) {
        Toast.makeText(context, super.text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateIcon() {

    }
}
