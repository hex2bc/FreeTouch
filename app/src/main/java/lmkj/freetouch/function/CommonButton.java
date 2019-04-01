package lmkj.freetouch.function;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;
import lmkj.freetouch.SettingsActivity;

public class CommonButton extends DiskButtonInfo {

    public CommonButton(int index, int drawableId, String text, boolean front) {
        super(index, drawableId, text, front);
    }

    public CommonButton(int index, int drawableId, String text, boolean front, boolean remove, boolean del) {
        super(index, drawableId, text, front, remove, del);
    }

    @Override
    public void execute(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (drawableId) {
            case R.drawable.ic_calculator:
                intent.setClassName("com.android.calculator2",
                        "com.android.calculator2.Calculator");
                context.startActivity(intent);
                break;
            case R.drawable.ic_camera:
                intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
                context.startActivity(intent);
                break;
            case R.drawable.ic_file:
                intent.setClassName("com.mediatek.filemanager",
                        "com.mediatek.filemanager.FileManagerOperationActivity");
                context.startActivity(intent);
                break;
            case R.drawable.ic_setting:
                intent.setClassName("com.android.settings",
                        "com.android.settings.Settings");
                context.startActivity(intent);
                break;
            default:
                Toast.makeText(context, super.text, Toast.LENGTH_SHORT).show();
                    break;

        }

    }

    @Override
    public void updateIcon() {

    }
}
