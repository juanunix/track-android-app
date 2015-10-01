package developer.sigmamovil.sigmatrackalpha1.misc;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by usuario on 29/09/2015.
 */
public class GeocoderAdapter {

    public Location location;
    public Context context;
    public GeocoderAdapter(Context context, Location location) {
        this.location = location;
        this.context = context;
    }

    public String getLocation() {
        String loc = "No disponible";

        try {
            Geocoder gcd = new Geocoder(this.context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(this.location.getLatitude(), this.location.getLongitude(), 1);
            if (addresses.size() > 0) {
                loc =  getCompleteAddressString(addresses);
            }
        }
        catch (IOException ex) {
            Log.e("ERROR", "Unable connect to Geocoder", ex);
        }

        return loc;
    }

    private String getCompleteAddressString(List<Address> addresses) {
        String strAdd = "";
        try {
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
            }
            strAdd = strReturnedAddress.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
