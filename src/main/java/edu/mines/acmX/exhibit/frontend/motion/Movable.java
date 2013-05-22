package edu.mines.acmX.exhibit.frontend.motion;

import edu.mines.acmX.exhibit.frontend.graphics.Path;
import edu.mines.acmX.exhibit.frontend.hands.Hand;
import edu.mines.acmX.exhibit.frontend.items.MotionState;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate;
import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;


/**
 * Created with IntelliJ IDEA.
 * User: Lauren
 * Date: 12/2/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Movable {
   public Path getPath();
   public void setPath(Path p);

   public Coordinate getPosition();
   public void setPosition(Coordinate loc);

   public boolean isRemovable();
   public void setRemovable(boolean removable);

   public boolean isTouchable();

   public Sprite getSprite();
   public boolean collidesWithPoint(Coordinate point);

   public void setMotionState(MotionState state);
   public MotionState getMotionState();

   public void reactToCollision(Hand hand, double currentTimeSec);

}
