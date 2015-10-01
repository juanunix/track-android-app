package developer.sigmamovil.sigmatrackalpha1.domain;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class Client {
    String idClient;
    String name;

    public Client(String idClient, String name) {
        this.idClient = idClient;
        this.name = name;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return idClient + " " + name;
    }
}
