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

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableRect2D extends Rectangle2D.Float implements Serializable {
    //float top;
    //float right;


    public SerializableRect2D() {
        super();
        //top = y+height;
        //right = x+width;
    }

    public SerializableRect2D(float x, float y, float w, float h) {
        super(x, y, w, h);
        //top = y+height;
        //right = x+width;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(new java.lang.Float(x));
        out.writeObject(new java.lang.Float(y));
        out.writeObject(new java.lang.Float(width));
        out.writeObject(new java.lang.Float(height));
        out.flush();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        java.lang.Float dbl = null;
        dbl = (java.lang.Float) in.readObject();
        x = dbl.floatValue();
        dbl = (java.lang.Float) in.readObject();
        y = dbl.floatValue();
        dbl = (java.lang.Float) in.readObject();
        width = dbl.floatValue();
        dbl = (java.lang.Float) in.readObject();
        height = dbl.floatValue();

//top = y+height;
//right = x+width;
    }


    public boolean intersects(SerializableRect2D r) {
        if (isEmpty() || r.width <= 0 || r.height <= 0) {
            return false;
        }
        return (x + width >= r.x &&
                y + height >= r.y &&
                x <= r.x + r.width &&
                y <= r.y + r.height);
    }


}