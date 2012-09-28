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
     * Standard mouse listener
     */

    class StandardMouseListener implements MouseListener {
        WWJ parent;
        WorldWindowGLCanvas ww;
        
        public StandardMouseListener(WWJ parent, WorldWindowGLCanvas ww){
            this.parent = parent;
            this.ww = ww;
        }
//buttons images changing on mouse over

        public void mouseClicked(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.getName().compareTo("globe") == 0) {
                if (!parent.isFlatGlobe()) {
                    ww.getModel().setGlobe(new EarthFlat());
                    b.setIcon(new ImageIcon("images/earthOver.jpg"));
                } else {
                    ww.getModel().setGlobe(new Earth());
                    b.setIcon(new ImageIcon("images/flatearthOver.jpg"));
                }
            }
            if (b.getName().compareTo("boundaries") == 0) {
                if (parent.politicalBoundries.isEnabled()) {
                    parent.politicalBoundries.setEnabled(false);
                    b.setIcon(new ImageIcon("images/boundariesOver.jpg"));
                } else {
                    parent.politicalBoundries.setEnabled(true);
                    b.setIcon(new ImageIcon("images/deactivateBoundariesOver.jpg"));
                }
            }
            if (b.getName().compareTo("earthquake") == 0) {
                b.setIcon(new ImageIcon("images/earthquakeOver.jpg"));
                parent.eartquakeFrame();
            }
            if (b.getName().compareTo("weather") == 0) {
                b.setIcon(new ImageIcon("images/weatherOver.jpg"));
                parent.weatherFrame();
            }
            if (b.getName().compareTo("ss") == 0) {
                b.setIcon(new ImageIcon("images/screenshotOver.jpg"));
                parent.takeScreenshot();
            }
            if (b.getName().compareTo("open") == 0) {
                b.setIcon(new ImageIcon("images/openFIleOver.jpg"));
                parent.loadImage();
            }

        }

        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.getName().compareTo("globe") == 0) {
                if (!parent.isFlatGlobe()) {
                    b.setIcon(new ImageIcon("images/flatearthOver.jpg"));
                } else {
                    b.setIcon(new ImageIcon("images/earthOver.jpg"));
                }
                parent.tagContent("2D/3D Globe", "See 3D or 2D globe", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);
            }
            if (b.getName().compareTo("boundaries") == 0) {
                if (parent.politicalBoundries.isEnabled()) {
                    b.setIcon(new ImageIcon("images/deactivateBoundariesOver.jpg"));
                } else {
                    b.setIcon(new ImageIcon("images/boundariesOver.jpg"));
                }
                parent.tagContent("Political boundaries", "Turn on/off political boundaries", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);

            }
            if (b.getName().compareTo("earthquake") == 0) {
                b.setIcon(new ImageIcon("images/earthquakeOver.jpg"));
                parent.tagContent("Earthquakes", "See earthquakes reports", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);
            }
            if (b.getName().compareTo("weather") == 0) {
                b.setIcon(new ImageIcon("images/weatherOver.jpg"));
                parent.tagContent("Weather", "See weather Reports", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);
            }
            if (b.getName().compareTo("ss") == 0) {
                b.setIcon(new ImageIcon("images/screenshotOver.jpg"));
                parent.tagContent("Screenshot", "Take a screenshot", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);
            }
            if (b.getName().compareTo("open") == 0) {
                b.setIcon(new ImageIcon("images/openFIleOver.jpg"));
                parent.tagContent("Open image", "Open a specific image", b.getLocationOnScreen());
                parent.infoTag.setVisible(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.getName().compareTo("globe") == 0) {
                if (!parent.isFlatGlobe()) {
                    b.setIcon(new ImageIcon("images/flatearth.jpg"));
                } else {
                    b.setIcon(new ImageIcon("images/earth.jpg"));
                }
                parent.infoTag.setVisible(false);
            }

            if (b.getName().compareTo("boundaries") == 0) {
                if (parent.politicalBoundries.isEnabled()) {
                    b.setIcon(new ImageIcon("images/deactivateBoundaries.jpg"));
                } else {
                    b.setIcon(new ImageIcon("images/boundaries.jpg"));
                }
                parent.infoTag.setVisible(false);
            }
            if (b.getName().compareTo("earthquake") == 0) {
                b.setIcon(new ImageIcon("images/earthquake.jpg"));
                parent.infoTag.setVisible(false);
            }
            if (b.getName().compareTo("weather") == 0) {
                b.setIcon(new ImageIcon("images/weather.jpg"));
                parent.infoTag.setVisible(false);
            }
            if (b.getName().compareTo("ss") == 0) {
                b.setIcon(new ImageIcon("images/screenshot.png"));
                parent.infoTag.setVisible(false);
            }
            if (b.getName().compareTo("open") == 0) {
                b.setIcon(new ImageIcon("images/openFIle.jpg"));
                parent.infoTag.setVisible(false);
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }