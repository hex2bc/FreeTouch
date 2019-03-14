package lmkj.freetouch;

public interface IDiskView {

    /**
    * 交换内外侧位置
    * */
    void showChanged(boolean change);

    /**
    * 编辑模式
    * */
    void showModifyMode(boolean show);

    /**
     * 更新布局
     * */
    void updateDiskView();
}
