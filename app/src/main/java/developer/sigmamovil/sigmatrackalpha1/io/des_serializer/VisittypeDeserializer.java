package developer.sigmamovil.sigmatrackalpha1.io.des_serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitType;

/**
 * Created by usuario on 29/09/2015.
 */
public class VisittypeDeserializer {
    public JSONObject object;
    public VisittypeDeserializer(JSONObject object) {
        this.object = object;
    }

    public ArrayList<VisitType> deserialize() {
        ArrayList<VisitType> visitTypesArray = new ArrayList<>();
        try {
            JSONArray responseArray = this.object.getJSONArray("visittypes");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) responseArray.get(i);
                VisitType visitType = new VisitType(jsonObject.getString("idVisittype"), jsonObject.getString("name"));
                visitTypesArray.add(visitType);
            }
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return visitTypesArray;
    }
}
