package cn.edu.bigc.pettrack.Event;

/**
 * Created by L T on 2016/10/5.
 */

public class RefreshEvent {
    boolean isAdded;
    public RefreshEvent(boolean isAdded) {
        this.isAdded=isAdded;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
