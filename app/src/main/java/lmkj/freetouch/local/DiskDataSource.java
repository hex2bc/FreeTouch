package lmkj.freetouch.local;

import java.util.ArrayList;

import lmkj.freetouch.DiskButtonInfo;

public interface DiskDataSource {

    ArrayList<DiskButtonInfo> getDisk();

    DiskButtonInfo getDisk(String diskId);

    void saveDisk(DiskButtonInfo disk);

    void refreshDisk(DiskButtonInfo disk);

    void deleteAllDisk();

    void deleteDisk(String diskId);
}

