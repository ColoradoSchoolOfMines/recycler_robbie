package edu.mines.acmX.exhibit.frontend.graphics;

import edu.mines.acmX.exhibit.stdlib.graphics.Sprite;

/**
 * Displayables have a sprite. This is used so that the game screen doesn't need to know the difference
 * between things like a recyclable and a hand.
 * <p/>
 * Created with IntelliJ IDEA.
 * User: jzeimen
 * Date: 10/20/12
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Displayable {

    public Sprite getSprite();

}
