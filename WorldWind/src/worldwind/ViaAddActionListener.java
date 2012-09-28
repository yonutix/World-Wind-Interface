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
     * Action listener for "Via" buttons
     * @vb - panel to add items in
     */

    class ViaAddActionListener implements ActionListener {
        WWJ parent;
        JPanel vb;

        public ViaAddActionListener(WWJ parent, JPanel vb) {
            this.vb = vb;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            JTextField jtf = new JTextField("via", 15);
            vb.add(jtf);
            JComboBox jcb = new JComboBox();
            jcb.addActionListener(new ComboBoxMeasureActionLsitener(jtf));
            jcb.setVisible(false);
            JButton del = new JButton("Delete");
            del.addActionListener(new DeleteTextFieldActionListener(vb, jtf, del, jcb));
            vb.add(del);
            vb.add(jcb);
            parent.pack();
        }
    }