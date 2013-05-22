package edu.mines.acmX.exhibit.frontend.items;


import org.apache.log4j.Logger;

import edu.mines.acmX.exhibit.frontend.graphics.Displayable;
import edu.mines.acmX.exhibit.frontend.graphics.Path;
import edu.mines.acmX.exhibit.frontend.hands.Hand;
import edu.mines.acmX.exhibit.frontend.motion.Movable;
import edu.mines.acmX.exhibit.frontend.utils.GameConstants;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate;
import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;


/**
 * Created with IntelliJ IDEA.
 * User: Lauren
 * Date: 12/2/12
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinFeedback implements Displayable, Movable {

    private static final Logger logger = Logger.getLogger(BinFeedback.class);

    private Sprite sprite;
    private Path path;
    private boolean removable;
    private MotionState motionState;

    public BinFeedback(String image, Path path) {
        this.sprite = new Sprite(image, (int) path.getInitialPosition().getX(), (int) path.getInitialPosition().getY());
        this.path = path;
        removable = true;
        motionState = MotionState.NONE;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Coordinate getPosition() {
        return sprite.getPosition();
    }

    public void setPosition(Coordinate position) {
        sprite.setPosition(position);
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isTouchable() {
        return motionState.isTouchable();
    }

    /**
     * checks for a collision with the given point.
     * Does *not* check if the item is touchable.
     *
     * @param point
     * @return
     */
    public boolean collidesWithPoint(Coordinate point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        if (x >= sprite.getX() - (GameConstants.SPRITE_X_OFFSET) &&
            x <= sprite.getX() + GameConstants.SPRITE_X_OFFSET) {
                if (y >= sprite.getY() - (GameConstants.SPRITE_Y_OFFSET) &&
                    y <= sprite.getY() + (GameConstants.SPRITE_Y_OFFSET)) {
                        return true;
                }
        }
        return false;
        
    }

    public MotionState getMotionState() {
        return motionState;
    }

    public void setMotionState(MotionState state) {
        motionState = state;
    }

    public void reactToCollision(Hand hand, double travelTime) {
        throw new IllegalStateException("BinFeedback should not be reacting to any collisions!");
    }

}
