/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 *
 * @author
 * @version 1.0
 */
package disklife.creature;

import disklife.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Organ {

    Creature creature;
    Vector2D position;
    float radius;
    float hitPoints = 1.0f;
    float maxPoints = 1.0f;
    Color currentColor = Color.green;


    Ellipse2D.Float circle;

    public Organ(Creature creature, Vector2D position, float radius) {
        this.position = position;
        this.radius = radius;
        this.creature = creature;

        circle = new Ellipse2D.Float(
                position.x - radius, position.y - radius, radius * 2.0f, radius * 2.0f);
    }

    public void draw(Graphics2D g) {
        g.setColor(currentColor);
        g.draw(circle);

    }

    void runTimeStep() {

    }

    void doDamage(float damage) {
        hitPoints -= damage;
        if (hitPoints <= 0.0f)
            hitPoints = 0.0f;
        //DiskLog.printlnErr( "sat=" +hitPoints/maxPoints/2);
        currentColor = Color.getHSBColor(hitPoints / maxPoints / 4, 1.0f, 1.0f);
    }

}