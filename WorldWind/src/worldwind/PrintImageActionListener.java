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
     * Listener for print button
     * @imgb - image to be printed
     */

    class PrintImageActionListener implements ActionListener {

        BufferedImage imgb;
        WWJ parent;

        public PrintImageActionListener(WWJ parent, BufferedImage imgb) {
            this.imgb = imgb;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            final Image img = new ImageIcon(imgb).getImage();
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(new Printable() {

                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex != 0) {
                        return NO_SUCH_PAGE;
                    }
                    graphics.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
                    return PAGE_EXISTS;
                }
            });
            if (printJob.printDialog()) {
                try {
                    printJob.print();
                } catch (Exception prt) {
                    parent.standardDialogBox("Error", "Error printing the image");
                }
            }
        }
    }