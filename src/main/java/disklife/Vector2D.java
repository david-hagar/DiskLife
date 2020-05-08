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

import java.awt.geom.Point2D;


public class Vector2D extends Point2D.Float {

    public Vector2D() {
        super(0.0f, 0.0f);
    }

    public Vector2D(float x, float y) {
        super(x, y);
    }

    public Vector2D(Vector2D v) {
        super(v.x, v.y);
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        Vector2D vNew = new Vector2D(v1);
        vNew.x += v2.x;
        vNew.y += v2.y;
        return vNew;
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        Vector2D vNew = new Vector2D(v1);
        vNew.x -= v2.x;
        vNew.y -= v2.y;
        return vNew;
    }

    public static Vector2D mult(Vector2D v, float k) {
        Vector2D vNew = new Vector2D(v);
        vNew.x *= k;
        vNew.y *= k;
        return vNew;
    }

    public static Vector2D div(Vector2D v, float k) {
        Vector2D vNew = new Vector2D(v);
        vNew.x /= k;
        vNew.y /= k;
        return vNew;
    }

    public static Vector2D neg(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        vNew.x = -vNew.x;
        vNew.y = -vNew.y;
        return vNew;
    }

    public static Vector2D unit(Vector2D v) {
        Vector2D vNew = new Vector2D(v);
        float mag = (float) Math.sqrt(vNew.x * vNew.x + vNew.y * vNew.y);
        vNew.x /= mag;
        vNew.y /= mag;
        return vNew;
    }

    public void set(Vector2D v) {
        x = v.x;
        y = v.y;
    }

    public float dot(Vector2D v) {
        return x * v.x + y * v.y;
    }

    public void add(Vector2D v) {
        x += v.x;
        y += v.y;
    }

    public void sub(Vector2D v) {
        x -= v.x;
        y -= v.y;
    }

    public void mult(float k) {
        x *= k;
        y *= k;
    }

    public void div(float k) {
        x /= k;
        y /= k;
    }

    public void neg() {
        x = -x;
        y = -y;
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void unit() {
        float mag = (float) Math.sqrt(x * x + y * y);
        x /= mag;
        y /= mag;
    }

    public void zeroOut() {
        x = y = 0.0f;
    }


}