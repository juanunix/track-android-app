package developer.sigmamovil.sigmatrackalpha1.io.model;

import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitHistory;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class VisitHistoryResponse {
    ArrayList<VisitHistory> result;

    public ArrayList<VisitHistory> getHistory() {
        return result;
    }

    public void setHistory(ArrayList<VisitHistory> history) {
        this.result = history;
    }
}