package worldwind;

import gov.nasa.worldwind.poi.*;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/*
 * \class measureActionListener action listener for measuring the distance between several points on the globe
 */
class measureActionListener implements ActionListener {
    JPanel Pfrom;
    JPanel panel;
    JPanel Pto;
    JTextArea jta;
    WWJ parent;

    public measureActionListener(WWJ parent,
            JPanel Pfrom,
            JPanel panel,
            JPanel Pto,
            JTextArea jta) {
        this.parent = parent;
        this.Pfrom = Pfrom;
        this.panel = panel;
        this.Pto = Pto;
        this.jta = jta;
    }

    public void actionPerformed(ActionEvent e) {
        JTextField from = (JTextField) Pfrom.getComponent(0);
        JTextField to = (JTextField) Pto.getComponent(0);
        JComboBox jcbf = ((JComboBox) Pfrom.getComponent(1));
        JComboBox jcbt = ((JComboBox) Pto.getComponent(1));

        jcbf.removeAllItems();
        jcbt.removeAllItems();
        jcbf.setVisible(false);
        jcbt.setVisible(false);

        java.util.List<PointOfInterest> combined = 
                new ArrayList<PointOfInterest>();
        if (parent.textFieldPoints(from.getText()).isEmpty()) {
            parent.mkr.clear();
            parent.positions.clear();
            parent.standardDialogBox("Incorrect input", "Input is incorrect!");
            return;
        }
        parent.ww.getView().goTo(
                new Position(
                        parent.textFieldPoints(
                            from.getText()).get(0).getLatlon(), 0), 3000e3);
        combined.add((parent.textFieldPoints(from.getText()).get(0)));
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (i % 3 == 1) {
                if (!(parent.textFieldPoints(
                        ((JTextField) 
                        panel.getComponent(i)).getText()).isEmpty())) {
                    JComboBox jcbm = (JComboBox) panel.getComponent(i + 2);
                    jcbm.setVisible(false);
                    if (parent.textFieldPoints((
                            (JTextField) panel.getComponent(i)).getText()).size() > 1) {
                        jcbm.removeAllItems();
                        java.util.List<PointOfInterest> each = 
                                parent.textFieldPoints((
                                (JTextField) panel.getComponent(i)).getText());
                        for (PointOfInterest poi : each) {
                            jcbm.addItem(poi);
                        }
                        jcbm.setVisible(true);

                    }
                    combined.add((parent.textFieldPoints(((
                            (JTextField) panel.getComponent(i)).getText())).get(0)));
                } else {
                    continue;
                }
            }
        }
        if ((parent.textFieldPoints(to.getText())).isEmpty()) {
            parent.standardDialogBox("Incorrect input", "Input is incorrect!");
            return;
        }
        if (parent.textFieldPoints(from.getText()).size() > 1) {
            java.util.List<PointOfInterest> each = 
                    parent.textFieldPoints(from.getText());
            for (PointOfInterest poi : each) {
                jcbf.addItem(poi);
            }
            jcbf.setVisible(true);

        }
        if (parent.textFieldPoints(to.getText()).size() > 1) {
            java.util.List<PointOfInterest> each = 
                    parent.textFieldPoints(to.getText());
            for (PointOfInterest poi : each) {
                jcbt.addItem(poi);
            }
            jcbt.setVisible(true);
        }
        combined.add((parent.textFieldPoints(to.getText()).get(0)));
        PointOfInterest poi1, poi2;
        double dist = 0;
        Iterator it = combined.iterator();
        poi2 = (PointOfInterest) it.next();
        for (int i = 1; i < combined.size(); i++) {
            poi1 = poi2;
            poi2 = (PointOfInterest) it.next();
            dist += LatLon.ellipsoidalDistance(poi1.getLatlon(), 
                    poi2.getLatlon(),
                    6378.1370,
                    6356.2569);
        }
        if (dist > 0.001) {
            jta.setText((new Double(dist)).toString() + " Km");
        } else {
            jta.setText("Aceeasi locatie");
        }

        it = combined.iterator();
        parent.positions.clear();
        parent.mkr.clear();
        MarkerAttributes attr =
                new BasicMarkerAttributes(Material.RED,
                        BasicMarkerShape.SPHERE,
                        1d,
                        10,
                        5);
        for (int i = 0; i < combined.size(); i++) {
            Position poz = 
                    new Position(((PointOfInterest) it.next()).getLatlon(), 0);
            parent.positions.add(poz);
            Marker mk = new BasicMarker(poz, attr);
            mk.setPosition(poz);
            parent.mkr.add(mk);
        }
        parent.Mlayer.setMarkers(parent.mkr);
        parent.segm.setPositions(parent.positions);
        parent.ww.redraw();
    }
}