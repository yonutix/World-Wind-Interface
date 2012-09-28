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
 * Listener for a combo box wich have a link with a text field
 * @input text field to make change in
 */

class ComboBoxMeasureActionLsitener implements ActionListener {

    JTextField input;

    public ComboBoxMeasureActionLsitener(JTextField input) {
        this.input = input;
    }

    public void actionPerformed(ActionEvent event) {
        JComboBox cb = (JComboBox) event.getSource();
        if (cb.getSelectedItem() != null) {
            input.setText(cb.getSelectedItem().toString());
        }
    }
}