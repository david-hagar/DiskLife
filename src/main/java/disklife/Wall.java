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

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Wall implements Collidable {

    SerializableRect2D rect = new SerializableRect2D();
    HashSet events = new HashSet();
    ArrayList emptyList = new ArrayList(0);

    public Wall() {
    }

    abstract void updateRect();

    public SerializableRect2D getBounds() {
        return rect;
    }

    void draw(Graphics2D g) {
        g.setColor(Color.red);
        g.fill(rect);

    }

    public void processFrameEnd() {
        //events.clear();
    }

    public void addEvent(Collision c) {
        //events.add(c);
    }

    public void removeEvent(Collision c) {
        //events.remove(c);
    }

    public Iterator getEventIterator() {
        return events.iterator();  // could return null iterator
    }

    public ArrayList getEventListClone() {
        return emptyList;
    }

}