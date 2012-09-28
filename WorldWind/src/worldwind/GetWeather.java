package worldwind;

import gov.nasa.worldwind.poi.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.util.*;

/*
 * \class GetWeather Listener for getting weather in JFrame after the 
 * Submit button is pressed
 */
class GetWeather implements ActionListener {

    WWJ parent;
    JTextField loc;
    JSpinner daysnr;
    JLabel weatherIcon;
    JLabel dateout;
    JLabel weatherstatus;
    JLabel temperature;
    JLabel rain;
    JLabel wind;
    JPanel buttons;
    JPanel pageNum;

    public GetWeather(WWJ parent,
            JTextField loc,
            JSpinner daysnr,
            JLabel weatherIcon,
            JLabel dateout,
            JLabel weatherstatus,
            JLabel temperature,
            JLabel rain,
            JLabel wind,
            JPanel buttons,
            JPanel pageNum) {
        this.loc = loc;
        this.daysnr = daysnr;
        this.weatherIcon = weatherIcon;
        this.dateout = dateout;
        this.weatherstatus = weatherstatus;
        this.temperature = temperature;
        this.rain = rain;
        this.wind = wind;
        this.buttons = buttons;
        this.pageNum = pageNum;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        SpinnerNumberModel m = ((SpinnerNumberModel) daysnr.getModel());
        int days = m.getNumber().intValue();
        String location = ((JTextField) loc).getText();
        java.util.List<PointOfInterest> points = 
                parent.textFieldPoints(location);
        if (!points.isEmpty()) {
            double latitude = points.get(0).getLatlon().latitude.getDegrees(),
                    longitude = points.get(0).getLatlon().longitude.getDegrees();
            //we want only the firs two decimals
            latitude = ((double) ((long) (latitude * 100))) / 100;
            longitude = ((double) ((long) (latitude * 100))) / 100;
            String APIKey = "65ea00ff33143650113112";
            String address = 
                    "http://free.worldweatheronline.com/feed/weather.ashx?" +
                    "key="+
                    APIKey +
                    "&num_of_days=" +
                    days + "&q=" +
                    latitude +
                    "," +
                    longitude +
                    "&format=json&cc=no";
            try {
                URL link = new URL(address);
                URLConnection yc = link.openConnection();
                BufferedReader in = 
                        new BufferedReader(
                                new InputStreamReader(yc.getInputStream()));
                Vector<WeatherElements> elem = new Vector<WeatherElements>();
                String jsonFile = in.readLine();
                int i1 = 0, i2 = 0;
                for (int i = 0; i < days; i++) {
                    i1 = jsonFile.indexOf("\"date\"", i2) + 9;
                    i2 = jsonFile.indexOf("\"", i1);
                    String date = jsonFile.substring(i1, i2);
                    i2++;

                    i1 = jsonFile.indexOf("\"precipMM\"", i2) + 13;
                    i2 = jsonFile.indexOf("\"", i1);
                    String rain = jsonFile.substring(i1, i2);
                    i2++;

                    i1 = jsonFile.indexOf("\"tempMaxC\"", i2) + 13;
                    i2 = jsonFile.indexOf("\"", i1);
                    String tempMax = jsonFile.substring(i1, i2);
                    i2++;

                    i1 = jsonFile.indexOf("\"tempMinC\"", i2) + 13;
                    i2 = jsonFile.indexOf("\"", i1);
                    String tempMin = jsonFile.substring(i1, i2);
                    i2++;

                    i1 = jsonFile.indexOf("\"value\"", i2) + 10;
                    i2 = jsonFile.indexOf("\"", i1);
                    String weatherStatus = jsonFile.substring(i1, i2);
                    i2++;


                    i1 = jsonFile.indexOf("\"value\"", i2) + 10;
                    i2 = jsonFile.indexOf("\"", i1);
                    String imgLink = jsonFile.substring(i1, i2);
                    imgLink = imgLink.replace("\\", "");
                    i2++;


                    i1 = jsonFile.indexOf("\"winddirDegree\"", i2) + 18;
                    i2 = jsonFile.indexOf("\"", i1);
                    String windDirDegree = jsonFile.substring(i1, i2);
                    i2++;


                    i1 = jsonFile.indexOf("\"winddirection\"", i2) + 18;
                    i2 = jsonFile.indexOf("\"", i1);
                    String windDir = jsonFile.substring(i1, i2);
                    i2++;

                    i1 = jsonFile.indexOf("\"windspeedKmph\"", i2) + 18;
                    i2 = jsonFile.indexOf("\"", i1);
                    String windSpeed = jsonFile.substring(i1, i2);
                    i2++;

                    WeatherElements o = new WeatherElements(date, 
                            rain,
                            tempMax,
                            tempMin,
                            weatherStatus,
                            imgLink,
                            windDirDegree,
                            windDir,
                            windSpeed);
                    elem.add(o);

                }

                weatherIcon.setVisible(true);
                dateout.setText(elem.elementAt(0).date);
                weatherIcon.setText("<html><img src=\"" +
                        elem.elementAt(0).imgLink + 
                        "\" /></html>");
                weatherstatus.setText("<html><h1>" + 
                        elem.elementAt(0).weatherStatus + 
                        "</h1></html>");
                temperature.setText("<html>Temperatures:<br />Temp min: " +
                        elem.elementAt(0).tempMin + 
                        "°C<br />Temp max: " + 
                        elem.elementAt(0).tempMax + 
                        "°C</html>");
                rain.setText("Rain: " +
                        elem.elementAt(0).rain +
                        " mm");
                wind.setText("<html>Wind: <br />"
                        + "<img src=\"http://www.worldweatheronline.com"+
                        "/App_Themes/Default/images/wind/" + 
                        elem.elementAt(0).windDir +
                        ".png\" /><br />"
                        + "Wind speed: " +
                        elem.elementAt(0).windSpeed +
                        "Km/h<br />"
                        + elem.elementAt(0).windDir
                        + "(" + elem.elementAt(0).windDirDegree
                        + "°)</html>");

                buttons.removeAll();
                pageNum.removeAll();
                buttons.updateUI();
                pageNum.updateUI();

                JButton previous = new JButton("Previous");
                previous.setEnabled(false);
                previous.setName("prev");

                JButton next = new JButton("Next");
                next.setName("next");

                if (days == 1) {
                    next.setEnabled(false);
                }

                JTextField current = new JTextField("1", 3);
                current.setEditable(false);
                JTextField maxNum = new JTextField("" + elem.size(), 3);
                maxNum.setEditable(false);
                pageNum.add(current);
                pageNum.add(maxNum);

                previous.addActionListener(
                        new WeatherButtonsActionListener(previous, 
                                next,
                                weatherIcon,
                                dateout,
                                weatherstatus,
                                temperature,
                                rain,
                                wind,
                                elem,
                                current));
                next.addActionListener(
                        new WeatherButtonsActionListener(previous, 
                                next,
                                weatherIcon,
                                dateout,
                                weatherstatus,
                                temperature,
                                rain,
                                wind,
                                elem,
                                current));
                buttons.add(next);
                buttons.add(previous);
                JButton genHTML = new JButton("Generate HTML");
                genHTML.addActionListener(new genHTMLWeatherReport(parent, 
                        elem));
                buttons.add(genHTML);
            } catch (Exception ex) {
                parent.standardDialogBox("Fetching data error",
                        "Somethnig goes wrong with the connection");
            }
        } else {
            parent.standardDialogBox("Incorrect input", "Input is incorrect!");
        }
    }
}