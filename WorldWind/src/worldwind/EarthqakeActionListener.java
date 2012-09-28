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
 * Listener for creating the earthquakes report in JFrame
 */
class EarthqakeActionListener implements ActionListener {

    JTable table;
    JTextField jtf;
    Vector<EarthquakeContentElement> elem;
    JButton prev, next;

    public EarthqakeActionListener(JTable table, JTextField jtf, Vector<EarthquakeContentElement> elem, JButton prev, JButton next) {
        this.table = table;
        this.jtf = jtf;
        this.elem = elem;
        this.prev = prev;
        this.next = next;
    }

    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton) e.getSource();
        Integer i = Integer.parseInt(jtf.getText());
        i--;
        if (bt.getName().compareTo("next") == 0) {
            i++;
            table.setValueAt(elem.elementAt(i).title, 0, 0);
            table.setValueAt(elem.elementAt(i).image, 0, 1);
            table.setValueAt(elem.elementAt(i).summary, 0, 2);
            table.setValueAt(elem.elementAt(i).position, 0, 3);
            table.setValueAt(elem.elementAt(i).magnitude, 0, 4);
            table.setValueAt(elem.elementAt(i).details, 0, 5);
            jtf.setText("" + (i.intValue() + 1));
            if (i == elem.size() - 1) {
                bt.setEnabled(false);
            } else {
                prev.setEnabled(true);
            }
        }
        if (bt.getName().compareTo("prev") == 0) {
            i--;
            table.setValueAt(elem.elementAt(i).title, 0, 0);
            table.setValueAt(elem.elementAt(i).image, 0, 1);
            table.setValueAt(elem.elementAt(i).summary, 0, 2);
            table.setValueAt(elem.elementAt(i).position, 0, 3);
            table.setValueAt(elem.elementAt(i).magnitude, 0, 4);
            table.setValueAt(elem.elementAt(i).details, 0, 5);
            jtf.setText("" + (i.intValue() + 1));
            if (i == 0) {
                bt.setEnabled(false);
            } else {
                next.setEnabled(true);
            }
        }
    }
}