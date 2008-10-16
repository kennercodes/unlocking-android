package com.msi.manning.weather.data;

public class WeatherForecast {

    private String date;
    private String day;
    private int high;
    private int low;
    private WeatherCondition condition;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public int getHigh() {
        return high;
    }
    public void setHigh(int high) {
        this.high = high;
    }
    public int getLow() {
        return low;
    }
    public void setLow(int low) {
        this.low = low;
    }    
    public WeatherCondition getCondition() {
        return condition;
    }
    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }    
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WeatherForecast:");
        sb.append(" date-" + this.date);
        sb.append(" day-" + this.day);
        sb.append(" high-" + this.high);
        sb.append(" low-" + this.low);
        if (this.condition != null) {
            sb.append(" condition-" + this.condition.getDisplay());
        }
        return sb.toString();
    }
    
}