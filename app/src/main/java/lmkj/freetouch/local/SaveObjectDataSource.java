package lmkj.freetouch.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import lmkj.freetouch.local.DiskDbHelper.DiskEntry;
import lmkj.freetouch.DiskButtonInfo;
import lmkj.freetouch.R;
import lmkj.freetouch.function.BluetoothButton;
import lmkj.freetouch.function.CommonButton;
import lmkj.freetouch.function.FlashlightButton;
import lmkj.freetouch.function.WifiButton;

public class SaveObjectDataSource implements DiskDataSource  {


    private static SaveObjectDataSource INSTANCE;

    private DiskDbHelper mDbHelper;

    // Prevent direct instantiation.
    private SaveObjectDataSource(Context context) {
        mDbHelper = new DiskDbHelper(context);
    }

    public static SaveObjectDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SaveObjectDataSource(context);
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
        saveDisk(new FlashlightButton(4, true));
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

    public boolean isEmpty() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + DiskEntry.TABLE_CLASS_NAME, null);

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

    @Override
    public ArrayList<DiskButtonInfo> getDisk() {
        if (isEmpty()) {
            initData();
        }
        ArrayList<DiskButtonInfo> disk = new ArrayList<DiskButtonInfo>();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + DiskEntry.TABLE_CLASS_NAME, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex(DiskEntry.COLUMN_NAME_CLASS_DATA));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    DiskButtonInfo info = (DiskButtonInfo) inputStream.readObject();
                    disk.add(info);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return disk;
    }

    @Override
    public DiskButtonInfo getDisk(String diskId) {

        DiskButtonInfo disk = null;
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + DiskEntry.TABLE_CLASS_NAME, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex(DiskEntry.COLUMN_NAME_CLASS_DATA));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    disk = (DiskButtonInfo) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return disk;
    }

    @Override
    public void saveDisk(DiskButtonInfo disk) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(disk);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            database.execSQL("insert into " + DiskEntry.TABLE_CLASS_NAME + " ("
                    + DiskEntry.COLUMN_NAME_ENTRY_ID + ","
                    + DiskEntry.COLUMN_NAME_CLASS_DATA + ") values(?,?)",
                    new Object[] {Integer.toString(disk.index), data});
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshDisk(DiskButtonInfo disk) {

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(disk);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            database.execSQL("UPDATE "+DiskEntry.TABLE_CLASS_NAME +" SET "
                            + DiskEntry.COLUMN_NAME_CLASS_DATA+"=? WHERE entryid="
                            + Integer.toString(disk.index), new Object[] {data});
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAllDisk() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(DiskEntry.TABLE_CLASS_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteDisk(String diskId) {

    }
}
