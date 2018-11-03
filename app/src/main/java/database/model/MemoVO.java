package database.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MemoVO extends RealmObject{
    @PrimaryKey
    private int no;
    private int order;
    private String memoText;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }
}
