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
     * Action lsitener for "zoom" button
     */

    class TextButtonActionListener implements ActionListener {

        JTextField txtF;
        JComboBox jcb;
        JTextArea jta;
        WWJ parent;

        TextButtonActionListener(WWJ parent, JTextField txtF, JComboBox jcb, JTextArea jta) {
            this.parent = parent;
            this.txtF = txtF;
            this.jcb = jcb;
            this.jta = jta;
        }

        public void actionPerformed(ActionEvent event) {
            jta.setVisible(false);
            jcb.removeAllItems();
            jcb.setVisible(false);


            java.util.List<PointOfInterest> list = new ArrayList<PointOfInterest>();
            String[] searchedText = txtF.getText().trim().split("[,]");
            ArrayList<String> wordsList = new ArrayList<String>();

            parent.gz = new YahooGazetteer();
            for (String s : searchedText) {
                if (s.trim().compareTo("") != 0) {
                    wordsList.add(s.trim());
                }
            }
            Iterator it = wordsList.iterator();
            if (wordsList.isEmpty()) {
                jta.setText("Input invalid");
                jta.setVisible(true);
                parent.mkr.clear();
            }
            if (wordsList.size() == 1) {
                list = parent.gz.findPlaces(searchedText[0]);
            }
            if (wordsList.size() == 2) {
                Matcher matcher = (Pattern.compile("[0-9]")).matcher(searchedText[1]);
                if (matcher.find()) {
                    searchedText[0] = searchedText[0].trim();
                    searchedText[1] = searchedText[1].trim();
                    list.add(parent.parseCoordinates(searchedText));
                } else {
                    list = parent.gz.findPlaces(searchedText[0].trim() + "+" + searchedText[1].trim());
                }
            }
            if (wordsList.size() > 2) {
                String buff = new String();
                while (it.hasNext()) {
                    buff = buff + (String) it.next() + "+";
                }
                buff = buff.substring(0, buff.length() - 1);
                list = parent.gz.findPlaces(buff);
            }
            jcb.setVisible(false);
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    Position poz = new Position(list.get(0).getLatlon(), 0);
                    parent.ww.getView().goTo(poz, 25e3);
                    parent.mkr.clear();
                    MarkerAttributes attr = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.SPHERE, 1d, 10, 5);
                    Marker mk = new BasicMarker(poz, attr);
                    mk.setPosition(poz);
                    parent.mkr.add(mk);
                    parent.Mlayer.setMarkers(parent.mkr);
                    parent.ww.redraw();
                } else {
                    for (PointOfInterest pt : list) {
                        jcb.addItem(pt);
                    }
                    jcb.setVisible(true);
                }
            } else {
                jta.setText("Locatie negasita");
                jta.setVisible(true);
            }
        }
    }