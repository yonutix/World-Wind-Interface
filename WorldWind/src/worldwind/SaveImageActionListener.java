package worldwind;

import java.io.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;

/*
 * \class SaveImageActionListener listener for saving button 
 */
class SaveImageActionListener implements ActionListener {

    BufferedImage img;
    WWJ parent;

    public SaveImageActionListener(WWJ parent, BufferedImage img) {
        this.img = img;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new File("."));
        int returnVal = fc.showSaveDialog(parent.ww);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(img, 
                        "bmp",
                        new File(fc.getSelectedFile().getPath()));
            } catch (IOException ex) {
                parent.standardDialogBox("Output exception",
                        "Error trying writing file");
            }
        }
    }
}