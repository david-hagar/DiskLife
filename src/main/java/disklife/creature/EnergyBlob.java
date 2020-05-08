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

import disklife.DiskModel;
import disklife.DiskSim;
import disklife.Vector2D;

import java.awt.*;

public class EnergyBlob extends DiskModel {

    public static int collisionID;

    static {
        collisionID = DiskSim.allocateCollidableID();
    }

    float energyAmount = 0.0f;

    public EnergyBlob(DiskSim sim, float energyAmount, Vector2D p, Vector2D v) {
        super(sim, (float) Math.sqrt(energyAmount) * 1.0f, p, v);

        this.energyAmount = energyAmount;

    }

    public int getCollisionID() {
        return collisionID;
    }


    public float getEnegyAmount() {
        return energyAmount;
    }


    public void draw(Graphics2D g) {
        super.draw(g);
        g.setColor(Color.yellow);
        g.fill(circle);

    }

    public void processCollision(Creature c, float time) {
        c.addEnergy(energyAmount);
        //sim.remove( (DiskModel) this );
        dispose();
    }


}