package edu.mines.acmX.exhibit.frontend.graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;

import org.apache.log4j.Logger;

import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;


/**
 * Created with IntelliJ IDEA.
 * User: Amanreet
 * Date: 11/27/12
 * Time: 9:17 PM
 * This class takes care of displaying the splash screen
 */
public class InstructionScreen {

    private static final Logger logger = Logger.getLogger(InstructionScreen.class);
    private static final String INSTRUCTIONS_FILENAME = "src/main/resources/SpriteImages/instructions_full.jpg";

    private Sprite background;

    public InstructionScreen() {
        background = new Sprite(INSTRUCTIONS_FILENAME, 0, 0);
    }

    public void paint(Graphics2D g2d, Component canvas) {
    	Image img = ResourceManager.getInstance().getImage(background.getImageFilename());
        g2d.drawImage(img, background.getX(), background.getY(), canvas);
    }

}

