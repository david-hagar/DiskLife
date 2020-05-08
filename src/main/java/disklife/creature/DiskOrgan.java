package disklife.creature;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c)
 * Company:
 *
 * @author
 * @version 1.0
 */

import disklife.Vector2D;

import java.awt.*;

public class DiskOrgan extends Organ {

    float energy = (float) Math.random() * 0.9f;

    public DiskOrgan(Creature creature, Vector2D pos, float r) {
        super(creature, pos, r);

        float crad = creature.getRadius();
        maxPoints = hitPoints = crad * 4;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.blue);
        super.draw(g);

    }

    void runTimeStep() {
        energy += 0.005 * radius;
        if (energy > 1.0f) {
            creature.copy();
            energy -= 1.0f;
        }
    }

    void doDamage(float damage) {
        super.doDamage(damage);
        if (hitPoints <= 0.0f)
            creature.expire();

    }

}