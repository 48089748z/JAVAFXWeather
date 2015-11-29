package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.simple.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Controller
{
    private static Forecast forecast;
    private static Location location;
    private static ArrayList<Day> days = new ArrayList<>();
    private ObservableList<String> items = FXCollections.observableArrayList();
    public ListView mainList;
    public ListView detailsList;
    public ImageView image;
    public ImageView cityImage;
    public Text headerText;
    private String JSON_FULL_STRING;

    private static String JSON_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily/";
    private static String CITY = "?q=Barcelona2";
    private static String FORMAT = "&mode=json";
    private static String UNITS = "&units=metric";
    private static String NUMBER_OF_DAYS = "&cnt=7";
    private static String API_KEY = "&appid=f3b53a805bc9ec413f57d26fdc30de46";

    private String JSON_FULL_URL = "http://api.openweathermap.org/data/2.5/forecast/daily/?q=Barcelona&mode=json&units=metric&cnt=7&appid=f3b53a805bc9ec413f57d26fdc30de46";
    final String WEATHER_IMAGE_BASE_URL = "http://openweathermap.org/img/w/";
    final String CITY_IMAGE_BASE_PATH = "C:\\Users\\uRi\\IdeaProjects\\WeatherInterface\\src\\sample\\Images\\";

    public void initialize()
    {
        downloadNewInformation();
        showNewInformation();
        mainList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                Integer index = mainList.getSelectionModel().getSelectedIndex();
                String imageCode = days.get(index).getImageCode()+".png";
                items.clear();
                items.add(days.get(index).toStringDetailed());
                showDetailedInterface();
                detailsList.setItems(items);
                image.setImage(new Image(WEATHER_IMAGE_BASE_URL +imageCode));
            }
        });
        showMainInterface();
    }
    public void changeCity(ActionEvent actionEvent)
    {
        MenuItem selected = (MenuItem) actionEvent.getSource();
        if (selected.getText().contains("London")) CITY = "?q="+ selected.getText()+"3";
        else {CITY = "?q="+ selected.getText()+"2";}
        refresh();
    }
    public void changeDays(ActionEvent actionEvent)
    {
        MenuItem selected = (MenuItem) actionEvent.getSource();
        switch (selected.getText().charAt(1))
        {
            case ' ':
            {
                NUMBER_OF_DAYS = "&cnt="+selected.getText().charAt(0);
                break;
            }
            default:
            {
                NUMBER_OF_DAYS = "&cnt="+selected.getText().charAt(0)+selected.getText().charAt(1);
                break;
            }
        }
        refresh();
    }
    public void changeUnits(ActionEvent actionEvent)
    {
        MenuItem selected = (MenuItem) actionEvent.getSource();
        switch (selected.getText())
        {
            default:
            {
                UNITS = "&units=metric";
                break;
            }
            case "Celsius":
            {
                UNITS = "&units=metric";
                break;
            }
            case "Fahrenheit":
            {
                UNITS = "&units=imperial";
                break;
            }
        }
        refresh();
    }
    public void refresh()
    {
        downloadNewInformation();
        showNewInformation();
        showMainInterface();
    }
    public void averageTemp(ActionEvent actionEvent)
    {
        Double total = 0.0;
        for (Day day : days) {total = total + day.getAvgTemp();}
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(" AVERAGE TEMPERATURE NEXT "+days.size()+" DAYS");
        alert.setHeaderText(String.valueOf(total/days.size())+" Degrees average at "+forecast.getLocation().getCity());
        alert.setContentText("Click X or Accept, to close this Dialog");
        alert.showAndWait();
    }
    public void showNewInformation()
    {
        headerText.setText(forecast.toString());
        items.clear();
        for(int x=0; x<forecast.getDays().size(); x++)
        {
            items.add(forecast.getDays().get(x).toString());
        }
        mainList.setItems(items);
    }
    public void showMainInterface()
    {
        mainList.setVisible(true);
        detailsList.setVisible(false);
        image.setVisible(false);
        cityImage.setVisible(false);
    }
    public void showDetailedInterface()
    {
        mainList.setVisible(false);
        detailsList.setVisible(true);
        image.setVisible(true);
    }
    public void about(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("   About ...");
        alert.setHeaderText("This APP downloads the Weather Details and Images from Internet");
        alert.setContentText("Click X or Accept, to close this Dialog");
        alert.showAndWait();
    }
    public void back(ActionEvent actionEvent) {
        showMainInterface();
        showNewInformation();
    }
    public void cityDetails(ActionEvent actionEvent)
    {
        image.setVisible(false);
        showDetailedInterface();
        items.clear();
        items.add(location.toString());
        detailsList.setItems(items);
        cityImage.setVisible(true);
        cityImage.setImage(new Image(new File(CITY_IMAGE_BASE_PATH+ location.getCity()+".png").toURI().toString()));
    }
    public void refresh(ActionEvent actionEvent) {refresh();}
    public void close(ActionEvent actionEvent) {Platform.exit();}
    public void downloadNewInformation()
    {
        days.clear();
        JSON_FULL_URL = JSON_BASE_URL+CITY+FORMAT+UNITS+NUMBER_OF_DAYS+API_KEY;//Se actualiza el URL antes de cada descarga
        try {JSON_FULL_STRING = getJSON(JSON_FULL_URL);} catch (Exception one){}

        JSONObject fullJO = (JSONObject) JSONValue.parse(JSON_FULL_STRING);
        JSONObject cityJO = (JSONObject) JSONValue.parse(fullJO.get("city").toString());

        forecast = new Forecast();
        location = new Location();
        location.setCity(cityJO.get("name").toString());
        location.setCountry(cityJO.get("country").toString());
        location.setPopulation(cityJO.get("population").toString());
        JSONObject coordJO = (JSONObject) JSONValue.parse(cityJO.get("coord").toString());
        location.setLatitude(coordJO.get("lat").toString());
        location.setLongitude(coordJO.get("lon").toString());
        forecast.setLocation(location);

        JSONArray daysJA = (JSONArray) fullJO.get("list");
        for(int x=0; x<daysJA.size(); x++)
        {
            Day day = new Day();
            JSONObject dayJO = (JSONObject) JSONValue.parse(daysJA.get(x).toString());
            day.setDate(dayJO.get("dt").toString());
            day.setPressure(dayJO.get("pressure").toString());
            day.setHumidity(dayJO.get("humidity").toString());
            day.setWindSpeed(dayJO.get("speed").toString());

            JSONObject tempJO = (JSONObject) JSONValue.parse(dayJO.get("temp").toString());
            day.setMaxTemp(tempJO.get("max").toString());
            day.setMinTemp(tempJO.get("min").toString());

            JSONArray weatherJA = (JSONArray) JSONValue.parse(dayJO.get("weather").toString());
            JSONObject weatherJO = (JSONObject) JSONValue.parse(weatherJA.get(0).toString());
            day.setDescription(weatherJO.get("description").toString());
            day.setImageCode(weatherJO.get("icon").toString());

            day.setAvgTemp();
            days.add(day);
        }
        forecast.setDays(days);
    }
    public static String getJSON(String jsonURL) throws Exception //Metode per agafar el JSON en format String desde la URL d'Internet
    {
        StringBuilder result = new StringBuilder();
        URL url = new URL(jsonURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
        {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }
}
