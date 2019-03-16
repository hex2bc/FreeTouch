package lmkj.freetouch;

import android.content.Context;

import java.util.ArrayList;

public interface IDiskPresenter {
    ArrayList<DiskButtonInfo> getLayoutView();
    void addButtonClick(int index);
    void deleteButtonClick(DiskButtonInfo info);
    void moreButtonClick();
    void middleButtonClick();
    void longClick();
    void buttonClick(Context context, DiskButtonInfo info);
    boolean onBackPress();
}
