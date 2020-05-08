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
import java.util.Arrays;

public class AxisEdgeList {

    BoxEdge[] list = new BoxEdge[0];
    CollisionDetector collisionDetector;
    String name;

    public AxisEdgeList(CollisionDetector collisionDetector, String name) {
        this.collisionDetector = collisionDetector;
        this.name = name;
    }

    static void test() {
        AxisEdgeList ael = new AxisEdgeList(null, "test");

        BoxEdge e1;
        BoxEdge e2;
        BoxEdge e3;
        BoxEdge e4;
        BoxEdge e5;
        BoxEdge e6;
        BoxEdge e7;
        BoxEdge e8;

        ael.add(e7 = new BoxEdge(2.0f), e8 = new BoxEdge(1.0f));
        ael.print();
        ael.add(new BoxEdge(3.0f), new BoxEdge(0.0f));
        ael.print();
        ael.add(e3 = new BoxEdge(2.5f), e4 = new BoxEdge(1.5f));
        ael.print();
        ael.add(e6 = new BoxEdge(2.0f), e5 = new BoxEdge(-1.0f));
        ael.print();
        ael.add(e1 = new BoxEdge(4.0f), e2 = new BoxEdge(2.1f));
        ael.print();

        System.out.println("remove");
        ael.remove(e1, e2);
        ael.print();
        ael.remove(e3, e5);
        ael.print();
        ael.remove(e6, e4);
        ael.print();
        ael.remove(e7, e8);
        ael.print();

        System.out.println("e6 =" + e6);
        System.out.println("e7 =" + e7);

    }

    void add(BoxEdge upperEdge, BoxEdge lowwerEdge) {
        //System.out.println(name + " add edge " + upperEdge.value + " " + lowwerEdge.value);
        //print();

        BoxEdge[] newList = new BoxEdge[list.length + 2];
        System.arraycopy(list, 0, newList, 2, list.length);
        newList[0] = lowwerEdge;
        newList[1] = upperEdge;
        list = newList;

        //print();
        sort();        // could be faster
        //print();

        //updateEdge( BoxEdge upperEdge, BoxEdge lowwerEdge );
    }

    //HashSet rs = new HashSet();

    void updateEdge(BoxEdge edge) {

        sort();   // could be faster

    }

    void remove(BoxEdge upperEdge, BoxEdge lowwerEdge) {
  /*
  if( rs.contains(upperEdge) ||  rs.contains(lowwerEdge))
    throw new NullPointerException("rs.contains(upperEdge)");
  rs.add(upperEdge);
  rs.add(lowwerEdge);
  */

        ArrayList l = new ArrayList(Arrays.asList(list));
        l.remove(lowwerEdge);
        l.remove(upperEdge);
        list = (BoxEdge[]) l.toArray(new BoxEdge[list.length - 2]);
/*
  int index1 = Arrays.binarySearch( (Object []) list, (Object) lowwerEdge );
  if( index1 <0 )
    {
    System.out.println("lowwer edge not in list to remove");
    return;
    }

  int index2 = Arrays.binarySearch( (Object []) list, (Object) upperEdge );
  if( index2 <0 )
    {
    System.out.println("upper edge not in list to remove");
    return;
    }

  BoxEdge newList[] = new BoxEdge[list.length-2];

  System.arraycopy(list, 0, newList, 0, index1);
  System.arraycopy(list, index1+1, newList, index1, index2-index1-1 );
  System.arraycopy(list, index2+1, newList, index2-1, list.length - index2 -1 );

  //print();
  list = newList;
  //print();
  */
    }

    void sort() {

        BoxEdge b1;
        BoxEdge b2;

        for (int i = list.length; --i >= 0; ) {
            boolean flipped = false;
            for (int j = 0; j < i; j++) {
                b1 = list[j];
                b2 = list[j + 1];
                if (b1.value > b2.value) {
                    //BoxEdge T = list[j];
                    list[j] = b2;
                    list[j + 1] = b1;
                    flipped = true;
                    b2.processUpCrossing(b1);
                    //System.out.println( name + "processUpCrossing " + i + " " + j + list[j] + list[j+1]);
                }
            }
            if (!flipped) {
                return;
            }
        }

    }

    void print() {
        for (int i = 0; i < list.length; i++)
            DiskLog.println("" + i + "=" + list[i] + "   value = " + ((list[i] == null) ? "null" : ("" + list[i].value)));
        DiskLog.println("-End------------");
    }


}


