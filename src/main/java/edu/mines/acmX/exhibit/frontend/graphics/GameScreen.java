package edu.mines.acmX.exhibit.frontend.graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;

/**
 * The GameScreen singleton class is responsible for drawing the sprites.
 * Generally to a sprite to be drawn call the addSprite method. To get it to stop
 * drawing the sprites remove the sprite using removeSprite()
 *
 * <p/>
 * <p/>
 * Created with IntelliJ IDEA.
 * User: jzeimen
 * Date: 10/20/12
 * Time: 9:13 PM
 */
public class GameScreen {

    private static final Logger logger = Logger.getLogger(GameScreen.class);

    private static GameScreen INSTANCE;
    private Sprite background1;
    private Sprite background2;
    private Sprite backgroundChutesAndFrame;
    private Sprite backgroundScoreFrame;
    private Sprite backgroundToDraw;
    private ArrayList<TextSpritesHolder> textSpriteHolders;
    private LinkedList<Sprite> sprites;
    private ArrayList<Sprite> recycleBinSprites;
    private ArrayList<Sprite> handSprites;
    private ArrayList<Sprite> gameOverSprites;

    private GameScreen() {

        background1 = new Sprite("src/main/resources/SpriteImages/Backgrounds/ui_background_1.jpg", 0, 0);
        background2 = new Sprite("src/main/resources/SpriteImages/Backgrounds/ui_background_2.jpg", 0, 0);
        backgroundToDraw = background1;
        backgroundChutesAndFrame = new Sprite("src/main/resources/SpriteImages/Backgrounds/ui_frame.png", 0, 0);
        backgroundScoreFrame = new Sprite("src/main/resources/SpriteImages/Backgrounds/ui_score_frame.png", 0, 0);

        textSpriteHolders = new ArrayList<TextSpritesHolder>();
        handSprites = new ArrayList<Sprite>();
        sprites = new LinkedList<Sprite>();
        recycleBinSprites = new ArrayList<Sprite>();
        gameOverSprites = new ArrayList<Sprite>();

        //Just a simple thread that toggles between the background images
        // so it feels more like the bars on the conveyor are turning.
        Thread backgroundSwitcher = new Thread("Background Switcher"){
          public void run(){
              while(true){
                  int sleepTime = 300;
                  synchronized (backgroundToDraw){
                      backgroundToDraw = background1;
                  }
                  try {
                      Thread.sleep(sleepTime);
                  } catch (InterruptedException e) {
                      //ignore
                  }
                  synchronized (backgroundToDraw){
                      backgroundToDraw = background2;
                  }
                  try {
                      Thread.sleep(sleepTime);
                  } catch (InterruptedException e) {
                      //ignore
                  }
              }
          }
        };
        backgroundSwitcher.start();
    }

    public static final GameScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }

    public void paint(Graphics2D g2d, Component canvas) {
    	ResourceManager rscMgr = ResourceManager.getInstance();
        synchronized (backgroundToDraw) {
            g2d.drawImage(rscMgr.getImage(backgroundToDraw.getImageFilename()), backgroundToDraw.getX(), backgroundToDraw.getY(), canvas);
        }

        for (Sprite bin : recycleBinSprites) {
            g2d.drawImage(rscMgr.getImage(bin.getImageFilename()), GraphicsConstants.getScaledX(bin.getX()), GraphicsConstants.getScaledY(bin.getY()), canvas);
        }

        g2d.drawImage(rscMgr.getImage(backgroundScoreFrame.getImageFilename()), backgroundScoreFrame.getX(), backgroundScoreFrame.getY(), canvas);

        for (Sprite sprite : sprites) {
            int offset = (int) (GraphicsConstants.getScaledX(50));
            g2d.rotate(sprite.getPosition().getRotation(), (int) GraphicsConstants.getScaledX(sprite.getX()) + offset, GraphicsConstants.getScaledY(sprite.getY()) + offset);
            g2d.drawImage(rscMgr.getImage(sprite.getImageFilename()), GraphicsConstants.getScaledX(sprite.getX()), GraphicsConstants.getScaledY(sprite.getY()), canvas);
            g2d.rotate(-1.0 * sprite.getPosition().getRotation(), GraphicsConstants.getScaledX(sprite.getX()) + offset, GraphicsConstants.getScaledY(sprite.getY()) + offset);
        }

        for(Sprite sprites: gameOverSprites){
            g2d.drawImage(rscMgr.getImage(sprites.getImageFilename()), GraphicsConstants.getScaledX(sprites.getX()), GraphicsConstants.getScaledY(sprites.getY()), canvas);
        }

        g2d.drawImage(rscMgr.getImage(backgroundChutesAndFrame.getImageFilename()), backgroundChutesAndFrame.getX(), backgroundChutesAndFrame.getY(), canvas);

        drawHands(g2d, canvas);
        drawTextSprites(g2d);
    }

    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    public boolean removeSprite(Sprite s) {
        return sprites.remove(s);
    }

    public void addRecycleBinSprite(Sprite s) {
        recycleBinSprites.add(s);
    }

    public void addGameOverSprite(Sprite s){
        gameOverSprites.add(s);
    }

    public void addHandSprite(Sprite s) {
        handSprites.add(s);
    }

    public boolean addTextSpriteHolder(TextSpritesHolder textSpritesHolder) {
        return textSpriteHolders.add(textSpritesHolder);
    }

    /**
     *
     * draws each hand as long as it's x position is greater than -1. The back end returns -1 when
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

    /**
     * Preloads all of the gameScreen images so that they are ready to be used
     */
    public void preLoadImages() {
    	ResourceManager rscMgr = ResourceManager.getInstance();
		rscMgr.getImage(background1.getImageFilename());
		rscMgr.getImage(background2.getImageFilename());
		rscMgr.getImage(backgroundChutesAndFrame.getImageFilename());
		rscMgr.getImage(backgroundScoreFrame.getImageFilename());

        for (Sprite bin : recycleBinSprites) {
        	
            rscMgr.getImage(bin.getImageFilename());
        }

        String[] imagesToLoad =
                {"src/main/resources/SpriteImages/Bins/left_bin_paper_half.png",
                "src/main/resources/SpriteImages/Bins/left_bin_paper_full.png",
                "src/main/resources/SpriteImages/Bins/left_bin_plastic_half.png",
                "src/main/resources/SpriteImages/Bins/left_bin_plastic_full.png",
                "src/main/resources/SpriteImages/Bins/left_bin_hazard_half.png",
                "src/main/resources/SpriteImages/Bins/left_bin_hazard_full.png",
                "src/main/resources/SpriteImages/Bins/left_bin_glass_half.png",
                "src/main/resources/SpriteImages/Bins/left_bin_glass_full.png",

                "src/main/resources/SpriteImages/Bins/right_bin_paper_half.png",
                "src/main/resources/SpriteImages/Bins/right_bin_paper_full.png",
                "src/main/resources/SpriteImages/Bins/right_bin_plastic_half.png",
                "src/main/resources/SpriteImages/Bins/right_bin_plastic_full.png",
                "src/main/resources/SpriteImages/Bins/right_bin_hazard_half.png",
                "src/main/resources/SpriteImages/Bins/right_bin_hazard_full.png",
                "src/main/resources/SpriteImages/Bins/right_bin_glass_half.png",
                "src/main/resources/SpriteImages/Bins/right_bin_glass_full.png"};
        for(String s : imagesToLoad){
            ResourceManager.getInstance().getImage(s);
        }
    }

}
