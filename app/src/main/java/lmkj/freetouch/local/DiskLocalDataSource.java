package lmkj.freetouch.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;
import lmkj.freetouch.function.BluetoothButton;
import lmkj.freetouch.function.CommonButton;
import lmkj.freetouch.function.WifiButton;
import lmkj.freetouch.local.DiskDbHelper.DiskEntry;

/**
 * Concrete implementation of a data source as a db.
 */
public class DiskLocalDataSource implements DiskDataSource {

    private static DiskLocalDataSource INSTANCE;

    private DiskDbHelper mDbHelper;

    // Prevent direct instantiation.
    private DiskLocalDataSource(Context context) {
        mDbHelper = new DiskDbHelper(context);
    }

    public static DiskLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DiskLocalDataSource(context);
        }
        return INSTANCE;
    }

    public void initData() {
        saveDisk(new CommonButton(0, R.drawable.ic_camera, "Camera", true));
        DiskButtonInfo mMore = new CommonButton(1, R.drawable.ic_more, "More", true);
        mMore.canDelete = false;
        saveDisk(mMore);
        saveDisk(new CommonButton(2, R.drawable.ic_home, "Home", true));
        saveDisk(new CommonButton(3, R.drawable.ic_calculator, "Calculator", true));
        saveDisk(new CommonButton(4, R.drawable.ic_light_off, "Light", true));
        saveDisk(new CommonButton(5, R.drawable.ic_lock, "Lock", true));
        saveDisk(new CommonButton(6, R.drawable.ic_setting, "Setting", true));
        saveDisk(new CommonButton(7, R.drawable.ic_ringling, "Ringling", true));
        saveDisk(new CommonButton(10, R.drawable.ic_hotspot_off, "Hot spot", false));
        saveDisk(new CommonButton(11, R.drawable.ic_back, "Back", false));
        saveDisk(new CommonButton(12, R.drawable.ic_home, "Home", false));
        saveDisk(new CommonButton(13, R.drawable.ic_file, "File Manager", false));
        saveDisk(new CommonButton(14, R.drawable.ic_network_on, "Network", false));
        saveDisk(new CommonButton(15, R.drawable.ic_rotation, "Rotation", false));
        saveDisk(new BluetoothButton(16, false));
        saveDisk(new WifiButton(17, false));
    }

    @Override
    public ArrayList<DiskButtonInfo> getDisk() {
        if (isEmpty()) {
            initData();
        }

        ArrayList<DiskButtonInfo> disk = new ArrayList<DiskButtonInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DiskEntry.COLUMN_NAME_ENTRY_ID,
                DiskEntry.COLUMN_NAME_TITLE,
                DiskEntry.COLUMN_NAME_DRAWABLE_ID,
                DiskEntry.COLUMN_NAME_FRONT,
                DiskEntry.COLUMN_NAME_REMOVE,
                DiskEntry.COLUMN_NAME_CAN_DELETE
        };

        Cursor c = db.query(
                DiskEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_TITLE));
                int drawable = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_DRAWABLE_ID));
                boolean front = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_FRONT)) == 1;
                boolean remove = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_REMOVE)) == 1;
                boolean canDelete = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_CAN_DELETE)) == 1;
                CommonButton btn = new CommonButton(Integer.parseInt(itemId), drawable, title, front, remove, canDelete);
                disk.add(btn);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return disk;
    }

    @Override
    public DiskButtonInfo getDisk(String diskId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DiskEntry.COLUMN_NAME_ENTRY_ID,
                DiskEntry.COLUMN_NAME_TITLE,
                DiskEntry.COLUMN_NAME_DRAWABLE_ID,
                DiskEntry.COLUMN_NAME_FRONT,
                DiskEntry.COLUMN_NAME_REMOVE,
                DiskEntry.COLUMN_NAME_CAN_DELETE
        };

        String selection = DiskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {diskId};

        Cursor c = db.query(
                DiskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        DiskButtonInfo disk = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_TITLE));
            int drawable =
                    c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_DRAWABLE_ID));
            boolean front = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_FRONT)) == 1;
            boolean remove = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_REMOVE)) == 1;
            boolean canDelete = c.getInt(c.getColumnIndexOrThrow(DiskEntry.COLUMN_NAME_CAN_DELETE)) == 1;
            disk = new CommonButton(Integer.parseInt(itemId), drawable, title, front, remove, canDelete);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return disk;
    }

    @Override
    public void saveDisk(DiskButtonInfo disk) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DiskEntry.COLUMN_NAME_ENTRY_ID, Integer.toString(disk.index));
        values.put(DiskEntry.COLUMN_NAME_TITLE, disk.text);
        values.put(DiskEntry.COLUMN_NAME_DRAWABLE_ID, disk.drawableId);
        values.put(DiskEntry.COLUMN_NAME_FRONT, disk.isFront);
        values.put(DiskEntry.COLUMN_NAME_CAN_DELETE, disk.canDelete);
        values.put(DiskEntry.COLUMN_NAME_REMOVE, disk.isRemove);

        db.insert(DiskEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void refreshDisk(DiskButtonInfo disk) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DiskEntry.COLUMN_NAME_TITLE, disk.text);
        values.put(DiskEntry.COLUMN_NAME_DRAWABLE_ID, disk.drawableId);
        values.put(DiskEntry.COLUMN_NAME_FRONT, disk.isFront);
        values.put(DiskEntry.COLUMN_NAME_CAN_DELETE, disk.canDelete);
        values.put(DiskEntry.COLUMN_NAME_REMOVE, disk.isRemove);

        String selection = DiskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(disk.index)};

        db.update(DiskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteAllDisk() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(DiskEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteDisk(String diskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = DiskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {diskId};

        db.delete(DiskEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public boolean isEmpty() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + DiskEntry.TABLE_NAME, null);

        if (c != null && c.getCount() > 0) {
            c.close();
            db.close();
            return false;
        } else {
            if (c != null) c.close();
            db.close();
            return true;
        }
    }
}
