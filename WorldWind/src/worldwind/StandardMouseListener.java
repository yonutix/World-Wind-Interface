package worldwind;

import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import java.awt.event.*;
import javax.swing.*;

/*
 * \class StandardMouseListener Standard mouse listener
 */
class StandardMouseListener implements MouseListener {

    WWJ parent;
    WorldWindowGLCanvas ww;

    public StandardMouseListener(WWJ parent, WorldWindowGLCanvas ww) {
        this.parent = parent;
        this.ww = ww;
    }
    
    /**
     * \brief buttons images changing on mouse over
     */
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
            parent.tagContent("2D/3D Globe", 
                    "See 3D or 2D globe",
                    b.getLocationOnScreen());
            parent.infoTag.setVisible(true);
        }
        if (b.getName().compareTo("boundaries") == 0) {
            if (parent.politicalBoundries.isEnabled()) {
                b.setIcon(new ImageIcon("images/deactivateBoundariesOver.jpg"));
            } else {
                b.setIcon(new ImageIcon("images/boundariesOver.jpg"));
            }
            parent.tagContent("Political boundaries",
                    "Turn on/off political boundaries",
                    b.getLocationOnScreen());
            parent.infoTag.setVisible(true);

        }
        if (b.getName().compareTo("earthquake") == 0) {
            b.setIcon(new ImageIcon("images/earthquakeOver.jpg"));
            parent.tagContent("Earthquakes",
                    "See earthquakes reports",
                    b.getLocationOnScreen());
            parent.infoTag.setVisible(true);
        }
        if (b.getName().compareTo("weather") == 0) {
            b.setIcon(new ImageIcon("images/weatherOver.jpg"));
            parent.tagContent("Weather",
                    "See weather Reports",
                    b.getLocationOnScreen());
            parent.infoTag.setVisible(true);
        }
        if (b.getName().compareTo("ss") == 0) {
            b.setIcon(new ImageIcon("images/screenshotOver.jpg"));
            parent.tagContent("Screenshot",
                    "Take a screenshot",
                    b.getLocationOnScreen());
            parent.infoTag.setVisible(true);
        }
        if (b.getName().compareTo("open") == 0) {
            b.setIcon(new ImageIcon("images/openFIleOver.jpg"));
            parent.tagContent("Open image",
                    "Open a specific image",
                    b.getLocationOnScreen());
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


/*
 * \class MenuActionListener Listener for exiting the main frame
 */
class MenuActionListener implements ActionListener {

    WWJ parent;
    JFrame f;

    public MenuActionListener(WWJ parent) {
        this.f = (JFrame) parent;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        if (((JMenuItem) e.getSource()).getName().compareTo("exit") == 0) {
            System.exit(-1);
        }
        if (((JMenuItem) e.getSource()).getName().compareTo("about") == 0) {
            parent.aboutWindow();
        }
    }
}

/*
 * \class ButtonActionListener closing the "about" window
 */
class ButtonActionListener implements ActionListener {

    JDialog f;

    public ButtonActionListener(JDialog f) {
        this.f = f;
    }

    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getName().compareTo("exit") == 0) {
            f.dispose();
        }
    }
}