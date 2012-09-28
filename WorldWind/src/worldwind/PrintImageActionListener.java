package worldwind;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.print.*;

/*
 * Listener for print button
 */
class PrintImageActionListener implements ActionListener {

    /**
     * \param imgb - image to be printed
     */
    BufferedImage imgb;
    WWJ parent;

    public PrintImageActionListener(WWJ parent, BufferedImage imgb) {
        this.imgb = imgb;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        final Image img = new ImageIcon(imgb).getImage();
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new Printable() {

            public int print(Graphics graphics, 
                    PageFormat pageFormat,
                    int pageIndex) throws PrinterException {
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                graphics.drawImage(img,
                        0,
                        0,
                        img.getWidth(null),
                        img.getHeight(null),
                        null);
                return PAGE_EXISTS;
            }
        });
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception prt) {
                parent.standardDialogBox("Error", "Error printing the image");
            }
        }
    }
}