package developer.sigmamovil.sigmatrackalpha1.io.des_serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.Client;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class ClientDeserializer  {
    public JSONObject object;
    public ClientDeserializer(JSONObject object) {
        this.object = object;
    }

    public ArrayList<Client> deserialize() {
        ArrayList<Client> clientArray = new ArrayList<>();
        try {
            JSONArray responseArray = this.object.getJSONArray("clients");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) responseArray.get(i);
                Client client = new Client(jsonObject.getString("idClient"), jsonObject.getString("name"));
                clientArray.add(client);
            }
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return clientArray;
    }
}
