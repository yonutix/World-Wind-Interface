package worldwind;

import java.awt.event.*;
import javax.swing.*;

/*
 * \class Listener for delete section
 */
public class DeleteTextFieldActionListener implements ActionListener {
    /*
     * \param vb  - panel to remove from
     * \param viaTextField - text field to be removed
     * \param removeTextFieldButton - button to be removed
     * \param jcb hidden or visible combo box to be removed
     */
    JPanel vb;
    JTextField viaTextFields;
    JButton removeTextFieldButton;
    JComboBox jcb;
    WWJ parent;

    public DeleteTextFieldActionListener(WWJ parent) {
        this.parent = parent;
    }

    public DeleteTextFieldActionListener(JPanel vb, 
            JTextField viaTextFields, 
            JButton removeTextFieldButton, 
            JComboBox jcb) {
        this.vb = vb;
        this.viaTextFields = viaTextFields;
        this.removeTextFieldButton = removeTextFieldButton;
        this.jcb = jcb;
    }

    public void actionPerformed(ActionEvent e) {
        vb.remove(viaTextFields);
        vb.remove(removeTextFieldButton);
        vb.remove(jcb);
        parent.pack();
    }
}