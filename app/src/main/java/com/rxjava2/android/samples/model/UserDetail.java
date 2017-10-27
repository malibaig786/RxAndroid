package com.rxjava2.android.samples.model;

/**
 * Created by amitshekhar on 04/02/17.
 */

public class UserDetail {

    public long id;
    public String firstname;
    public String lastname;

    public UserDetail(long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
