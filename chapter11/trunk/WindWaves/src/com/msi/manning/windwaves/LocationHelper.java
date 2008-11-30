package com.msi.manning.windwaves;

import java.text.DecimalFormat;

import android.location.Criteria;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class LocationHelper {

	public static final String CLASSTAG = LocationHelper.class.getSimpleName();

	public static final double MILLION = 1e6;

	private static final DecimalFormat DEC_FORMAT = new DecimalFormat("###.##");

	public static final GeoPoint GOLDEN_GATE = new GeoPoint(
			(int) (37.49 * MILLION), (int) (-122.49 * MILLION));

	// note GeoPoint stores lat/long as "integer numbers of microdegrees"
	// meaning int*1E6
	// parse Location into GeoPoint
	public static GeoPoint getGeoPoint(final Location loc) {
		int lat = (int) (loc.getLatitude() * MILLION);
		int lon = (int) (loc.getLongitude() * MILLION);
		return new GeoPoint(lat, lon);
	}

	// parse geoRssPoint into GeoPoint(<georss:point>36.835 -121.899</georss:point>)
	public static GeoPoint getGeoPoint(final String geoRssPoint) {
		Log.d(Constants.LOGTAG, CLASSTAG + " getGeoPoint - geoRssPoint - " + geoRssPoint);
		GeoPoint returnPoint = null;
		String gPoint = geoRssPoint.trim();
		if (gPoint.indexOf(" ") != -1) {
			String latString = gPoint.substring(0, gPoint.indexOf(" "));
			String lonString = gPoint.substring(gPoint.indexOf(" "), gPoint
					.length());
			double latd = Double.parseDouble(latString);
			double lond = Double.parseDouble(lonString);
			int lat = (int) (latd * MILLION);
			int lon = (int) (lond * MILLION);
			returnPoint = new GeoPoint(lat, lon);
		} 
		return returnPoint;
	}

	// parse double point(-127.50) into String (127.50W)
	public static String parsePoint(final double point, final boolean isLat) {
	    Log.d(Constants.LOGTAG, CLASSTAG + " parsePoint - point - " + point + " isLat - " + isLat);
		String result = DEC_FORMAT.format(point);
		if (result.indexOf("-") != -1) {
			result = result.substring(1, result.length());
		}
		// latitude is decimal expressed as +- 0-90
        // (South negative, North positive, from Equator)
		if (isLat) {
            if (point < 0) {
                result += "S";
            } else {
                result += "N";
            }
        }
		// longitude is decimal expressed as +- 0-180
        // (West negative, East positive, from Prime Meridian)
		else {
			if (point < 0) {
				result += "W";
			} else {
				result += "E";
			}
		}		
		Log.d(Constants.LOGTAG, CLASSTAG + " parsePoint result - " + result);
		return result;
	}
}