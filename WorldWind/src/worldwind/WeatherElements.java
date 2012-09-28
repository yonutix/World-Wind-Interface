package worldwind;

/*
 * \class WeatherElements for weather characteristics
 */
public class WeatherElements {

    String date, 
            rain, 
            tempMax,
            tempMin,
            weatherStatus,
            imgLink,
            windSpeed,
            windDirDegree,
            windDir;

    public WeatherElements(String date,
            String rain,
            String tempMax,
            String tempMin,
            String weatherStatus,
            String imgLink,
            String windDirDegree,
            String windDir,
            String windSpeed) {
        this.date = date;
        this.rain = rain;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherStatus = weatherStatus;
        this.imgLink = imgLink;
        this.windSpeed = windSpeed;
        this.windDirDegree = windDirDegree;
        this.windDir = windDir;
    }
}