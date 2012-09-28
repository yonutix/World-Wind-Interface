package worldwind;

import java.awt.event.*;
import javax.swing.*;

/*
 * \class ViaAddActionListener Action listener for "Via" buttons
 */
class ViaAddActionListener implements ActionListener {
    /**
     * \param vb - panel to add items in
     */
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
        del.addActionListener(new DeleteTextFieldActionListener(vb, 
                jtf, 
                del, 
                jcb));
        vb.add(del);
        vb.add(jcb);
        parent.pack();
    }
}