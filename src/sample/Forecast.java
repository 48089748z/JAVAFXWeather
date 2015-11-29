package sample;

import java.util.ArrayList;

/**
 * Created by uRi on 27/11/2015.
 */
public class Forecast {
    private ArrayList<Day> days;
    private Location location;

    public Forecast(ArrayList<Day> days, Location location)
    {
        this.days = days;
        this.location = location;
    }
    public Forecast() {}
    public ArrayList<Day> getDays() {return days;}
    public void setDays(ArrayList<Day> days) {this.days = days;}
    public Location getLocation() {return location;}
    public void setLocation(Location location) {this.location = location;}
    public String toString() {return " "+location.getCity().toUpperCase()+"  "+ location.getCountry().toUpperCase();}
}