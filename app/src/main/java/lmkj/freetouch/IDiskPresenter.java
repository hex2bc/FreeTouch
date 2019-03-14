package lmkj.freetouch;

import java.util.ArrayList;

public interface IDiskPresenter {
    ArrayList<DiskButtonInfo> getLayoutView();
    void addButtonClick(int index);
    void deleteButtonClick(DiskButtonInfo info);
    void moreButtonClick();
    void middleButtonClick();
    void longClick();
    boolean onBackPress();
}
