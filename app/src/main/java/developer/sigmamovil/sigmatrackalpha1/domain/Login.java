package developer.sigmamovil.sigmatrackalpha1.domain;

/**
 * Created by william.montiel on 24/09/2015.
 */
public class Login {
    int status;

    public Login(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
