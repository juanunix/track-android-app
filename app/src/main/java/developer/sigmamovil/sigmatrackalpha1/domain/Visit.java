package developer.sigmamovil.sigmatrackalpha1.domain;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class Visit {
    int idAccount;
    int latitude;
    int longitude;
    int battery;
    String location;
    int new_client;

    public Visit(int idAccount, int latitude, int longitude, int battery, String location) {
        this.idAccount = idAccount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.battery = battery;
        this.location = location;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getBattery() {
        return battery;
    }

    public String getLocation() {
        return location;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
