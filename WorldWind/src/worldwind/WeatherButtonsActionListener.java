/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package worldwind;

import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.poi.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import java.beans.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.measure.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.awt.print.*;

public class WeatherButtonsActionListener implements ActionListener {

    JButton prev, next;
    JLabel weatherIcon;
    JLabel dateout;
    JLabel weatherstatus;
    JLabel temperature;
    JLabel rain;
    JLabel wind;
    Vector<WeatherElements> elem;
    JTextField pgNum;

    public WeatherButtonsActionListener(JButton prev,
            JButton next,
            JLabel weatherIcon,
            JLabel dateout,
            JLabel weatherstatus,
            JLabel temperature,
            JLabel rain,
            JLabel wind,
            Vector<WeatherElements> elem,
            JTextField pgNum) {
        this.prev = prev;
        this.next = next;
        this.weatherIcon = weatherIcon;
        this.dateout = dateout;
        this.weatherstatus = weatherstatus;
        this.temperature = temperature;
        this.rain = rain;
        this.wind = wind;
        this.elem = elem;
        this.pgNum = pgNum;
    }

    public void actionPerformed(ActionEvent e) {
        Integer tc = Integer.parseInt(pgNum.getText());
        int i = tc.intValue();
        if (((JButton) e.getSource()).getName().compareTo("prev") == 0) {
            i--;
            if (i == 1) {
                prev.setEnabled(false);
            } else {
                prev.setEnabled(true);
            }
            pgNum.setText("" + (i));
            next.setEnabled(true);
            i--;
        }
        if (((JButton) e.getSource()).getName().compareTo("next") == 0) {

            if (i + 1 == elem.size()) {
                next.setEnabled(false);
            } else {
                next.setEnabled(true);
            }
            pgNum.setText("" + (i + 1));
            prev.setEnabled(true);
        }
        dateout.setText(elem.elementAt(i).date);
        weatherIcon.setText("<html><img src=\"" + elem.elementAt(i).imgLink + "\" /></html>");
        weatherstatus.setText("<html><h1>" + elem.elementAt(i).weatherStatus + "</h1></html>");
        temperature.setText("<html>Temperatures:<br />Temp min: " + elem.elementAt(i).tempMin + "°C<br />Temp max: " + elem.elementAt(i).tempMax + "°C</html>");
        rain.setText("Rain: " + elem.elementAt(i).rain + " mm");
        wind.setText("<html>Wind: <br />"
                + "<img src=\"http://www.worldweatheronline.com/App_Themes/Default/images/wind/" + elem.elementAt(i).windDir + ".png\" /><br />"
                + "Wind speed: " + elem.elementAt(i).windSpeed + "Km/h<br />"
                + elem.elementAt(i).windDir + "(" + elem.elementAt(i).windDirDegree + "°)</html>");
    }
}