package disklife;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) </p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class Spring {

    public float breakForce = 15.0f;
    DiskModel disk1 = null;
    DiskModel disk2 = null;
    SpringForce force1 = new SpringForce();
    SpringForce force2 = new SpringForce();
    DiskSim diskSim = null;
    float idealDistance;
    float springConstant;
    boolean isActivated = false;

    public Spring(DiskModel disk1, DiskModel disk2, DiskSim diskSim,
                  float idealDistance, float springConstant) {
        this.disk1 = disk1;
        this.disk2 = disk2;
        this.diskSim = diskSim;
        this.idealDistance = idealDistance;
        this.springConstant = springConstant;

        disk1.addForce(force1);
        disk2.addForce(force2);
    }

    // end internal class

    public void update() {
        Vector2D d = Vector2D.sub(disk2.position, disk1.position);
        float mag = (d.mag() - idealDistance) * springConstant;
        if (!isActivated) {
            if (mag > 0)
                isActivated = true;
            else
                mag /= 5.0f;
        }

        if (mag > breakForce) {
            dispose();
            return;
        }

  /*
  if( mag > 0.0f )
    mag*=mag * springConstant /20.0f;
  else
    mag*= - mag * springConstant /20.0f;
  */
        d.unit();
        d.mult(mag);

        force1.force.set(d);
        d.neg();
        force2.force.set(d);
    }

    public void dispose() {
        if (disk1 == null)
            return;

        disk1.removeForce(force1);
        disk2.removeForce(force2);
        diskSim.removeSpring(this);
        disk1 = disk2 = null;
        force1 = force2 = null;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.cyan);
        g.draw(new Line2D.Float(disk1.position, disk2.position));
    }

    public class SpringForce implements Force {
        Vector2D force = new Vector2D();

        public SpringForce() {

        }

        public Vector2D getForce() {
            return force;
        }

        public void disconnect() {
            dispose();
        }

        public DiskModel getDisk1() {
            return disk1;
        }

        public DiskModel getDisk2() {
            return disk2;
        }

    }


}