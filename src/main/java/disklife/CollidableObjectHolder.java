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

public class CollidableObjectHolder {

    Collidable collidableObject;
    CollisionDetector collisionDetector;
    UpperEdge xUpperEdge = new UpperEdge(this);
    LowerEdge xLowwerEdge = new LowerEdge(this);
    UpperEdge yUpperEdge = new UpperEdge(this);
    LowerEdge yLowwerEdge = new LowerEdge(this);


    public CollidableObjectHolder(Collidable collidableObject, CollisionDetector collisionDetector) {
        this.collidableObject = collidableObject;
        this.collisionDetector = collisionDetector;
        update();
    }

    void update() {
        DiskLog.println("CollidableObjectHolder update obj=" + collidableObject);
        SerializableRect2D bounds = collidableObject.getBounds();
        xUpperEdge.value = bounds.x + bounds.width;
        xLowwerEdge.value = bounds.x;
        yUpperEdge.value = bounds.y + bounds.height;
        yLowwerEdge.value = bounds.y;
    }
}