package com.msi.manning.windwaves.data;

import java.util.Date;

public class BuoyData {
    
    public Date date;
    public String dateString;
    public String title;
    public String description;
    public String location;
    public String windDirection;
    public String windSpeed;
    public String windGust;
    public String waveHeight;
    public String wavePeriod;
    public String atmoPressure;
    public String atmoPressureTendency;
    public String airTemp;
    public String waterTemp;
    public String link;
    public String geoRssPoint;
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BuoyData - " + title);
        sb.append("\ndate - " + date);
        sb.append("\ndateString - " + dateString);
        sb.append("\nlink - " + link);
        sb.append("\ngeoRssPoint - " + geoRssPoint);
        sb.append("\ndescription - " + description);
        sb.append("\nlocation - " + location);
        sb.append("\nwindDirection - " + windDirection);
        sb.append("\nwindSpeed - " + windSpeed);
        sb.append("\nwindGust - " + windGust);
        sb.append("\nwaveHeight - " + waveHeight);
        sb.append("\nwavePeriod - " + wavePeriod);
        sb.append("\natmoPressure - " + atmoPressure);
        sb.append("\natmoPressureTendency - " + atmoPressureTendency);
        sb.append("\nairTemp - " + airTemp);
        sb.append("\nwaterTemp - " + waterTemp);
        return sb.toString();
    }    
}