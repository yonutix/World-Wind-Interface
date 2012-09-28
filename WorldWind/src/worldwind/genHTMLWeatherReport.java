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
/*
 * Listener for HTML report button - creates the HTML file
 */

public class genHTMLWeatherReport implements ActionListener {

    Vector<WeatherElements> elem;
    WWJ parent;

    public genHTMLWeatherReport(WWJ parent, Vector<WeatherElements> elem) {
        this.elem = elem;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JFileChooser fc = new JFileChooser(new File("."));
            int returnVal = fc.showSaveDialog(parent.ww);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    FileWriter f = new FileWriter("weather.html");
                    BufferedWriter out = new BufferedWriter(f);
                    out.write("<html>"
                            + "<head>"
                            + "<title>Weather report</title>"
                            + "<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>"
                            + "<script type=\"text/javascript\">"
                            + "google.load('visualization', '1.1', {packages: ['controls']});"
                            + "</script>"
                            + "<script type=\"text/javascript\">function drawVisualization() {"
                            + "var data = google.visualization.arrayToDataTable([");
                    out.write("['Day', 'Weather', 'Condition', 'Temperature', 'Rain', 'Wind', 'date', 'wind Speed', 'maxTemp', 'minTemp', 'rain'],");
                    for (int i = 0; i < elem.size(); i++) {

                        String weatherImg = "<img src=\"" + elem.elementAt(i).imgLink + "\" />";
                        String temp = "Max. temp.:<br />" + elem.elementAt(i).tempMax + "<br />Min. tem.:<br />" + elem.elementAt(i).tempMin;
                        String wind = ""
                                + "<img src=\"http://www.worldweatheronline.com/App_Themes/Default/images/wind/" + elem.elementAt(i).windDir + ".png\" /><br />"
                                + elem.elementAt(i).windSpeed + "Km/h<br />"
                                + elem.elementAt(i).windDir + "<br />("
                                + elem.elementAt(i).windDirDegree + ")";
                        out.write("['"
                                + elem.elementAt(i).date + "', '"
                                + weatherImg + "', '"
                                + elem.elementAt(i).weatherStatus + "', '"
                                + temp + "', '"
                                + elem.elementAt(i).rain + "', '"
                                + wind + "', '" + elem.elementAt(i).date + "', "
                                + elem.elementAt(i).windSpeed + ", "
                                + elem.elementAt(i).tempMax + ", "
                                + elem.elementAt(i).tempMin + ", "
                                + elem.elementAt(i).rain + "]");
                        if (i != elem.size() - 1) {
                            out.write(",\n");
                        }
                    }
                    out.write("]);");
                    out.write("var latitude = new google.visualization.ControlWrapper({\n"
                            + "'controlType': 'NumberRangeFilter',\n"
                            + "'containerId': 'latitude',\n"
                            + "'options': {\n"
                            + "  'filterColumnLabel': 'wind Speed',\n"
                            + "'ui': {'labelStacking': 'vertical'}\n"
                            + "}\n"
                            + "});\n"
                            + "var longitude = new google.visualization.ControlWrapper({\n"
                            + "'controlType': 'NumberRangeFilter',\n"
                            + "'containerId': 'longitude',\n"
                            + "'options': {\n"
                            + "  'filterColumnLabel': 'maxTemp',\n"
                            + "'ui': {'labelStacking': 'vertical'}\n"
                            + "}\n"
                            + "});\n"
                            + "var elevation = new google.visualization.ControlWrapper({\n"
                            + "'controlType': 'NumberRangeFilter',\n"
                            + "'containerId': 'elevation',\n"
                            + "'options': {\n"
                            + "  'filterColumnLabel': 'rain',\n"
                            + "'ui': {'labelStacking': 'vertical'}\n"
                            + "}\n"
                            + "});\n"
                            + "var table = new google.visualization.ChartWrapper({\n"
                            + "'chartType': 'Table',\n"
                            + "'containerId': 'chart3',\n"
                            + "'view': {'columns': [0, 1, 2, 3, 4, 5]},\n"
                            + "'options': {\n"
                            + "       allowHtml: true,\n"
                            + "       'dataTable' : data,\n"
                            + "'width': '800px'\n"
                            + "}});\n");

                    out.write("  var categoryPicker = new google.visualization.ControlWrapper({\n"
                            + "'controlType': 'CategoryFilter',\n"
                            + "'containerId': 'control2',\n"
                            + "'options': {\n"
                            + " 'filterColumnLabel': 'date',\n"
                            + "'ui': {\n"
                            + "'labelStacking': 'vertical',\n"
                            + " 'allowTyping': false,\n"
                            + " 'allowMultiple': false\n"
                            + " }\n"
                            + "}\n"
                            + "});\n");

                    out.write("var barChart = new google.visualization.ChartWrapper({\n"
                            + "'chartType': 'BarChart',\n"
                            + "'containerId': 'chart1',\n"
                            + "'options': {\n"
                            + " 'width': 400,\n"
                            + " 'height': 300,\n"
                            + "  'chartArea': {top: 0, right: 0, bottom: 0}\n"
                            + " },\n"
                            + "'view': {'columns': [6, 8, 9]}\n"
                            + "});\n");

                    out.write("myView = new google.visualization.DataView(data);\n"
                            + "new google.visualization.Dashboard(document.getElementById('dashboard')).bind([latitude, longitude, elevation, categoryPicker], [table]).bind(categoryPicker, barChart).draw(myView);\n"
                            + "}\n"
                            + "google.setOnLoadCallback(drawVisualization);\n"
                            + "</script>\n"
                            + "<style type=\"text/css\">\n"
                            + "body{\n"
                            + "	background-attachment:fixed;\n"
                            + "	color:#900;\n"
                            + "}\n"
                            + "</style>\n"
                            + "</head>\n"
                            + "<body background=\"weatherTexture.jpg\">\n"
                            + "<center>\n"
                            + "<div id=\"dashboard\">\n"
                            + "<table width=\"200\" border=\"0\">\n"
                            + "  <tr>\n"
                            + "    <td><div id=\"latitude\"></div></td>\n"
                            + "    <td><div id=\"longitude\"></div></td>\n"
                            + "  </tr>\n"
                            + "  <tr>\n"
                            + "    <td><div id=\"elevation\"></div></td>\n"
                            + "    <td><div id=\"control2\"></div></td>\n"
                            + "  </tr>\n"
                            + "</table>\n"
                            + "<div id=\"control1\"></div>\n"
                            + "<BR>\n"
                            + "<div id=\"chart3\"></div>\n"
                            + "<div id = \"chart1\"></div>\n"
                            + "</div>\n"
                            + "</center>\n"
                            + "</body>\n"
                            + "</html>");
                    out.close();
                    f.close();
                } catch (Exception exp) {
                    parent.standardDialogBox("Output exception", "Error saving file");
                }

            }
        } catch (Exception ex) {
            parent.standardDialogBox("Output exception", "Error writing file");
        }
    }
}