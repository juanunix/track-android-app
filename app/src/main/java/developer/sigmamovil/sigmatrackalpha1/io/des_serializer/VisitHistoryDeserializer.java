package developer.sigmamovil.sigmatrackalpha1.io.des_serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import developer.sigmamovil.sigmatrackalpha1.io.model.VisitHistoryResponse;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitHistory;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class VisitHistoryDeserializer implements JsonDeserializer<VisitHistoryResponse> {

    @Override
    public VisitHistoryResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        VisitHistoryResponse response = gson.fromJson(json, VisitHistoryResponse.class);

        JsonObject visitHistoryResponseData = json.getAsJsonObject();
        JsonArray historyArray = visitHistoryResponseData.getAsJsonArray("history");

        response.setHistory(extractHistoryFromJsonArray(historyArray));

        return response;
    }

    private ArrayList<VisitHistory> extractHistoryFromJsonArray(JsonArray array) {
        ArrayList<VisitHistory> history = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonObject historyData = array.get(i).getAsJsonObject();

            String idVisit = validateString("idVisit", historyData);

            if (idVisit != "") {
                String type = validateString("type", historyData);
                String client = validateString("client", historyData);
                String date = "";
                if (!historyData.get("end").isJsonNull()) {
                    date = historyData.get("start").getAsString() + " - " + historyData.get("end").getAsString();
                }

                String elapsed = validateString("elapsed", historyData);
                double fLatitude = validateDouble("fLatitude", historyData);
                double fLongitude = validateDouble("fLongitude", historyData);
                double iLatitude = validateDouble("iLatitude", historyData);
                double iLongitude = validateDouble("iLongitude", historyData);
                int finished = validateInt("finished", historyData);

                VisitHistory currentVisit = new VisitHistory(idVisit, type, client, date, elapsed, iLatitude, iLongitude, fLatitude, fLongitude, finished);

                history.add(currentVisit);
            }
        }

        return history;
    }

    private String validateString(String key, JsonObject jsonObject) {
        if (!jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "";
    }

    private double validateDouble(String key, JsonObject jsonObject) {
        if (!jsonObject.get(key).isJsonNull() && jsonObject.get(key).getAsString() != "") {
            return jsonObject.get(key).getAsDouble();
        }

        return 0;
    }

    private int validateInt(String key, JsonObject jsonObject) {
        if (!jsonObject.get(key).isJsonNull() && jsonObject.get(key).getAsString() != "") {
            return jsonObject.get(key).getAsInt();
        }

        return 1;
    }
}
