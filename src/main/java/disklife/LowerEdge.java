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

public class LowerEdge extends BoxEdge {

    public LowerEdge(CollidableObjectHolder holder) {
        this.holder = holder;
    }

    void processUpCrossing(BoxEdge edge) {
        if (edge instanceof UpperEdge) {
            holder.collisionDetector.addPartialCollision(holder.collidableObject, edge.holder.collidableObject);
        }
    }
}