package developer.sigmamovil.sigmatrackalpha1.domain;

/**
 * Created by william.montiel on 23/09/2015.
 */
public class VisitType {
    String idVisittype;
    String name;

    public VisitType(String idVisittype, String name) {
        this.idVisittype = idVisittype;
        this.name = name;
    }

    public String getIdVisittype() {
        return idVisittype;
    }

    public String getName() {
        return name;
    }
}
