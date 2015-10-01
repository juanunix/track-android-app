package developer.sigmamovil.sigmatrackalpha1.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by william.montiel on 25/09/2015.
 */
public class User implements Serializable {
    String idUser;
    String name;
    String lastName;

    public User(String idUser, String name, String lastName) {
        this.idUser = idUser;
        this.name = name;
        this.lastName = lastName;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
