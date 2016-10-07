package cn.edu.bigc.pettrack.Event;

/**
 * Created by L T on 2016/9/27.
 */

public class PersonTypeEvent {
    public static final int FOLLOWER=0;
    public static final int FOLLOWEE=1;
    int personType;

    public PersonTypeEvent(int personType) {
        this.personType = personType;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }
}
