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

public class VerticalWall extends Wall {

    static int collisionID;

    static {
        collisionID = DiskSim.allocateCollidableID();
    }

    float y1;
    float x;
    float y2;
    float thickness = 10.0f;
    float halfThickness = 5.0f;
    DiskSim sim;
    CollidableObjectHolder holder;

    public VerticalWall(DiskSim sim, float y1, float y2, float x, float thickness) {
        if (y1 > y2) {
            float y = y1;
            y1 = y2;
            y2 = y;
        }

        this.sim = sim;
        this.y1 = y1;
        this.x = x;
        this.y2 = y2;
        this.thickness = thickness;
        this.halfThickness = thickness / 2.0f;

        updateRect();
    }

    public int getCollisionID() {
        return collisionID;
    }

    void updateRect() {
        rect.height = y2 - y1;
        rect.width = thickness;
        rect.y = y1;
        rect.x = x - halfThickness;
    }

    public CollidableObjectHolder getCollidableObjectHolder() {
        return holder;
    }

    public void setCollidableObjectHolder(CollidableObjectHolder holder) {
        this.holder = holder;
    }

    public void processCollision(DiskModel dm, float time) {
        dm.updateCollisionPosition(time);
        dm.velocity.x *= -1;
        dm.updateExtentOfMotion();
        dm.updateZeroPosition();

    }

    public float getCollisionTime(DiskModel dm, float frametime) throws NoCollisionInFrame {
        float displacement = x - dm.zeroPosition.x;

        if (dm.velocity.x * displacement < 0) {
            throw new NoCollisionInFrame();
        }

        if (dm.velocity.x > 0) {
            displacement -= dm.radius + halfThickness;
        } else
            displacement += dm.radius + halfThickness;

        float time = displacement / dm.velocity.x;
/*
  float d = Math.abs( x - dm.zeroPosition.x ) - dm.radius + halfThickness;
  if(  d <= 0.0f)
    DiskLog.printlnErr( "Colliding " +d);
*/
        DiskLog.println("vw time =" + time);

        if (time < 0) {
            //DiskLog.printlnErr( "ZeroTime");
            return frametime + 0.00001f;
        }

        if (time < frametime || time > DiskSim.timePerFrame)
            throw new NoCollisionInFrame();

        return time;
    }

}