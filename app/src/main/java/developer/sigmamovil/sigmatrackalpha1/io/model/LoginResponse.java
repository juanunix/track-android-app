package developer.sigmamovil.sigmatrackalpha1.io.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.Login;

/**
 * Created by william.montiel on 24/09/2015.
 */
public class LoginResponse {
    @SerializedName("response")
    LoginResult result;

    public ArrayList<Login> getLogin() {
        return result.login;
    }

    private class LoginResult {
        ArrayList<Login> login;
    }
}
