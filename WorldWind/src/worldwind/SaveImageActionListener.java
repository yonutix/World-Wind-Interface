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
     * listener for saving button 
     */

    class SaveImageActionListener implements ActionListener {

        BufferedImage img;
        WWJ parent;
        public SaveImageActionListener(WWJ parent, BufferedImage img) {
            this.img = img;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser(new File("."));
            int returnVal = fc.showSaveDialog(parent.ww);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(img, "bmp", new File(fc.getSelectedFile().getPath()));
                } catch (IOException ex) {
                    parent.standardDialogBox("Output exception", "Error trying writing file");
                }
            }
        }
    }