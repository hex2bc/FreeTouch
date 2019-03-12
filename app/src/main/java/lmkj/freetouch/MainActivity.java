package lmkj.freetouch;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    CircularDiskLayout mCircularDiskLayout;
    private ArrayList<DiskButtonInfo> mDiskButtons = new ArrayList<>();
    private enum State { NONE, NORMAL, MODIFY};
    private State mState = State.NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircularDiskLayout = (CircularDiskLayout) findViewById(R.id.main);

        mDiskButtons.add(new DiskButtonInfo(0, R.drawable.ic_camera, "Camera"));
        mDiskButtons.add(new DiskButtonInfo(1, R.drawable.ic_back, "Back"));
        mDiskButtons.add(new DiskButtonInfo(2, R.drawable.ic_bluetooth_off, "Bluetooth"));
        mDiskButtons.add(new DiskButtonInfo(3, R.drawable.ic_calculator, "Calculator"));
        mDiskButtons.add(new DiskButtonInfo(4, R.drawable.ic_more, "More"));
        mDiskButtons.add(new DiskButtonInfo(5, R.drawable.ic_home, "Home"));
        mDiskButtons.add(new DiskButtonInfo(6, R.drawable.ic_file, "File"));

        createView();
    }

    private void createView() {
        for (DiskButtonInfo info : mDiskButtons) {
            createButton(info);
        }
    }

    private void createButton(DiskButtonInfo info) {
        LayoutInflater mInflater = getLayoutInflater();
        BubbleTextView tv = (BubbleTextView) mInflater.inflate(R.layout.disk_icon_style, mCircularDiskLayout, false);
        Drawable drawable = getResources().getDrawable(info.drawableId);
        drawable.setBounds(0, 0, 76, 76);
        tv.setText(info.text);
        tv.setTag(info);
        tv.setOnClickListener(this);
        tv.setOnLongClickListener(this);
        tv.setCompoundDrawables(null, drawable, null, null);
        mCircularDiskLayout.addView(tv);
    }

    private void updateDiskView() {
        mCircularDiskLayout.removeAllViews();
        createView();
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (mState == State.MODIFY) {
            if (tag instanceof DiskButtonInfo) {
                mDiskButtons.remove(tag);
                updateDiskView();
            }
        } else if (tag instanceof DiskButtonInfo) {
            Toast.makeText(this, ((DiskButtonInfo) tag).text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mState == State.NORMAL) {
            mState = State.MODIFY;
            mCircularDiskLayout.setDeleteButtonVisibility(true);
        } else if (mState == State.MODIFY) {
            mState = State.NORMAL;
            mCircularDiskLayout.setDeleteButtonVisibility(false);
        }
        Toast.makeText(this, "Long Press!", Toast.LENGTH_SHORT).show();
        return true;
    }
}
