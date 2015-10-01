package developer.sigmamovil.sigmatrackalpha1.io.model;

import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.Client;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class ClientResponse {
    ArrayList<Client> result;

    public ArrayList<Client> getClients() {
        return result;
    }

    public void setClients(ArrayList<Client> clients) {
        this.result = clients;
    }
}
