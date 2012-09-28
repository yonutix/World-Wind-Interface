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
 * Listener for deleting section
 * @vb  - panel to remove from
 * @viaTextField - text field to be removed
 * @removeTextFieldButton - button to be removed
 * @jcb hidden or visible combo box to be removed
 */

public class DeleteTextFieldActionListener implements ActionListener {

    JPanel vb;
    JTextField viaTextFields;
    JButton removeTextFieldButton;
    JComboBox jcb;
    WWJ parent;

    public DeleteTextFieldActionListener(WWJ parent) {
        this.parent = parent;
    }

    public DeleteTextFieldActionListener(JPanel vb, JTextField viaTextFields, JButton removeTextFieldButton, JComboBox jcb) {
        this.vb = vb;
        this.viaTextFields = viaTextFields;
        this.removeTextFieldButton = removeTextFieldButton;
        this.jcb = jcb;
    }

    public void actionPerformed(ActionEvent e) {
        vb.remove(viaTextFields);
        vb.remove(removeTextFieldButton);
        vb.remove(jcb);
        parent.pack();
    }
}