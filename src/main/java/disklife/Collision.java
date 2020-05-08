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
import disklife.creature.EnergyBlob;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Collision implements Comparable {

    static CollisionHandler[][] chList;
    static private final LinkedList recycleCache = new LinkedList();
    public Collidable c1;
    public Collidable c2;
    public float collisionTime = 1.0e20f;
    public int partialCollisionCount = 0;
    public boolean hasEvent = false;
    private CollisionHandler collisionHandler;


    public Collision(Collidable c1, Collidable c2) {
        if (c1 == c2) {
            System.out.println("collision objects are the same");
            throw new NullPointerException();
        }

        this.c1 = c1;
        this.c2 = c2;
        collisionHandler = chList[c1.getCollisionID()][c2.getCollisionID()]; // not best place

        //diskCollision = (c1 instanceof DiskModel) && (c2 instanceof DiskModel);

        //System.out.println("new partial collision " +c1 + " " +c2 );
    }

    static void initCollisionHandlerList() {
        // load classes
        int i;
        i = DiskModel.collisionID;
        //System.out.println("i = " + i );
        i = HorizontalWall.collisionID;
        //System.out.println("i = " + i );
        i = VerticalWall.collisionID;
        //System.out.println("i = " + i );
        i = EnergyBlob.collisionID;
        //System.out.println("i = " + i );
        i = Creature.collisionID;
        //System.out.println("i = " + i );
        //System.out.println("size = " + DiskSim.getCollidableIDCount() );

        //DiskModel eom = new DiskModel((DiskSim) null);
        //HorizontalWall hw = new HorizontalWall( (DiskSim) null,  0.0f, 0.0f, 0.0f, 0.0f);
        //VerticalWall vw = new VerticalWall( (DiskSim)null, 0.0f, 0.0f, 0.0f, 0.0f);
        //EnergyBlob eb = new EnergyBlob( (DiskSim)null, 0.0f, 0.0f, 0.0f, 0.0f);


        //System.out.println("eom = " + eom.collisionID );
        //System.out.println("hw = " + hw.collisionID );
        //System.out.println("vw = " + vw.collisionID );

        chList = new CollisionHandler[
                DiskSim.getCollidableIDCount()][DiskSim.getCollidableIDCount()];

        chList[EnergyBlob.collisionID][EnergyBlob.collisionID] =
                chList[Creature.collisionID][Creature.collisionID] =
                        chList[DiskModel.collisionID][DiskModel.collisionID] = new CollisionHandler() {
                            public void processCollision(Collidable c1, Collidable c2, float time) {
                                ((DiskModel) c1).processCollision(((DiskModel) c2), time);
                            }

                            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                return ((DiskModel) c1).getCollisionTime(((DiskModel) c2), frametime);
                            }
                        };

        chList[HorizontalWall.collisionID][EnergyBlob.collisionID] =
                chList[HorizontalWall.collisionID][Creature.collisionID] =
                        chList[HorizontalWall.collisionID][DiskModel.collisionID] = new CollisionHandler() {
                            public void processCollision(Collidable c1, Collidable c2, float time) {
                                ((HorizontalWall) c1).processCollision(((DiskModel) c2), time);
                            }

                            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                return ((HorizontalWall) c1).getCollisionTime(((DiskModel) c2), frametime);
                            }
                        };

        chList[VerticalWall.collisionID][EnergyBlob.collisionID] =
                chList[VerticalWall.collisionID][Creature.collisionID] =
                        chList[VerticalWall.collisionID][DiskModel.collisionID] = new CollisionHandler() {
                            public void processCollision(Collidable c1, Collidable c2, float time) {
                                ((VerticalWall) c1).processCollision(((DiskModel) c2), time);
                            }

                            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                return ((VerticalWall) c1).getCollisionTime(((DiskModel) c2), frametime);
                            }
                        };

        chList[EnergyBlob.collisionID][HorizontalWall.collisionID] =
                chList[Creature.collisionID][HorizontalWall.collisionID] =
                        chList[DiskModel.collisionID][HorizontalWall.collisionID] = new CollisionHandler() {
                            public void processCollision(Collidable c1, Collidable c2, float time) {
                                ((HorizontalWall) c2).processCollision(((DiskModel) c1), time);
                            }

                            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                return ((HorizontalWall) c2).getCollisionTime(((DiskModel) c1), frametime);
                            }
                        };

        chList[EnergyBlob.collisionID][VerticalWall.collisionID] =
                chList[Creature.collisionID][VerticalWall.collisionID] =
                        chList[DiskModel.collisionID][VerticalWall.collisionID] = new CollisionHandler() {
                            public void processCollision(Collidable c1, Collidable c2, float time) {
                                ((VerticalWall) c2).processCollision(((DiskModel) c1), time);
                            }

                            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                return ((VerticalWall) c2).getCollisionTime(((DiskModel) c1), frametime);
                            }

                        };

        chList[EnergyBlob.collisionID][Creature.collisionID] = new CollisionHandler() {
            public void processCollision(Collidable c1, Collidable c2, float time) {
                ((EnergyBlob) c1).processCollision((Creature) c2, time);
            }

            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                return ((DiskModel) c2).getCollisionTime(((DiskModel) c1), frametime);
            }

        };

        chList[Creature.collisionID][EnergyBlob.collisionID] = new CollisionHandler() {
            public void processCollision(Collidable c1, Collidable c2, float time) {
                ((EnergyBlob) c2).processCollision((Creature) c1, time);
            }

            public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                return ((DiskModel) c1).getCollisionTime(((DiskModel) c2), frametime);
            }

        };


        chList[HorizontalWall.collisionID][HorizontalWall.collisionID] =
                chList[VerticalWall.collisionID][VerticalWall.collisionID] =
                        chList[VerticalWall.collisionID][HorizontalWall.collisionID] =
                                chList[HorizontalWall.collisionID][VerticalWall.collisionID] = new CollisionHandler() {
                                    public void processCollision(Collidable c1, Collidable c2, float time) {
                                        throw new NullPointerException("Wall collsion" + c1 + c2);
                                    }

                                    public float getCollisionTime(Collidable c1, Collidable c2, float frametime) throws NoCollisionInFrame {
                                        throw new NullPointerException("Wall collsion" + c1 + c2);
                                    }
                                };


    }

    static void recycleCollision1(Collision c) {
        recycleCache.addFirst(c);

    }

    static Collision getRecycledCollision1(Collidable c1, Collidable c2) {
        try {
            Collision c = (Collision) recycleCache.removeFirst();
            c.c1 = c1;
            c.c2 = c2;
            c.partialCollisionCount = 0;
            c.hasEvent = false;
            return c;
        } catch (NoSuchElementException e) {
            return new Collision(c1, c2);
        }

    }

    public int compareTo(Object o) {
        Collision otherCollision = (Collision) o;

        if (collisionTime == otherCollision.collisionTime)
            return 0;

        return (collisionTime < otherCollision.collisionTime) ? -1 : 1;
    }

    void print() {
        System.out.println("c1 = " + c1 + "c2 = " + c2 + "pccount" + partialCollisionCount);
    }

    public void processCollision(float time) {
        //print();

        collisionHandler.processCollision(c1, c2, time);
    }

    public float getCollisionTime(float frametime) throws NoCollisionInFrame {
        if (collisionHandler == null) {
            System.out.println("nullch " + c1.getCollisionID() + " " + c2.getCollisionID());
            System.out.println(" " + c1 + " " + c2);
        }
        return collisionHandler.getCollisionTime(c1, c2, frametime);
    }

    public void addEventReferenceToObjects() {
        collisionHandler = chList[c1.getCollisionID()][c2.getCollisionID()]; // not best place
        c1.addEvent(this);
        c2.addEvent(this);
    }

    public void removeEventReferenceToObjects() {
        c1.removeEvent(this);
        c2.removeEvent(this);
    }


}


