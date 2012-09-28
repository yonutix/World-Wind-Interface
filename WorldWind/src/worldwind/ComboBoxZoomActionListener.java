package worldwind;

import gov.nasa.worldwind.poi.*;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.geom.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * \class ComboBoxZoomActionListener Listener for "Zoom" combo box
 */
class ComboBoxZoomActionListener implements ActionListener {

    /**
     *  \param input text field to be modified
     */
    JTextField input;
    WWJ parent;

    public ComboBoxZoomActionListener(WWJ parent, JTextField input) {
        this.input = input;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent event) {
        JComboBox cb = (JComboBox) event.getSource();
        if (cb.getSelectedItem() != null) {
            PointOfInterest selectedPoi = 
                    (PointOfInterest)cb.getSelectedItem();
            parent.mkr.clear();
            MarkerAttributes attr = new BasicMarkerAttributes(Material.RED, 
                    BasicMarkerShape.SPHERE, 1d, 10, 5);
            Position poz = new Position(selectedPoi.getLatlon(), 0);
            Marker mk = new BasicMarker(poz, attr);
            mk.setPosition(poz);
            parent.mkr.add(mk);
            parent.Mlayer.setMarkers(parent.mkr);
            parent.ww.redraw();
            input.setText(cb.getSelectedItem().toString());
            parent.ww.redraw();
            parent.ww.getView().goTo(new Position(selectedPoi.getLatlon(), 0), 
                                    25e3);
        }
        cb.setVisible(false);
    }
}