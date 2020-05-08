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

import java.util.ArrayList;
import java.util.Iterator;

public interface Collidable {

    SerializableRect2D getBounds();

    void processFrameEnd();

    void addEvent(Collision c);

    void removeEvent(Collision c);

    Iterator getEventIterator();

    ArrayList getEventListClone();

    CollidableObjectHolder getCollidableObjectHolder();

    void setCollidableObjectHolder(CollidableObjectHolder holder);

    int getCollisionID();

}