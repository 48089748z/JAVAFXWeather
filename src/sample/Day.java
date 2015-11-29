package sample;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by uRi on 27/11/2015.
 */
public class Day
{
    private String date;
    private String maxTemp;
    private String minTemp;
    private String humidity;
    private String pressure;
    private String description;
    private String windSpeed;
    private String imageCode;
    private Double avgTemp;

    public Day(String date, String maxTemp, String minTemp, String humidity, String pressure, String description, String windSpeed, String imageCode)
    {
        this.date=date;
        this.maxTemp=maxTemp;
        this.minTemp=minTemp;
        this.humidity=humidity;
        this.pressure=pressure;
        this.description=description;
        this.windSpeed=windSpeed;
        this.imageCode=imageCode;
    }
    public Day() {}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = new SimpleDateFormat("EEE dd/MM/yyyy").format(new Date(Long.parseLong(date)*1000)).toString();}
    public String getMaxTemp() {return maxTemp;}
    public void setMaxTemp(String maxTemp) {this.maxTemp = maxTemp;}
    public String getMinTemp() {return minTemp;}
    public void setMinTemp(String minTemp) {this.minTemp = minTemp;}
    public String getHumidity() {return humidity;}
    public void setHumidity(String humidity) {this.humidity = humidity;}
    public String getPressure() {return pressure;}
    public void setPressure(String pressure) {this.pressure = pressure;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getWindSpeed() {return windSpeed;}
    public void setWindSpeed(String windSpeed) {this.windSpeed = windSpeed;}
    public String getImageCode() {return imageCode;}
    public void setImageCode(String imageCode) {this.imageCode = imageCode;}
    public Double getAvgTemp() {return avgTemp;}
    public void setAvgTemp(){avgTemp = Double.parseDouble(changeComasToDots(new DecimalFormat("#.##").format(Double.parseDouble(minTemp) + Double.parseDouble(maxTemp)/2)));}
    public String toStringDetailed()
    {
        return "\nDay:               "+date+"\nTemp:           "+avgTemp+" Degrees\nMax Temp:    "+maxTemp+" Degrees\nMin Temp:     "+minTemp+" Degrees\nHumidity:       "+humidity+"%\nPressure:        "+pressure+" Pa\nDescription:    "+description+"\nWind Speed:    "+windSpeed+" mps";
    }
    public String toString()
    {
        return "\nDay:               "+date+"\nTemp:           "+avgTemp+" Degrees\nDescription:    "+description;
    }
    private String changeComasToDots(String stringWithComas) //Al fer el DecimalFormat em canvia els . per , i no puc fer un parseDouble, per aixo he creat aquest metode, per tornar les , a . i poder fer parseDouble
    {
        Character[] array = new Character[stringWithComas.length()];
        String result = "";
        for (int x=0; x<stringWithComas.length(); x++)
        {
            if (stringWithComas.charAt(x) == ',')
            {
                array[x] = '.';
            }
            else
            {
                array[x] = stringWithComas.charAt(x);
            }
        }
        for (int x=0; x<array.length; x++)
        {
            result = result + String.valueOf(array[x]);
        }
        return result;
    }
}
