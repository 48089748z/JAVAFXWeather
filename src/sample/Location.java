package sample;

/**
 * Created by uRi on 28/11/2015.
 */
public class Location
{
    private String city;
    private String longitude;
    private String latitude;
    private String country;
    private String population;
    public Location(){
    }
    public Location(String city, String longitude, String latitude, String country, String population){
        this.city = city;
        this.longitude=longitude;
        this.latitude=latitude;
        this.country=country;
        this.population=population;
    }
    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}
    public String getLongitude() {return longitude;}
    public void setLongitude(String longitude) {this.longitude = longitude;}
    public String getLatitude() {return latitude;}
    public void setLatitude(String latitude) {this.latitude = latitude;}
    public String getCountry(){return country;}
    public void setCountry(String country) {this.country = country;}
    public String getPopulation() {return population;}
    public void setPopulation(String population) {this.population = population;}
    public String toString()
    {
        return "\n"+ city +"\nCountry:   "+country+"\nLongitude:    "+longitude+"\nLatitude:    "+latitude;
    }
}
/*"city": {
    "id": 3648559,
    "city": "Barcelona",
    "coord": {
      "lon": -64.699997,
      "lat": 10.13333
    },*/
