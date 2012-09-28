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
     * listener for generating HTML reports
     */

    class GenHTMLActionListener implements ActionListener {

        Vector<EarthquakeContentElement> tbContent;
        Vector<EarthquakeAttributes> eqattr;
        WWJ parent;
        public GenHTMLActionListener(WWJ parent, Vector<EarthquakeContentElement> tbContent, Vector<EarthquakeAttributes> eqattr) {
            this.parent = parent;
            this.tbContent = tbContent;
            this.eqattr = eqattr;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fc = new JFileChooser(new File("."));
                int returnVal = fc.showSaveDialog(parent.ww);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter f = new FileWriter(fc.getSelectedFile());
                        BufferedWriter out = new BufferedWriter(f);
                        out.write("<html>\n");
                        out.write("<head>\n");
                        out.write("<title>");
                        out.write("Earthquakes report");
                        out.write("</title>\n");
                        out.write("<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n");
                        out.write("<script type=\"text/javascript\">\n");
                        out.write("google.load('visualization', '1.1', {packages: ['controls']});\n");
                        out.write("</script>\n");
                        out.write("<script type=\"text/javascript\">");
                        out.write("function drawVisualization() {\n"
                                + "var data = google.visualization.arrayToDataTable([\n"
                                + "['Title', 'Image', 'Summary', 'Position', 'Magnitude', 'Details', 'Latitude', 'Longitude', 'Elevation', 'Magnitude', 'Location']\n");
                        if (tbContent.size() != 0) {
                            out.write(",\n");
                        }
                        for (int i = 0; i < tbContent.size(); i++) {

                            String location = tbContent.elementAt(i).title.substring(tbContent.elementAt(i).title.indexOf(",") + 2, tbContent.elementAt(i).title.length());
                            out.write("['" + tbContent.elementAt(i).title + "', '" + tbContent.elementAt(i).image + "', '" + tbContent.elementAt(i).summary
                                    + "', '" + tbContent.elementAt(i).position + "', " + tbContent.elementAt(i).magnitude + ", '" + tbContent.elementAt(i).details + "'," + eqattr.elementAt(i).latitude + ", "
                                    + eqattr.elementAt(i).longitude + ", " + eqattr.elementAt(i).elevation + ", " + eqattr.elementAt(i).magnitude + ", '" + location + "']");
                            if (i + 1 != tbContent.size()) {
                                out.write(",\n");
                            }

                        }
                        out.write("]);\n");

                        out.write("var latitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'latitude',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Latitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var longitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'longitude',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Longitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var elevation = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'elevation',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Elevation',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var magnitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'magnitude',\n"
                                + "'options': {\n"
                                + " 'filterColumnLabel': 'Magnitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var stringFilter = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'StringFilter',\n"
                                + "'containerId': 'string_filter',\n"
                                + "'options': {\n"
                                + "'filterColumnLabel': 'Location'\n"
                                + "}\n"
                                + "});\n\n"
                                + "var table = new google.visualization.ChartWrapper({\n"
                                + "'chartType': 'Table',\n"
                                + "'containerId': 'chart3',\n"
                                + "'view': {'columns': [0, 1, 2, 3, 4, 5]},\n"
                                + "'options': {\n"
                                + "       allowHtml: true,\n"
                                + "       'dataTable' : data,\n"
                                + "'width': '1200px'\n"
                                + "}});\n\n");

                        out.write("myView = new google.visualization.DataView(data);"
                                + "new google.visualization.Dashboard(document.getElementById('dashboard')).bind([stringFilter, latitude, longitude, elevation, magnitude], [table]).draw(myView );\n"
                                + "}\n"
                                + "google.setOnLoadCallback(drawVisualization);\n"
                                + "</script>\n");

                        out.write("<style type=\"text/css\">\n"
                                + "body{\n"
                                + "	background-attachment:fixed;\n"
                                + "	color:#900;\n"
                                + "}\n"
                                + "</style>\n"
                                + "</head>\n"
                                + "<body background=\"weatherTexture.jpg\">\n"
                                + "<center>\n"
                                + "<div id=\"dashboard\">\n"
                                + "<div id=\"string_filter\"></div>\n"
                                + "<table width=\"200\" border=\"0\">\n"
                                + "  <tr>\n"
                                + "    <td><div id=\"latitude\"></div></td>\n"
                                + "    <td><div id=\"longitude\"></div></td>\n"
                                + "  </tr>\n"
                                + "  <tr>\n"
                                + "    <td><div id=\"elevation\"></div></td>\n"
                                + "    <td><div id=\"magnitude\"></div></td>\n"
                                + "  </tr>\n"
                                + "</table>\n"
                                + "<div id=\"control1\"></div>\n"
                                + "<div id=\"chart3\"></div>\n"
                                + "</div>\n"
                                + "</center>\n"
                                + "</body>\n"
                                + "</html>");
                        out.close();
                        f.close();
                    } catch (Exception exp) {
                        parent.standardDialogBox("Output exception", "Error trying saving file");
                    }
                }
            } catch (Exception ex) {
                parent.standardDialogBox("Output exception", "Error trying writing file");
            }
        }
    }