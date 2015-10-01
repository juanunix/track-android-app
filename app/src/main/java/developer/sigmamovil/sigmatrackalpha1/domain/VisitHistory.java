package developer.sigmamovil.sigmatrackalpha1.domain;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class VisitHistory {
    String idVisit;
    String type;
    String client;
    String date;
    String elapsed;
    Double iLatitude;
    Double iLongitude;
    Double fLatitude;
    Double fLongitude;
    int finished;

    public VisitHistory(String idVisit, String type, String client, String date, String elapsed, Double iLatitude, Double iLongitude, Double fLatitude, Double fLongitude, int finished) {
        this.idVisit = idVisit;
        this.type = type;
        this.client = client;
        this.date = date;
        this.elapsed = elapsed;
        this.iLatitude = iLatitude;
        this.iLongitude = iLongitude;
        this.fLatitude = fLatitude;
        this.fLongitude = fLongitude;
        this.finished = finished;
    }

    public String getIdVisit() {
        return idVisit;
    }
    public String getType() {
        return type;
    }
    public String getClient() {
        return client;
    }
    public String getDate() {

        return date;
    }
    public String getElapsed() {

        return elapsed;
    }

    public double getIlatitude() {
        return iLatitude;
    }

    public double getIlongitude() {
        return iLongitude;
    }

    public double getFlatitude() {
        return fLatitude;
    }

    public double getFlongitude() {
        return fLongitude;
    }

    public int getFinished() {
        return finished;
    }
}
