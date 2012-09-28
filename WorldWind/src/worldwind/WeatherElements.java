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
 * class for weather characteristics
 */

public class WeatherElements {

    String date, rain, tempMax, tempMin, weatherStatus, imgLink, windSpeed, windDirDegree, windDir;

    public WeatherElements(String date,
            String rain,
            String tempMax,
            String tempMin,
            String weatherStatus,
            String imgLink,
            String windDirDegree,
            String windDir,
            String windSpeed) {
        this.date = date;
        this.rain = rain;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherStatus = weatherStatus;
        this.imgLink = imgLink;
        this.windSpeed = windSpeed;
        this.windDirDegree = windDirDegree;
        this.windDir = windDir;
    }
}