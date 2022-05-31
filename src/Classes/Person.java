package Classes;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String userName;
    protected String email;
    protected String phoneNumber;

    public Person(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
