package developer.sigmamovil.sigmatrackalpha1.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import developer.sigmamovil.sigmatrackalpha1.io.model.VisitHistoryResponse;
import developer.sigmamovil.sigmatrackalpha1.io.des_serializer.VisitHistoryDeserializer;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class SigmaTrackApiAdapter {
    private static SigmaTrackApiService API_SERVICE;

    public static SigmaTrackApiService getApiServiceForVisitHistory() {
        if (API_SERVICE == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ApiConstants.URL_BASE)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setConverter(buildSigmaTrackApiVisitHistoryConverter())
                    .build();

            API_SERVICE = adapter.create(SigmaTrackApiService.class);
        }

        return API_SERVICE;
    }

    private static GsonConverter buildSigmaTrackApiVisitHistoryConverter() {
        Gson gsonConf = new GsonBuilder()
                .registerTypeAdapter(VisitHistoryResponse.class, new VisitHistoryDeserializer())
                .create();

        return new GsonConverter(gsonConf);
    }
}
