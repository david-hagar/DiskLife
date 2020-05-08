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

import disklife.creature.Creature;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DiskSim {

    static final float timePerFrame = 1.0f;
    static int collidableIDCount = 0;
    double systemTime = 0.0;
    Integer repaintMutex = new Integer(1);
    boolean antiAlias = false;
    private final ArrayList disks = new ArrayList();
    private final ArrayList walls = new ArrayList();
    private final ArrayList springs = new ArrayList();
    private final DiskCollider diskCollider = new DiskCollider();
    private final HashSet removeList = new HashSet();
    private final ArrayList addList = new ArrayList();
    private final Rectangle2D.Float bounds = new Rectangle2D.Float(-110.0f, -110.0f, 220.0f, 220.0f);

    public DiskSim() {
        Collision.initCollisionHandlerList();

        try {
            Wall w;
            w = new VerticalWall(this, -100.0f, 100.0f, 100.5001f, 1.0f);
            addwall(w);
            w = new VerticalWall(this, -100.0f, 100.0f, -100.5001f, 1.0f);
            addwall(w);
            w = new HorizontalWall(this, -100.0f, 100.0f, 100.5001f, 1.0f);
            addwall(w);
            w = new HorizontalWall(this, -100.0f, 100.0f, -100.5001f, 1.0f);
            addwall(w);

        } catch (CollisionException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //makeGrid();
/*
  try
  {
  DiskModel dm1 = new DiskModel(this,5.0f, new Vector2D( -30.1f,30.0f), new Vector2D(-1.0f, -1.1f));
  dm1.angularVelocity = 0.01f;
  addDiskModel(dm1);

  DiskModel dm2 = new DiskModel(this,3.0f, new Vector2D( -30.0f,10.1f), new Vector2D(-1.1f, -1.0f));
  addDiskModel(dm2);

  Spring s = new Spring( dm1, dm2, this, 20.0f, 10.0f );
  addSpring(s);

  DiskModel dm3 = new DiskModel(this,2.0f, new Vector2D( -10.0f,10.0f), new Vector2D( 1.0f, -1.0f));
  addDiskModel(dm3);

  s = new Spring( dm2, dm3, this, 20.0f, 10.0f );
  addSpring(s);

  s = new Spring( dm1, dm3, this, 20.0f, 10.0f );
  addSpring(s);

  DiskModel dm4 = new DiskModel(this,3.0f, new Vector2D( -0.0f,-0.0f), new Vector2D(2.0f, -2.0f));
  addDiskModel(dm4);

  s = new Spring( dm4, dm3, this, 30.0f, 10.0f );
  addSpring(s);

  s = new Spring( dm4, dm2, this, 30.0f, 10.0f );
  addSpring(s);

  DiskModel dm5 = new DiskModel(this,3.0f, new Vector2D( -50.0f, 50.0f), new Vector2D(2.0f, -1.0f));
  addDiskModel(dm5);

  DiskModel dm6 = new DiskModel(this,1.0f, new Vector2D( -80.0f, 80.0f), new Vector2D(2.0f, -1.0f));
  addDiskModel(dm6);

  DiskModel dm7 = new DiskModel(this,1.0f, new Vector2D( -80.0f, -50.0f), new Vector2D(2.0f, -1.0f));
  addDiskModel(dm7);


  }catch(CollisionException e)
  {
  e.printStackTrace();
  System.exit(0);
  }
*/
  /*
  for(int i=0;i<50;i++)
     try
     {
     addDiskModel(disklife.creature.Creature.newRandom(this, 1.5, 1.0, 4.0));
     }
     catch(CollisionException e)
    {
    i--;
    }
  */

        for (int i = 0; i < 10; i++)
            try {
                DiskModel c1 = Creature.newRandom(this, 1.5, 1.0, 2.0);
                addDiskModel(c1);
                Creature c2 = new Creature(this, c1.getRadius(),
                        Vector2D.add(c1.position, new Vector2D(0.0f, 20.0f)), new Vector2D(c1.velocity));
                addDiskModel(c2);
                Spring s = new Spring(c1, c2, this, 20.0f, 10.0f);
                addSpring(s);
            } catch (CollisionException e) {
                i--;
            }


        //removeDiskModel( (DiskModel) disks.get(5) );

        //diskCollider.printPartialCollisions();
        //diskCollider.printCollisions();
        //diskCollider.printCalcCollisions();
    }

    static AffineTransform calcBoundsTF(Dimension viewWin, Rectangle.Double bounds) {
        AffineTransform tf = new AffineTransform();

        double boundsAspect = bounds.height / bounds.width;
        double windowAspect = (double) viewWin.height / viewWin.width;

        double scale;
        if (windowAspect > boundsAspect)
            scale = viewWin.width / bounds.width;
        else
            scale = viewWin.height / bounds.height;

        scale *= 0.9;

        double centerX = bounds.x + bounds.width / 2.0;
        double centerY = bounds.y + bounds.height / 2.0;

        tf.translate(viewWin.width / 2, viewWin.height / 2);
        tf.scale(1.0, -1.0);
        tf.scale(scale, scale);
        tf.translate(-centerX, -centerY);

        return tf;
    }

    static public int allocateCollidableID() {
        return collidableIDCount++;
    }

    static public int getCollidableIDCount() {
        return collidableIDCount;
    }

    public void checkCollision(DiskModel dm) throws CollisionException {
        SerializableRect2D bounds = dm.getObjectBounds();
        Iterator i = disks.iterator();
        while (i.hasNext()) {
            DiskModel d = (DiskModel) i.next();
            if (dm.intersects(d) && !removeList.contains(d))
                throw new CollisionException();
        }

        i = walls.iterator();
        while (i.hasNext()) {
            Wall w = (Wall) i.next();
            if (bounds.intersects(w.getBounds()))
                throw new CollisionException();
        }

        i = addList.iterator();
        while (i.hasNext()) {
            DiskModel d = (DiskModel) i.next();
            if (dm.intersects(d))
                throw new CollisionException();
        }

    }

    private void addDiskModel(DiskModel dm) throws CollisionException {
        checkCollision(dm);
        diskCollider.add(dm);
        disks.add(dm);
    }

    private void remove(DiskModel dm) {
        int index = disks.indexOf(dm);
        if (index == -1)
            throw new NullPointerException("tried to remove nonexistent disk model");
        disks.remove(index);
        diskCollider.remove(dm);

    }

    public void addToRemoveList(DiskModel dm) {
        removeList.add(dm);
    }

    public void addToAddList(DiskModel dm) throws CollisionException {
        checkCollision(dm);
        addList.add(dm);
    }

    void addwall(Wall wall) throws CollisionException {
        diskCollider.add(wall);
        walls.add(wall);
    }

    boolean checkForOverlap() {
        boolean retV = false;

        int size = disks.size();
        for (int i = 0; i < size - 1; i++) {
            DiskModel obj1 = (DiskModel) disks.get(i);
            for (int j = i + 1; j < size; j++) {
                DiskModel obj2 = (DiskModel) disks.get(j);
                //System.out.println( "here" );
                if (obj1.intersects(obj2)) {
                    DiskLog.printlnErr("overlap " + obj1 + " " + obj2);
                    retV = true;
                }
            }
        }

        return retV;
    }

    void draw(JPanel jPanel) {
        draw((Graphics2D) jPanel.getGraphics(), jPanel.getSize());
    }

    void draw(Graphics2D g, JPanel jPanel) {
        draw(g, jPanel.getSize());
    }

    void draw(Graphics2D g, Dimension viewWin) {
        synchronized (this) {
            // ElapsTimer t = new ElapsTimer();
            if (antiAlias) {
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(0.5f));
            } else
                g.setStroke(new BasicStroke(0.0f));

            //g.setColor(Color.black);
            //Rectangle.Double rectangle= new Rectangle.Double(
            //           0.0, 0.0, (double)viewWin.width -1, (double)viewWin.height -1);
            //g.fill(rectangle);

            Rectangle.Double bounds = new Rectangle.Double(-100.0, -100.0, 200.0, 200.0);
            AffineTransform tf = calcBoundsTF(viewWin, bounds);
            g.transform(tf);
            //g.setColor(Color.blue);
            //g.draw(bounds);

            //g.scale(6.0,6.0);
            //g.translate( 100.0, 0);
            Iterator i = disks.iterator();
            while (i.hasNext()) {
                ((DiskModel) i.next()).draw(g);
            }

            i = walls.iterator();
            while (i.hasNext()) {
                ((Wall) i.next()).draw(g);
            }

            i = springs.iterator();
            while (i.hasNext()) {
                ((Spring) i.next()).draw(g);
            }

            //t.stopAndPrint("draw");

        }
        synchronized (repaintMutex) {
            repaintMutex.notify();
        }

    }

    void makeGrid() {
        boolean toggle = false;

        for (float x = -80; x < 80; x += 20)
            for (float y = -60; y < 90; y += 20)
//  for(float x=-20 ; x<20 ; x+=20 )
//  for(float y=-20 ; y<30 ; y+=40 )
            {
                try {
                    DiskModel dm;
                    if (toggle)
                        //dm= new DiskModel(this, 9.0f, new Vector2D( x,y ), new Vector2D( 0.0f, 0.0f));
                        dm = new DiskModel(this, (float) (Math.abs(x) / 10.0 + 2.0), new Vector2D(x, y), new Vector2D(0.0f, 0.0f));
                    else
                        //dm= new DiskModel(this,9.0f, new Vector2D( x+10.0f,y ), new Vector2D( 0.0f, 0.0f));
                        dm = new DiskModel(this, (float) (Math.abs(x) / 10.0 + 2.0), new Vector2D(x + 10.0f, y), new Vector2D(0.0f, 0.0f));

                    addDiskModel(dm);

                    toggle = !toggle;
                } catch (CollisionException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }

        try {
            DiskModel dm = new DiskModel(this, 5.0f, new Vector2D(0.0f, -90.0f), new Vector2D(0.5f, 40.0f));
            addDiskModel(dm);
        } catch (CollisionException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    void runOneFrame() {
        synchronized (this) {
            //ElapsTimer t = new ElapsTimer();

            DiskLog.println("");

            diskCollider.processOneFrame();


            Iterator i = removeList.iterator();
            while (i.hasNext()) {
                remove((DiskModel) i.next());
            }
            removeList.clear();

            doBoundsCheck();
            updateSprings();

            i = disks.iterator();
            while (i.hasNext()) {
                ((DiskModel) i.next()).runTimeStep();
            }


            i = addList.iterator();
            while (i.hasNext()) {
                //try{
                DiskModel dm = (DiskModel) i.next();
                diskCollider.add(dm);
                disks.add(dm);
                DiskLog.println("Added OK ________________________");
                //}
                //catch(CollisionException e)
                //{DiskLog.printlnErr("Got collision on final add for add list");}
            }
            addList.clear();

            //t.stopAndPrint("collision");


            systemTime += timePerFrame;
        }

    }


    public void addSpring(Spring spring) {
        springs.add(spring);
    }

    public void removeSpring(Spring spring) {
        springs.remove(spring);
    }

    public void updateSprings() {
        Iterator iter = ((ArrayList) springs.clone()).iterator();
        while (iter.hasNext()) {
            ((Spring) iter.next()).update();
        }

    }


    private void doBoundsCheck() {
        Iterator iter = disks.iterator();
        while (iter.hasNext()) {
            DiskModel item = (DiskModel) iter.next();

            if (!bounds.contains(item.position)) {
                item.dispose();
            }
        }

    }


}