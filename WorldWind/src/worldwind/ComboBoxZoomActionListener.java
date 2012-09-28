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
     * Listener for "Zoom" combo box
     * @input - text field to be modified
     */

    class ComboBoxZoomActionListener implements ActionListener {

        JTextField input;
        WWJ parent;

        public ComboBoxZoomActionListener(WWJ parent, JTextField input) {
            this.input = input;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent event) {
            JComboBox cb = (JComboBox) event.getSource();
            if (cb.getSelectedItem() != null) {
                PointOfInterest selectedPoi = (PointOfInterest) cb.getSelectedItem();
                parent.mkr.clear();
                MarkerAttributes attr = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.SPHERE, 1d, 10, 5);
                Position poz = new Position(selectedPoi.getLatlon(), 0);
                Marker mk = new BasicMarker(poz, attr);
                mk.setPosition(poz);
                parent.mkr.add(mk);
                parent.Mlayer.setMarkers(parent.mkr);
                parent.ww.redraw();
                input.setText(cb.getSelectedItem().toString());
                parent.ww.redraw();
                parent.ww.getView().goTo(new Position(selectedPoi.getLatlon(), 0), 25e3);
            }
            cb.setVisible(false);
        }
    }