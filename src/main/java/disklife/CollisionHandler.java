/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 *
 * @author
 * @version 1.0
 */
package disklife;

public interface CollisionHandler {

    void processCollision(Collidable c1, Collidable c2, float time);

    float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame;
}