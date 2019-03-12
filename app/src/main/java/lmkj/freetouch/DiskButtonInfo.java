package lmkj.freetouch;

public class DiskButtonInfo {
    int index;
    int drawableId;
    String text;
    boolean isFront;
    boolean isRemove = false;
    boolean canDelete = true;

    public DiskButtonInfo(int index, int drawableId, String text, boolean front) {
        this.index = index;
        this.drawableId = drawableId;
        this.text = text;
        this.isFront = front;
    }

    public void remove() {
        drawableId = R.drawable.ic_add;
        text = "";
        isRemove = true;
        canDelete = false;
    }
}
