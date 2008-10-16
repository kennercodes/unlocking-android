package com.msi.manning.weather.service;

import android.location.Criteria;

public class LocationHelper {

    private static final String CLASSTAG = LocationHelper.class.getSimpleName();
    
    public static final Criteria PROVIDER_CRITERIA;
    
    static {
        PROVIDER_CRITERIA = new Criteria();
        PROVIDER_CRITERIA.setAccuracy(Criteria.NO_REQUIREMENT);
        PROVIDER_CRITERIA.setAltitudeRequired(false);
        PROVIDER_CRITERIA.setBearingRequired(false);
        PROVIDER_CRITERIA.setCostAllowed(false);
        PROVIDER_CRITERIA.setPowerRequirement(Criteria.NO_REQUIREMENT);
        PROVIDER_CRITERIA.setSpeedRequired(false);
    }
    
    
    
    
}