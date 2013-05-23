package edu.mines.acmX.exhibit.frontend.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.mines.acmX.exhibit.frontend.GameManager;
import edu.mines.acmX.exhibit.frontend.hands.Hand;
import edu.mines.acmX.exhibit.frontend.hands.PlayerHand;
import edu.mines.acmX.exhibit.frontend.utils.PlayerMode;
import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;

public class PlayerOptionsScreen {

    private static final Logger logger = Logger.getLogger(GameScreen.class);

    private static final int TIME_TO_SHOW = 4;

    private ArrayList<Sprite> handSprites;
    private java.util.List<Hand> hands;
    private ArrayList<TextSpritesHolder> textSpriteHolders;
    private long previousTime;
    private Sprite background;
    int countdown;
    private int screenWidth;

    public PlayerOptionsScreen(GameManager gameManager) {
        background = new Sprite("src/main/resources/SpriteImages/playerSelection.jpg", 0, 0);
        handSprites = new ArrayList<Sprite>();
        hands = new ArrayList<Hand>();

        Hand tempHand = new PlayerHand(gameManager, 0);
        hands.add(tempHand);
        addHandSprite(tempHand.getSprite());

        textSpriteHolders = new ArrayList<TextSpritesHolder>();

        TextSpritesHolder holder = new TextSpritesHolder() {
            public List<TextSprite> getTextSprites() {
                Font f = new Font("Stencil", Font.BOLD, 100);
                Color c = Color.green;
                List<TextSprite> sprites = new ArrayList<TextSprite>();
                sprites.add(new TextSprite(Integer.toString(countdown), f, c, 70, 200));
                return sprites;
            }
        };
        textSpriteHolders.add(holder);
        countdown = TIME_TO_SHOW;
        previousTime = 0;
        screenWidth = 1920;
    }

    public void paint(Graphics2D g2d, Component canvas) {
    	Image img = ResourceManager.getInstance().getImage(background.getImageFilename());
        g2d.drawImage(img, background.getX(), background.getY(), canvas);
        drawTextSprites(g2d);
        drawHands(g2d, canvas);
    }

    /**
     * Adds a hand sprite to the hands array
     * @param s
     */
    public void addHandSprite(Sprite s) {
        handSprites.add(s);
    }

    /**
     * Draws each hand as long as it's x position is greater than -1. The back end returns -1 when
     * a hand is not available.
     * @param g2d
     * @param canvas
     */
    private void drawHands(Graphics2D g2d, Component canvas) {

        for (Sprite hand : handSprites) {
            //compensate for current scale
            int x = (int) Math.round(hand.getX() * GraphicsConstants.SCALE_FACTOR);
            int y = (int) Math.round(hand.getY() * GraphicsConstants.SCALE_FACTOR);
            //If it is negative one it is a sentinel for it not existing so ignore.
            if (hand.getX() > -1) {
            	Image img = ResourceManager.getInstance().getImage(hand.getImageFilename());
                g2d.drawImage(img, x, y, canvas);
            }
        }
    }

    /**
     * Draws the text sprites that are held in a TextSpriteHolder
     *
     * @param g
     */
    private void drawTextSprites(Graphics2D g) {
        for (TextSpritesHolder holder : textSpriteHolders) {
            for (TextSprite textSprite : holder.getTextSprites()) {
                g.setColor(textSprite.getColor());
                g.setFont(textSprite.getFont());
                int x = (int) Math.floor(textSprite.getX() * GraphicsConstants.SCALE_FACTOR);
                int y = (int) Math.floor(textSprite.getY() * GraphicsConstants.SCALE_FACTOR);
                g.drawString(textSprite.getMessage(), x, y);
            }
        }
    }

    public void updateThis() {
        for (Hand hand : hands) {
            hand.updateLocation();
        }

        if ((System.currentTimeMillis() / 1000) > previousTime + 1) {
            if (countdown >= 0) {
                countdown -= 1;
            }
            previousTime = System.currentTimeMillis() / 1000;
        }
    }

    public boolean canGameStart() {
        return (countdown == 0);
    }

    public PlayerMode getPlayerMode() {
        if (hands.get(0).getX() < screenWidth / 2) {
            return PlayerMode.ONE_PLAYER;
        } else {
            return PlayerMode.TWO_PLAYER;
        }
    }

}
