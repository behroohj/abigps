package gpstracker.developers.abi.com.abigps;

/**
 * Created by imanbahmani on 11/2/2016 AD.
 */

 import android.location.Location;
 import android.location.LocationListener;
 import android.os.Bundle;

public class gps implements LocationListener {


    public static double lat;
    public static double lon;

    public void onLocationChanged(Location loc) {
        // TODO Auto-generated method stub

        lat=loc.getLatitude();
        lon=loc.getLongitude();
    }

    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
    }

}