/*
TODO: Make this one of thos emini modules on friday 24
*/

package edu.mines.acmX.exhibit.frontend;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.mines.acmX.exhibit.stdlib.input_processing.imaging.PictureRGB;

public class PictureRGBApplication extends JPanel {
    PictureRGB prgb;
    BufferedImage img;
    public PictureRGBApplication() {
        prgb = new PictureRGB();
        //img = prgb.getPicture();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Picture");
        f.setSize(640, 480);
        f.setTitle("Image");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        Container pane = f.getContentPane();
        PictureRGBApplication panel = new PictureRGBApplication();
        pane.add(panel);
        f.setVisible(true);
    }
}
