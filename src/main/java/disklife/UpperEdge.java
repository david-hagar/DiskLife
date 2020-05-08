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

public class UpperEdge extends BoxEdge {

    public UpperEdge(CollidableObjectHolder holder) {
        this.holder = holder;
    }

    void processUpCrossing(BoxEdge edge) {
        if (edge instanceof LowerEdge && value != edge.value) {
            holder.collisionDetector.removePartialCollision(holder.collidableObject, edge.holder.collidableObject);
        }

    }

}