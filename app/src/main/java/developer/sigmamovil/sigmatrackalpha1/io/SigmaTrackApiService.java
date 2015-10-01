package developer.sigmamovil.sigmatrackalpha1.io;

import retrofit.http.GET;
import developer.sigmamovil.sigmatrackalpha1.io.model.VisitHistoryResponse;
import retrofit.http.Path;

/**
 * Created by william.montiel on 14/09/2015.
 */
public interface SigmaTrackApiService {
    @GET(ApiConstants.URL_VISIT_HISTORY + "/{idUser}")
    void getVisitHistory(@Path("idUser") String iduser, retrofit.Callback<VisitHistoryResponse> serverResponse);
}
