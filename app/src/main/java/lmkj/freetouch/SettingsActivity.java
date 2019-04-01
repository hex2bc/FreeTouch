package lmkj.freetouch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lmkj.freetouch.function.BluetoothButton;
import lmkj.freetouch.function.CommonButton;
import lmkj.freetouch.function.FlashlightButton;
import lmkj.freetouch.function.WifiButton;
import lmkj.freetouch.local.SaveObjectDataSource;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = "SettingsActivity";
    private int mIndex = -1;
    ArrayList<DiskButtonInfo> infos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            mIndex = getIntent().getIntExtra("index", -1);
        }
        setupActionBar();
    }

    private void initDiskInfo() {
        infos.add(new CommonButton(-1, R.drawable.ic_camera, "Camera", true));
        infos.add(new CommonButton(-1, R.drawable.ic_home, "Home", true));
        infos.add(new CommonButton(-1, R.drawable.ic_calculator, "Calculator", true));
        infos.add(new CommonButton(-1, R.drawable.ic_lock, "Lock", true));
        infos.add(new CommonButton(-1, R.drawable.ic_setting, "Setting", true));
        infos.add(new CommonButton(-1, R.drawable.ic_ringling, "Ringling", true));
        infos.add(new CommonButton(-1, R.drawable.ic_hotspot_off, "Hot spot", true));
        infos.add(new CommonButton(-1, R.drawable.ic_back, "Back", true));
        infos.add(new CommonButton(-1, R.drawable.ic_home, "Home", true));
        infos.add(new CommonButton(-1, R.drawable.ic_file, "File Manager", true));
        infos.add(new CommonButton(-1, R.drawable.ic_network_on, "Network", true));
        infos.add(new CommonButton(-1, R.drawable.ic_rotation, "Rotation", true));
        infos.add(new WifiButton(-1, true));
        infos.add(new BluetoothButton(-1, true));
        infos.add(new FlashlightButton(-1,true));
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        Header cate = new Header();
        cate.title = "Function";
        target.add(cate);
        initDiskInfo();
        for (DiskButtonInfo info : infos) {
            Header header = new Header();
            header.iconRes = info.drawableId;
            header.title = info.text;
            Bundle bundle = new Bundle();
            bundle.putSerializable("class", info);
            header.extras = bundle;
            target.add(header);
        }
//        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (mIndex >= 0) {
            SaveObjectDataSource db = SaveObjectDataSource.getInstance(getApplicationContext());
            DiskButtonInfo replace = (DiskButtonInfo) header.extras.getSerializable("class");
            DiskButtonInfo old = db.getDisk(Integer.toString(mIndex));

            replace.index = mIndex;
            replace.isFront = old.isFront;

            db.refreshDisk(replace);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        Intent service = new Intent().setClass(this, FloatButtonService.class);
        service.putExtra("FromSettings", true);
        startService(service);
        super.onDestroy();
    }
}
