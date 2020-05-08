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

import java.util.ArrayList;
import java.util.Iterator;

public abstract class CollisionDetector {

    ArrayList objects = new ArrayList();
    private final Hashtable2D partialCollisions = new Hashtable2D();
    private final AxisEdgeList xAxisEdgeList = new AxisEdgeList(this, "x axis");
    private final AxisEdgeList yAxisEdgeList = new AxisEdgeList(this, "y axis");


    public CollisionDetector() {
    }

    void add(Collidable obj) {
        CollidableObjectHolder holder = new CollidableObjectHolder(obj, this);
        obj.setCollidableObjectHolder(holder);

        xAxisEdgeList.add(holder.xUpperEdge, holder.xLowwerEdge);
        yAxisEdgeList.add(holder.yUpperEdge, holder.yLowwerEdge);

        objects.add(obj);

    }

    void remove(Collidable obj) {
        int index = objects.indexOf(obj);
        if (index == -1)
            throw new NullPointerException("tried to remove nonexistent Collidable");
        objects.remove(index);

        CollidableObjectHolder holder = obj.getCollidableObjectHolder();
        obj.setCollidableObjectHolder(null);

        xAxisEdgeList.remove(holder.xUpperEdge, holder.xLowwerEdge);
        yAxisEdgeList.remove(holder.yUpperEdge, holder.yLowwerEdge);


        // remove full collisions
        Iterator i = obj.getEventListClone().iterator();
        while (i.hasNext()) {
            Collision c = ((Collision) i.next());
            partialCollisions.remove(c.c1, c.c2);
            removeCollision(c);
        }

        // find partial collisions
        ArrayList toRemove = new ArrayList();
        i = partialCollisions.iterator();
        while (i.hasNext()) {
            Collision c = ((Collision) i.next());
            if (c.c1 == obj || c.c2 == obj)
                toRemove.add(c);
        }

        // remove partial Collisions
        i = toRemove.iterator();
        while (i.hasNext()) {
            Collision c = ((Collision) i.next());
            partialCollisions.remove(c.c1, c.c2);
        }


    }


    void addPartialCollision(Collidable obj1, Collidable obj2) {
        //System.out.println("addPartialCollision" + obj1 + " " + obj2 );

        if (obj1.hashCode() > obj2.hashCode()) {
            Collidable t = obj1;
            obj1 = obj2;
            obj2 = t;
        }

        Collision c = (Collision) partialCollisions.get(obj1, obj2);
        if (c == null) {
            //c = Collision.getRecycledCollision(obj1, obj2);
            c = new Collision(obj1, obj2);
            partialCollisions.add(obj1, obj2, c);
        }

        c.partialCollisionCount++;
        if (c.partialCollisionCount == 2) {
            addCollision(c);
        }

    }

    void removePartialCollision(Collidable obj1, Collidable obj2) {
        //System.out.println("removePartialCollision" + obj1 + " " + obj2 );
        if (obj1.hashCode() > obj2.hashCode()) {
            Collidable t = obj1;
            obj1 = obj2;
            obj2 = t;
        }

        Collision c = (Collision) partialCollisions.get(obj1, obj2);
        if (c == null) {
            System.out.println("tried to remove collision that was not in table");
            return;
        }

        if (c.partialCollisionCount == 1) {
            partialCollisions.remove(obj1, obj2);
            //Collision.recycleCollision(c);
            //c.c1=null;
            //c.c2=null;
        } else if (c.partialCollisionCount == 2) {
            removeCollision(c);
            c.partialCollisionCount--;
        } else
            throw new NullPointerException("partialCollisionCount = " + c.partialCollisionCount);
    }


    void updateCollisions() {
        //System.out.println("Axis Before");
        // xAxisEdgeList.print();
        //yAxisEdgeList.print();

        xAxisEdgeList.sort();
        yAxisEdgeList.sort();

        // System.out.println("Axis After");
        // xAxisEdgeList.print();
        // yAxisEdgeList.print();
    }

/*
  void updateCollisionsOld()
  {
  clearCollisions();

  int size = objects.size();
  for( int i=0; i<size-1 ;i++)
    {
    Collidable obj1 = (Collidable)objects.get(i);
    SerializableRect2D bounds1 = obj1.getBounds();
    for( int j=i+1; j<size; j++)
      {
      Collidable obj2 = (Collidable)objects.get(j);
      //if( bounds1.intersects( obj2.getBounds() )  )
      //   addCollision(obj1, obj2);

      }
    }

  }
*/

    void printCalcCollisions() {
        System.out.println("printCalcCollisions");
        int size = objects.size();
        for (int i = 0; i < size - 1; i++) {
            Collidable obj1 = (Collidable) objects.get(i);
            SerializableRect2D bounds1 = obj1.getBounds();
            for (int j = i + 1; j < size; j++) {
                Collidable obj2 = (Collidable) objects.get(j);
                if (bounds1.intersects(obj2.getBounds())) {
                    System.out.println("collision found " + obj1 + " " + obj2);
                }

            }
        }

    }


    void updateCollision(Collision c) {
        // could be faster
        xAxisEdgeList.sort();
        yAxisEdgeList.sort();
    }

    void printPartialCollisions() {
        System.out.println("printPartialCollisions");
        Iterator i = partialCollisions.iterator();
        while (i.hasNext()) {
            ((Collision) i.next()).print();
        }

    }

    abstract void addCollision(Collision c);

    abstract void removeCollision(Collision c);


}