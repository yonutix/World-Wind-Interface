package worldwind;

import javax.swing.*;
import java.awt.event.*;
import java.beans.*;
import java.awt.*;
import gov.nasa.worldwind.util.measure.*;
import gov.nasa.worldwind.awt.*;

/*
 * \class SurfaceMouseListener Mouse listener for 
 */
class SurfaceMouseListener implements MouseListener {
    
    /**
     * \param panel - surface response panel
     * \param field - field for write surface dimension
     */
    JPanel panel;
    JTextField field;
    MeasureTool measureTool;
    WorldWindowGLCanvas ww;
    WWJ parent;

    public SurfaceMouseListener(JPanel panel, 
            JTextField field, 
            MeasureTool measureTool, 
            WorldWindowGLCanvas ww, 
            WWJ parent) {
        this.panel = panel;
        this.field = field;
        this.measureTool = measureTool;
        this.ww = ww;
        this.parent = parent;
    }

    public void mouseClicked(MouseEvent e) {
        JButton bt = ((JButton) e.getSource());
        //if the tool is armed disarm it, else arm it
        if (measureTool.isArmed()) {
            measureTool.clear();
            measureTool.setArmed(false);
            ((Component) ww).setCursor(Cursor.getDefaultCursor());
            panel.setVisible(false);
            bt.setIcon(new ImageIcon("images/metalrulerOver.jpg"));
        } else {
            measureTool.setArmed(true);
            ((Component) ww).setCursor(
                    Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            panel.setVisible(true);
            //anonim listener
            measureTool.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent event) {
                    field.setText("" + (measureTool.getArea() / 1000000));
                }
            });
            bt.setIcon(new ImageIcon("images/stopMeasureOver.jpg"));
        }
        parent.tagContent("2D/3D Globe",
                "Select points and measure the surface described",
                bt.getLocationOnScreen());
        parent.infoTag.setVisible(true);
    }

    public void mouseEntered(MouseEvent e) {
        JButton b = (JButton) e.getSource();
        if (measureTool.isArmed()) {
            b.setIcon(new ImageIcon("images/stopMeasureOver.jpg"));

        } else {
            b.setIcon(new ImageIcon("images/metalrulerOver.jpg"));
        }
        parent.infoTag.setVisible(true);
    }

    public void mouseExited(MouseEvent e) {
        JButton b = (JButton) e.getSource();
        if (measureTool.isArmed()) {
            b.setIcon(new ImageIcon("images/stopMeasure.jpg"));
        } else {
            b.setIcon(new ImageIcon("images/metalruler.jpg"));
        }
        parent.infoTag.setVisible(false);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}