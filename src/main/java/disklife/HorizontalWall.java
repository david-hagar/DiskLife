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

public class HorizontalWall extends Wall {

    static int collisionID;

    static {
        collisionID = DiskSim.allocateCollidableID();
    }

    DiskSim sim;
    float x1;
    float y;
    float x2;
    float thickness = 10.0f;
    float halfThickness = 5.0f;
    CollidableObjectHolder holder;

    public HorizontalWall(DiskSim sim, float x1, float x2, float y, float thickness) {
        if (x1 > x2) {
            float x = x1;
            x1 = x2;
            x2 = x;
        }

        this.sim = sim;
        this.x1 = x1;
        this.y = y;
        this.x2 = x2;
        this.thickness = thickness;
        this.halfThickness = thickness / 2.0f;

        updateRect();
    }

    public int getCollisionID() {
        return collisionID;
    }

    void updateRect() {
        rect.width = x2 - x1;
        rect.height = thickness;
        rect.x = x1;
        rect.y = y - halfThickness;
    }

    public CollidableObjectHolder getCollidableObjectHolder() {
        return holder;
    }

    public void setCollidableObjectHolder(CollidableObjectHolder holder) {
        this.holder = holder;
    }

    public void processCollision(DiskModel dm, float time) {
        dm.updateCollisionPosition(time);
        dm.velocity.y *= -1;
        dm.updateExtentOfMotion();
        dm.updateZeroPosition();
    }

    public float getCollisionTime(DiskModel dm, float frametime) throws NoCollisionInFrame {
        float displacement = y - dm.zeroPosition.y;

        if (dm.velocity.y * displacement < 0) {
            throw new NoCollisionInFrame();
        }

        if (dm.velocity.y > 0)
            displacement -= dm.radius + halfThickness;
        else
            displacement += dm.radius + halfThickness;

        float time = displacement / dm.velocity.y;

        DiskLog.println("hw time =" + time);

        if (time < 0) {
            //DiskLog.printlnErr( "ZeroTime");
            return frametime + 0.00001f;
        }

        if (time < frametime || time > DiskSim.timePerFrame)
            throw new NoCollisionInFrame();

        return time;
    }


}