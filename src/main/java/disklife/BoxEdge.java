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

public class BoxEdge implements Comparable {

    CollidableObjectHolder holder;
    float value = 0.0f;

    public BoxEdge() {
    }

    public BoxEdge(float value) {
        this.value = value;
    }

    void processUpCrossing(BoxEdge edge) {
    }

    public int compareTo(Object o) {
        BoxEdge e = (BoxEdge) o;

        if (value == e.value) {
            if (hashCode() == e.hashCode())
                return 0;
            else
                return (hashCode() < e.hashCode()) ? -1 : 1;
        }

        return (value < e.value) ? -1 : 1;
    }


}