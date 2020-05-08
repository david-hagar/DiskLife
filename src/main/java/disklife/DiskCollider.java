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

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DiskCollider extends CollisionDetector {

    boolean updateMode = false;
    BinaryHeap eventQueue = new BinaryHeap();
    ElapsTimer t = new ElapsTimer(System.out, 20);
    //Hashtable2D oldCollisions = new Hashtable2D();
    private final Hashtable2D collisionsTable = new Hashtable2D();
    private float currentFrameTime = 0.0f;
    private Collision currentCollision = null;

    public DiskCollider() {
    }

    void addCollision(Collision c) {
        DiskLog.println("new full collision " + c + " " + c.c1 + " " + c.c2);
        collisionsTable.add(c.c1, c.c2, c);
        c.addEventReferenceToObjects();

        if (updateMode) {
            calcOneEvent(c);
        }

    }


    void removeCollision(Collision c) {
        DiskLog.println("remove full collision " + c + " " + c.c1 + " " + c.c2);
        collisionsTable.remove(c.c1, c.c2);
        c.removeEventReferenceToObjects();
        if (updateMode) {
            removeFromEventQueue(c);
        }

    }


    void addToEventQueue(Collision c) {
        if (!c.hasEvent) {
            eventQueue.add(c);
            c.hasEvent = true;
        } else {
            DiskLog.println("tried to add duplicate event to queue " + c + " " + c.c1 + " " + c.c2);
        }
    }

    void removeFromEventQueue(Collision c) {
        try {
            if (c.hasEvent) {
                eventQueue.remove(c);
                c.hasEvent = false;
            }
        } catch (NoSuchElementException e) {
            DiskLog.printlnErr("### tried to remove event that was supposed to be there" +
                    c + " " + c.c1 + " " + c.c2);
        }


    }


    void printCollisions() {
        System.out.println("printCollisions");

        Iterator i = collisionsTable.iterator();
        while (i.hasNext()) {
            ((Collision) i.next()).print();
        }

    }


    void checkCalcCollisions() {

        DiskLog.println("checkCalcCollisions");
        HashSet set = new HashSet();

        int count = 0;
        int size = objects.size();
        for (int i = 0; i < size - 1; i++) {
            Collidable obj1 = (Collidable) objects.get(i);
            SerializableRect2D bounds1 = obj1.getBounds();
            for (int j = i + 1; j < size; j++) {
                Collidable obj2 = (Collidable) objects.get(j);
                if (bounds1.intersects(obj2.getBounds())) {
                    count++;
                    Collision c;
                    if (obj1.hashCode() > obj2.hashCode())
                        c = (Collision) collisionsTable.get(obj2, obj1);
                    else
                        c = (Collision) collisionsTable.get(obj1, obj2);

                    if (c == null) {
                        DiskLog.printlnErr("#######  error, collision not found " + obj1 + " " + obj2);
                        DiskLog.printlnErr("1=" + bounds1);
                        DiskLog.printlnErr("2=" + obj2.getBounds());
                        Rectangle2D r = bounds1.createIntersection(obj2.getBounds());
                        DiskLog.printlnErr("i=" + r);
                        if (r.getWidth() > 0.00001 && r.getHeight() > 0.00001)
                            DiskLog.printlnErr("#### Big ####");
                    } else
                        set.add(c);
                }

            }
        }

        int count2 = 0;
        Iterator i = collisionsTable.iterator();
        while (i.hasNext()) {
            Collision c = (Collision) i.next();
            count2++;
            if (!set.contains(c)) {
                DiskLog.printlnErr("#######  error, extra collision" + c + " " + c.c1 + " " + c.c2);
                DiskLog.printlnErr("1=" + c.c1.getBounds());
                DiskLog.printlnErr("2=" + c.c2.getBounds());
                Rectangle2D r = c.c1.getBounds().createIntersection(c.c2.getBounds().getBounds());
                DiskLog.printlnErr("i=" + r);
                if (r.getWidth() > 0.00001 && r.getHeight() > 0.00001)
                    DiskLog.printlnErr("#### Big ####");
                DiskLog.printlnErr("intersects=" + c.c1.getBounds().intersects(c.c2.getBounds().getBounds()));
                DiskLog.printlnErr("intersects=" + c.c2.getBounds().intersects(c.c1.getBounds().getBounds()));

            }
        }

        //if( count != count2 )
        //  System.out.println("############ error, counts don't match " + count + " " + count2);

    }


    void removeObjectEvents(Collidable obj) {
        DiskLog.println("removeObjectEvents start" + obj);
        Iterator i = obj.getEventIterator();
        while (i.hasNext()) {
            Collision collisionEvent = (Collision) i.next();
            if (currentCollision != collisionEvent) {
                DiskLog.println("removeObjectEvents " + collisionEvent + " " + collisionEvent.c1 + " " + collisionEvent.c2);
                removeFromEventQueue(collisionEvent);
            } else {
                DiskLog.println("removeObjectEvents skipped current " + collisionEvent + " " + collisionEvent.c1 + " " + collisionEvent.c2);
            }
        }

    }


    void addObjectEvents(Collidable obj) {
        DiskLog.println("addObjectEvents start" + obj);

        Iterator i = obj.getEventIterator();
        while (i.hasNext()) {
            Collision collisionEvent = (Collision) i.next();
            if (currentCollision != collisionEvent) {
                DiskLog.println("addObjectEvents " + collisionEvent + " " + collisionEvent.c1 + " " + collisionEvent.c2);
                calcOneEvent(collisionEvent);
            } else {
                DiskLog.println("addObjectEvents skipped current " + collisionEvent + " " + collisionEvent.c1 + " " + collisionEvent.c2);
            }

        }

    }


    void calcOneEvent(Collision c) {
        if (c == currentCollision)
            return;

        try {
            c.collisionTime = c.getCollisionTime(currentFrameTime);
            addToEventQueue(c);

            DiskLog.println("added event time = " + c + " " + c.collisionTime + " " + c.c1 + " " + c.c2);
        } catch (NoCollisionInFrame e) {
            DiskLog.println("disguarded event " + c + " " + c.c1 + " " + c.c2);
        }

    }


    void calcEvents() {

        Iterator i = collisionsTable.iterator();
        while (i.hasNext()) {
            Collision c;

            calcOneEvent(c = (Collision) i.next());
            DiskLog.println("initial calc event " + c + " " + c.c1 + " " + c.c2);
        }

    }


    void processEvents() {
        Collision lastCollision = null;

        updateMode = true;
        while (!eventQueue.isEmpty()) {
            Collision c = currentCollision = ((Collision) eventQueue.deleteMin());
            c.hasEvent = false;
            DiskLog.println("event collisionTime = " + c.collisionTime + " " + c);
            if (c == lastCollision) {
                DiskLog.printlnErr("skipping same collision " + c);
                continue;
            }
            currentFrameTime = c.collisionTime;


            Collidable c1 = c.c1;
            Collidable c2 = c.c2;
            removeObjectEvents(c1);
            removeObjectEvents(c2);
            c.processCollision(currentFrameTime);
            updateCollision(c);
            addObjectEvents(c1);
            addObjectEvents(c2);
            //checkCalcCollisions();

            lastCollision = c;
        }
        updateMode = false;

    }


    void processFrameEnd() {
        Iterator i = objects.iterator();
        while (i.hasNext()) {
            ((Collidable) i.next()).processFrameEnd();
        }
    }


    void processOneFrame() {
        currentFrameTime = 0.0f;
        currentCollision = null;

        //oldCollisions = collisionsTable;
        //collisionsTable = new Hashtable2D();

        //updateEOM();

        t.start();
        updateCollisions();
        //checkCalcCollisions();

        calcEvents();
        processEvents();
        currentFrameTime = 1.0f;
        processFrameEnd();
        t.stopAndPrint("collision");

    }

}