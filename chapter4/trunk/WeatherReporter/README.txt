WeatherReporter
================

Concept: 
---------
Weather information application, 
combined with location based weather alerting mechanism.
General idea, a hand-held type of weather radio that 
updates it's own current location. 
(And this was around before the two or three similar apps
now in the Android Developer Challenge ;).) 

Details: 
---------
Uses an online weather API (Yahoo! Weather) to get weather details
for the current device location using providers, AND allows user to 
specify other locations using postal code 
(enhancement use an optional MapView for specified location input, or other). 

Once a location is shown, with weather details, including forecast, 
the user also has the option to enable or disable alerts for that 
location (two location types, current device location - which updates
itself, and user specified locations). 

If user enables alerts the application will use a Notification to 
warn the user when severe weather is in either the current device 
location, or one of their selected saved (and alert enabled) locations.

*Note - in dev mode the alert polling threshold is very low (15 seconds).
The plan is to make this default to every 4-6 hours in production mode 
(and allow the user to change it if needed). 


Android Specifics:
-------------------
Written with two main goals in mind: first - be very useful to the user,
second - exercise many of the Android architectural aspects. 

Uses a background service to poll for weather conditions for alert enabled
locations. Service is started at boot via BroadcastReciever (and if not already 
running is also started when first Activity is invoked). 

Uses SQLite to store saved location information. 

Uses a custom scheme and authority as a registered Intent to allow other
applications to invoke it easily with a "weather://com.msi.manning?loc=ZIP" style URI. 





