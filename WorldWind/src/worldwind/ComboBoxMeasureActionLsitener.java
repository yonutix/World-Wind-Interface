/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package worldwind;

import java.awt.event.*;
import javax.swing.*;

/*
 * \class ComboBoxMeasureActionLsitener Listener for a combo box wich is linked
 * with a text field
 */
class ComboBoxMeasureActionLsitener implements ActionListener {
    /**
     * \param text text field to make change in
     */
    JTextField input;
    
    public ComboBoxMeasureActionLsitener(JTextField input) {
        this.input = input;
    }
    
    public void actionPerformed(ActionEvent event) {
        JComboBox cb = (JComboBox) event.getSource();
        if (cb.getSelectedItem() != null) {
            input.setText(cb.getSelectedItem().toString());
        }
    }
}