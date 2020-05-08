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

import disklife.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;

public class Creature extends DiskModel {

    public static int collisionID;

    static {
        collisionID = DiskSim.allocateCollidableID();
    }

    DiskOrgan diskOrgan;
    boolean alive = true;
    private ArrayList organs = new ArrayList();


    public Creature(DiskSim sim, float r, Vector2D p, Vector2D v) {
        super(sim, r, p, v);
        float brad = r * 0.2f;
        addOrgan(new Brain(this, new Vector2D(0.0f, r - brad), brad));
        addOrgan(diskOrgan = new DiskOrgan(this, new Vector2D(0.0f, 0.0f), r / 2));


//		Force f = new Force()
//		{
//
//			Vector2D v = new Vector2D(0, -1);
//
//
//			public void disconnect()
//			{
//			}
//
//
//			public Vector2D getForce()
//			{
//				return v;
//			}
//		};
//
//		addForce(f);

    }

    public static DiskModel newRandom(DiskSim sim, double maxV, double minRadius,
                                      double maxRadius) {

        float px = (float) (200.0 * Math.random() - 100.0);
        float py = (float) (200.0 * Math.random() - 100.0);
        float vx = (float) (maxV * Math.random() * 2.0 - maxV);
        float vy = (float) (maxV * Math.random() * 2.0 - maxV);
        float r = (float) ((maxRadius - minRadius) * Math.random() + minRadius);

        Creature dm = new Creature(sim, r, new Vector2D(px, py), new Vector2D(vx,
                vy));
        dm.angularVelocity = (float) (Math.PI / 50 * Math.random() - Math.PI / 100);
        dm.angularPosition = (float) (Math.PI * 2.0 * Math.random());
        dm.thrustMag = (float) (10.0 * Math.random());
        return dm;
    }

    public void addOrgan(Organ org) {
        organs.add(org);
    }

    public void draw(Graphics2D g) {
        super.draw(g);
        AffineTransform t = g.getTransform();

        g.translate(position.x, position.y);
        g.rotate(angularPosition);

        Iterator i = organs.iterator();
        while (i.hasNext()) {
            ((Organ) i.next()).draw(g);
        }


        g.setTransform(t);
    }

    public void runTimeStep() {
        if (!alive)
            return;
        super.runTimeStep();

        Iterator i = organs.iterator();
        while (i.hasNext()) {
            ((Organ) i.next()).runTimeStep();
        }

    }

    public void expire() {
        // sim.addToRemoveList((DiskModel)this);
        dispose();
        EnergyBlob eb = new EnergyBlob(sim, diskOrgan.energy,
                new Vector2D(position), new Vector2D(velocity));
        eb.angularVelocity = 0.1f;

        try {
            sim.addToAddList(eb);
        } catch (CollisionException e) {
            DiskLog.printlnErr("could not add new Blob due to collision");
        }


        organs = null;
        diskOrgan = null;
        alive = false;

    }


    public void doExtraCollisionProcessing(DiskModel other,
                                           Vector2D collisionAxis, float delta_v) {
        if (!alive || !isActive || !other.isActive)
            return;
        Iterator i = organs.iterator();
        while (i.hasNext()) {
            ((Organ) i.next()).doDamage(Math.abs(delta_v) * 0.05f * mass);
            // DiskLog.printlnErr( "damage=" + delta_v * 0.1f );
        }

        if (!isActive)
            return;
        if (forces.size() < 6 && other.forces.size() < 6) {
            if (hasConnection(other))
                return;
            float k = (mass + other.mass) / 10.0f;
            Spring s = new Spring(this, other, sim, radius + other.getRadius() + 3
                    + 0.1f * (float) Math.random(), k);
            s.breakForce = (mass + other.mass) / 3 * ((float) Math.random() + 0.5f);
            sim.addSpring(s);
        }

    }


    public void copy() {
        Vector2D p = new Vector2D(position);
        p.y -= radius * 3 + 0.01f;
        float newRadius = Math.abs(radius + (float) Math.random() * 1.0f - 0.5f);
        Creature nc = new Creature(sim, newRadius, p, Vector2D.neg(velocity));
        nc.angularVelocity = angularVelocity
                + (float) (Math.PI / 200 * Math.random() - Math.PI / 400);
        nc.thrustMag = thrustMag + (float) (4.0 * Math.random() - 2.0);
        // DiskLog.println( "old=" +this.position + " new=" + p);
        try {
            sim.addToAddList(nc);
            // float k = ( mass + nc.mass ) /10.0f;
            // Spring s = new Spring( this, nc, sim, radius + nc.getRadius() + 5.0f ,
            // k);
            // s.breakForce = ( mass + nc.mass )/5;
            // sim.addSpring(s);
        } catch (CollisionException e) {
            DiskLog.println("could not add new due to collision");
        }

    }


    public void addEnergy(float amount) {
        if (!alive)
            return;
        diskOrgan.energy += amount;
    }


    public int getCollisionID() {
        return collisionID;
    }

}