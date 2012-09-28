package worldwind;

/*
 * \class EarthquakeContentElement Characteristics for an earthquake
 */
class EarthquakeContentElement {

    String title, 
            summary, 
            position, 
            magnitude, 
            details, 
            image;

    public EarthquakeContentElement(String title,
            String image,
            String summary,
            Double lat,
            Double lon,
            Double elevation,
            Double magnitude,
            String Details) {
        this.title = title;
        this.image = image;
        this.summary = summary;
        this.position = "<html><p>"+ 
                lat.toString()+ 
                "</p><p>"+ 
                lon.toString()+ 
                "</p><p>"+ 
                elevation.toString()+ 
                "</p></html>";
        this.magnitude = magnitude.toString();
        this.details = Details;
    }
}

/*
 * \class EarthquakeAttributes for mathematic earthquake attributes
 */
class EarthquakeAttributes {

    Double longitude, latitude, elevation, magnitude;

    public EarthquakeAttributes(Double longitude, 
            Double latitude, 
            Double elevation, 
            Double magnitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.magnitude = magnitude;
    }
}