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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DiskModel implements Collidable {

    public static int collisionID = DiskSim.allocateCollidableID();
    public float mass = 1.0f;
    public Vector2D position = new Vector2D();
    public Vector2D zeroPosition = new Vector2D();
    public Vector2D velocity = new Vector2D();
    public float angularAcceleration = 0.0f;
    public float angularVelocity = 0.01f;
    public float angularPosition = 0.0f;
    public float thrustMag = 1.0f;
    public ArrayList forces = new ArrayList(3);
    public boolean isActive = true;
    protected DiskSim sim;
    protected float radius = 1.0f;
    protected float diameter = radius * 2.0f;
    protected float currentFrameTime = 0.0f;
    protected Ellipse2D.Float circle = new Ellipse2D.Float(
            position.x - radius, position.y - radius, diameter, diameter);

    SerializableRect2D eom = new SerializableRect2D(
            position.x - radius, position.y - radius, diameter, diameter);
    CollidableObjectHolder holder = new CollidableObjectHolder(this, null);
    ArrayList eomList = new ArrayList();
    ArrayList circleList = new ArrayList();
    ArrayList zcircleList = new ArrayList();
    private final Vector2D acceleration = new Vector2D();
    private final HashSet cevents = new HashSet(10);

    public DiskModel(DiskSim sim) {
        this.sim = sim;
    }


    public DiskModel(DiskSim sim, float r, Vector2D p, Vector2D v) {
        this.sim = sim;
        position.set(p);
        zeroPosition.set(p);
        velocity.set(v);
        setRadius(r);
        mass = (float) (2.0 * Math.PI * radius * radius);
        updateExtentOfMotion();
    }

    public static DiskModel newRandom(DiskSim sim, double maxV, double minRadius, double maxRadius) {
        DiskModel dm = new DiskModel(sim);
        dm.position.x = (float) (200.0 * Math.random() - 100.0);
        dm.position.y = (float) (200.0 * Math.random() - 100.0);
        dm.velocity.x = (float) (maxV * Math.random() * 2.0 - maxV);
        dm.velocity.y = (float) (maxV * Math.random() * 2.0 - maxV);
        dm.setRadius((float) ((maxRadius - minRadius) * Math.random() + minRadius));
        dm.mass = (float) (2.0 * Math.PI * dm.radius * dm.radius);
        dm.zeroPosition.set(dm.position);

        dm.updateExtentOfMotion();
        return dm;
    }

    public void runTimeStep() {
        if (!isActive)
            return;

        acceleration.zeroOut();
        Iterator iter = forces.iterator();
        while (iter.hasNext()) {
            acceleration.add(((Force) iter.next()).getForce());
        }
        Vector2D friction = Vector2D.mult(velocity, -1.0f);
        acceleration.add(friction);

        Vector2D thrust = new Vector2D(0.0f, thrustMag);
        AffineTransform.getRotateInstance(angularPosition).transform(thrust, thrust);
        acceleration.add(thrust);

        acceleration.div(mass);

        velocity.add(Vector2D.mult(acceleration, DiskSim.timePerFrame));

        float mag = velocity.mag();
        if (mag > 20.0f) {
            velocity.mult(20.0f / mag);
        }

        angularPosition += angularVelocity;
        angularVelocity += angularAcceleration;

        // clip velocity

        // update friction force
        // sum forces
        // update acceleration

    }

    void updateExtentOfMotion() {
        eomList.add(eom.clone());

        SerializableRect2D r1 = new SerializableRect2D(
                position.x - radius, position.y - radius, diameter, diameter);
        Vector2D newPos = Vector2D.add(position, Vector2D.mult(velocity, DiskSim.timePerFrame - currentFrameTime));
        SerializableRect2D r2 = new SerializableRect2D(
                newPos.x - radius, newPos.y - radius, diameter, diameter);

        Rectangle2D bounds = r1.createUnion(r2);
        eom.setRect(bounds);
        holder.update();
    }

    SerializableRect2D getCurrentEOM() {
        return eom;
    }

    SerializableRect2D getObjectBounds() {
        return new SerializableRect2D(
                position.x - radius, position.y - radius, diameter, diameter);
    }

    boolean intersects(DiskModel dm) {
        return dm.position.distance(position) - (dm.radius + radius) < 0.0f;
    }

    void updateCollisionPosition(float newFrameTime) {
        circle.x = position.x - radius;
        circle.y = position.y - radius;
        circleList.add(circle.clone());

        position.add(Vector2D.mult(velocity, newFrameTime - currentFrameTime));
        currentFrameTime = newFrameTime;

    }

    void updateZeroPosition() {
        zeroPosition.set(velocity);
        zeroPosition.mult(-currentFrameTime);
        zeroPosition.add(position);

        circle.x = zeroPosition.x - radius;
        circle.y = zeroPosition.y - radius;
        zcircleList.add(circle.clone());

    }

    public void draw(Graphics2D g) {
        Iterator i;
/*
  g.setColor(Color.blue);
  i = eomList.iterator();
  while(i.hasNext())
  {
    g.draw((SerializableRect2D) i.next() );
    g.setColor(Color.green);
  }
*/
        eomList.clear();


        GeneralPath p = new GeneralPath();
        Ellipse2D.Float f;
        boolean first = true;

        g.setColor(Color.gray);
        i = circleList.iterator();
        while (i.hasNext()) {
            f = (Ellipse2D.Float) i.next();
            //g.draw(f );
            //g.setColor(Color.red);
            if (first)
                p.moveTo(f.x + f.width / 2, f.y + f.height / 2);
            else
                p.lineTo(f.x + f.width / 2, f.y + f.height / 2);
            first = false;
        }

        circleList.clear();
/*
  g.setColor(Color.pink);
  i = zcircleList.iterator();
  while(i.hasNext())
  {
    g.draw((Ellipse2D.Float) i.next() );
    g.setColor(Color.orange);
  }
  */
        zcircleList.clear();


/*  g.translate( position.x-radius, position.y-radius);
  g.scale(0.5,-0.5);
  g.drawString(""+ this ,0.0f,0.0f);
  g.scale(2.0,-2.0);
  g.translate( -(position.x-radius), -(position.y-radius));
*/

        circle.x = position.x - radius;
        circle.y = position.y - radius;

        f = circle;
        if (!first) {
            p.lineTo(f.x + f.width / 2, f.y + f.height / 2);
            g.setColor(Color.green);
            g.draw(p);
        }

        g.setColor(Color.white);
        g.draw(circle);
        //g.draw( eom );

    }

    void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;

        updateExtentOfMotion();
    }

    void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        zeroPosition.set(position);

        updateExtentOfMotion();
    }

    public void processCollision(DiskModel dmB, float time) {
        if (!isActive || !dmB.isActive)
            return;

        DiskModel dmA = this;

        //if( dmB.intersects(dmA) )
        //  return;

        dmA.updateCollisionPosition(time);
        dmB.updateCollisionPosition(time);

        Vector2D collisionAxis = Vector2D.sub(dmA.position, dmB.position);
        collisionAxis.unit();

        float vA1x = dmA.velocity.dot(collisionAxis);
        float vB1x = dmB.velocity.dot(collisionAxis);
        float ma_plus_mb = dmA.mass + dmB.mass;

        float e = 1.0f;
        float ePlusOne = e + 1.0f;

        float vA2x = ((dmA.mass - dmB.mass * e) * vA1x + ePlusOne * dmB.mass * vB1x) / ma_plus_mb;
        float vB2x = ((dmB.mass - dmA.mass * e) * vB1x + ePlusOne * dmA.mass * vA1x) / ma_plus_mb;

        float delta_vAx = vA2x - vA1x;
        float delta_vBx = vB2x - vB1x;
        dmA.velocity.add(Vector2D.mult(collisionAxis, delta_vAx));
        dmB.velocity.add(Vector2D.mult(collisionAxis, delta_vBx));

        dmA.updateExtentOfMotion();
        dmB.updateExtentOfMotion();
        dmA.updateZeroPosition();
        dmB.updateZeroPosition();

        dmB.doExtraCollisionProcessing(dmA, collisionAxis, delta_vBx);
        collisionAxis.neg();
        dmA.doExtraCollisionProcessing(dmB, collisionAxis, delta_vAx);

        //float diff = (dmA.angularVelocity - dmA.angularVelocity )/4;
    }

    public void doExtraCollisionProcessing(DiskModel other, Vector2D collisionAxis, float delta_v) {
        // overridden in subclass for extra affects
    }


    public float getCollisionTime(DiskModel dmB, float frametime) throws NoCollisionInFrame {
        DiskModel dmA = this;

        Vector2D relPos = Vector2D.sub(dmA.position, dmB.position);
        Vector2D relVel = Vector2D.sub(dmA.velocity, dmB.velocity);

        if (relPos.dot(relVel) > 0) {
            throw new NoCollisionInFrame();
        } else if (dmB.intersects(dmA))
            return frametime + 0.000001f;


        float A = dmA.zeroPosition.x - dmB.zeroPosition.x;
        float B = dmA.velocity.x - dmB.velocity.x;
        float C = dmA.zeroPosition.y - dmB.zeroPosition.y;
        float D = dmA.velocity.y - dmB.velocity.y;

        float a = B * B + D * D;
        float b = 2.0f * (A * B + C * D);
        float r = dmA.radius + dmB.radius;
        float c = A * A + C * C - r * r;

        float det = b * b - 4.0f * a * c;

        if (det < 0 || a == 0.0f)
            throw new NoCollisionInFrame();

        float sqrt = (float) Math.sqrt(det);
        float t1 = (-b - sqrt) / (2.0f * a);
        float t2 = (-b + sqrt) / (2.0f * a);
        DiskLog.println("time1 = " + t1);
        DiskLog.println("time2 = " + t2);

        if (t1 < frametime || t1 > DiskSim.timePerFrame) {
            if (t2 < frametime || t2 > DiskSim.timePerFrame) {
                throw new NoCollisionInFrame();
            } else
                return t2;
        } else {
            if (t2 < frametime || t2 > DiskSim.timePerFrame) {
                return t1;
            } else
                return Math.min(t1, t2);
        }

    }


    public void processFrameEnd() {
        //updateCollisionPosition(sim.timePerFrame);
        circle.x = position.x - radius;
        circle.y = position.y - radius;
        circleList.add(circle.clone());

        position.add(Vector2D.mult(velocity, DiskSim.timePerFrame - currentFrameTime));
        zeroPosition.set(position);
        currentFrameTime = 0.0f;
        //events.clear();

        //velocity.y-=0.1f;
/*  float frictiony = 0.005f * ((velocity.y >0.0) ? 1.0f:-1.0f);
  if( frictiony < Math.abs( velocity.y))
    velocity.y-=frictiony;
  else
    velocity.y = 0;
  float frictionx = 0.005f * ((velocity.x >0.0) ? 1.0f:-1.0f);
  if( frictionx < Math.abs(velocity.x))
    velocity.x-=frictionx;
  else
    velocity.x = 0;
*/
        updateExtentOfMotion();

    }

    public SerializableRect2D getBounds() {
        return eom;
    }

    public CollidableObjectHolder getCollidableObjectHolder() {
        return holder;
    }

    public void setCollidableObjectHolder(CollidableObjectHolder holder) {
        this.holder = holder;
    }

    public int getCollisionID() {
        return collisionID;
    }

    public void addEvent(Collision c) {
        //System.out.println("addEvent " + this + " " + c + " size=" +cevents.size() );
        if (!cevents.add(c))
            throw new NullPointerException("Object already had collision reference ");
        //System.out.println("post addEvent " + this + " " + c + " size=" +cevents.size() );
    }

    public void removeEvent(Collision c) {
        //System.out.println("removeEvent " + this + " " + c + " size=" +cevents.size() );
        if (!cevents.remove(c))
            throw new NullPointerException("Object did not contain collision reference ");
        //System.out.println("post removeEvent " + this + " " + c + " size=" +cevents.size() );
    }

    public Iterator getEventIterator() {
        return cevents.iterator();
    }

    public ArrayList getEventListClone() {
        return new ArrayList(cevents);   // don't like this !!!!!!!!!!!!!!!!!
    }

    public float getRadius() {
        return radius;
    }

    void setRadius(float r) {
        radius = r;
        diameter = r * 2.0f;
        circle.width = diameter;
        circle.height = diameter;
        circle.x = position.x - radius;
        circle.y = position.y - radius;
    }

    public void dispose() {
        if (!isActive)
            return;

        Iterator iter = ((ArrayList) forces.clone()).iterator();
        while (iter.hasNext()) {
            ((Force) iter.next()).disconnect();
        }
        forces = null;

        sim.addToRemoveList(this);
        isActive = false;
    }


    public void addForce(Force f) {
        forces.add(f);
    }

    public void removeForce(Force f) {
        forces.remove(f);
    }


    public boolean hasConnection(DiskModel disk) {
        Iterator iter = forces.iterator();
        while (iter.hasNext()) {
            Force f = (Force) iter.next();
            if (!(f instanceof Spring.SpringForce))
                continue;
            Spring.SpringForce item = (Spring.SpringForce) f;
            if (item.getDisk1() == disk || item.getDisk2() == disk)
                return true;
        }
        return false;
    }


}