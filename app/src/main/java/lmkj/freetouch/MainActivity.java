package lmkj.freetouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    CircularDiskLayout mCircularDiskLayout;
    DiskButtonInfo mMore;
    private ArrayList<DiskButtonInfo> mDiskButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircularDiskLayout = (CircularDiskLayout) findViewById(R.id.main);

        mDiskButtons.add(new DiskButtonInfo(0, R.drawable.ic_camera, "Camera", true));
        mMore = new DiskButtonInfo(1, R.drawable.ic_more, "More", true);
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
        mDiskButtons.add(new DiskButtonInfo(16, R.drawable.ic_bluetooth_on, "More", false));
        mDiskButtons.add(new DiskButtonInfo(17, R.drawable.ic_wifi_on, "Wifi", false));

        createView();
    }

    private void createView() {
        for (DiskButtonInfo info : mDiskButtons) {
            createButton(info);
        }
    }

    private void exchange() {
        Iterator<DiskButtonInfo> iter = mDiskButtons.iterator();
        while (iter.hasNext()) {
            DiskButtonInfo info = iter.next();
            info.isFront = !info.isFront;
        }
        updateDiskView();
    }

    private void createButton(DiskButtonInfo info) {
        LayoutInflater mInflater = getLayoutInflater();
        BubbleTextView tv = (BubbleTextView) mInflater.inflate(R.layout.disk_icon_style, mCircularDiskLayout, false);
        tv.createButtonFromInfo(info);
        if (info.isFront) {
            tv.setOnClickListener(this);
            tv.setOnLongClickListener(this);
        }
        mCircularDiskLayout.addView(tv);
    }

    private void updateDiskView() {
        mCircularDiskLayout.removeAllViews();
        createView();
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag.equals(mMore)) {
            exchange();
        } else if (mCircularDiskLayout.isModifyMode()) {
            if (tag instanceof DiskButtonInfo) {
                deleteButton((DiskButtonInfo) tag);
            }
        } else if (tag instanceof DiskButtonInfo) {
            Toast.makeText(this, ((DiskButtonInfo) tag).text, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteButton(DiskButtonInfo info) {
        if (info.canDelete) {
            info.remove();
            updateDiskView();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!mCircularDiskLayout.isModifyMode()) {
            mCircularDiskLayout.setModifyMode(true);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mCircularDiskLayout.isModifyMode()) {
            mCircularDiskLayout.setModifyMode(false);
        } else {
            super.onBackPressed();
        }
    }
}
