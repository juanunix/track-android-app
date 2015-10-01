package developer.sigmamovil.sigmatrackalpha1.io.des_serializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.Login;
import developer.sigmamovil.sigmatrackalpha1.io.model.LoginResponse;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class LoginDeserializer implements JsonDeserializer<LoginResponse> {
    @Override
    public LoginResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.d("TAG", "GSON!!");
        Gson gson = new Gson();

        LoginResponse response = gson.fromJson(json, LoginResponse.class);

        JsonObject loginResponseData = json.getAsJsonObject();
        JsonArray loginArray = loginResponseData.getAsJsonArray("login");

        //response.setlogin(extractLoginFromJsonArray(loginArray));

        return response;
    }

    private ArrayList<Login> extractLoginFromJsonArray(JsonArray array) {
        ArrayList<Login> login = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonObject loginData = array.get(i).getAsJsonObject();

            int status = loginData.get("status").getAsInt();

            Login currentClient = new Login(status);
            login.add(currentClient);
        }

        Log.d("TAG", "HALO!!!!!!!");

        return login;
    }
}
