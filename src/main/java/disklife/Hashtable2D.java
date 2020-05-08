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

import java.util.Hashtable;
import java.util.Iterator;

public class Hashtable2D {

    final Hashtable levelOne = new Hashtable();
    final Hashtable levelTwo = new Hashtable();
    public Hashtable2D() {
    }

    static void test() {
        Hashtable2D table = new Hashtable2D();

        table.print();
        table.add(new Integer(1), new Integer(2), "Value 1");
        table.add(new Integer(5), new Integer(8), "Value 2");
        table.add(new Integer(3), new Integer(1), "Value 3");
        table.add(new Integer(3), new Integer(9), "Value 4");
        table.add(new Integer(8), new Integer(4), "Value 5");
        table.print();

        System.out.println("get=" + table.get(new Integer(1), new Integer(2)));
        System.out.println("get?=" + table.get(new Integer(10), new Integer(2)));


        table.remove(new Integer(5), new Integer(8));
        table.print();
    }

    void add(Object key1, Object key2, Object obj) {
        Hashtable lev2 = (Hashtable) levelOne.get(key1);
        if (lev2 == null) {
            lev2 = new Hashtable();
            levelOne.put(key1, lev2);
        }
        lev2.put(key2, obj);
    }

    void remove(Object key1, Object key2) {
        Hashtable lev2 = (Hashtable) levelOne.get(key1);
        if (lev2 == null) {
            System.out.println("key 1 not found in hashtable on remove");
            throw new NullPointerException("key 1 not found in hashtable on remove");
            //return;
        }

        lev2.remove(key2);
        if (lev2.isEmpty())
            levelOne.remove(key1);

    }


    Object get(Object key1, Object key2) {
        Hashtable lev2 = (Hashtable) levelOne.get(key1);
        if (lev2 == null) {
            //System.out.println("key 1 not found in hashtable get");
            return null;
        }

        return lev2.get(key2);
    }


    Iterator iterator() {
        return new H2DIterator();
    }

    void print() {
        System.out.println("Start");
        Iterator i = iterator();
        while (i.hasNext()) {
            Object o = i.next();
            System.out.println("obj=" + o);
        }

        System.out.println("Finish");
    }

    private class H2DIterator implements Iterator {
        Iterator levelOneIterator = levelOne.values().iterator();
        Iterator levelTwoIterator = levelTwo.values().iterator();

        H2DIterator() {
        }

        public boolean hasNext() {
            if (levelTwoIterator.hasNext())
                return true;
            else
                return levelOneIterator.hasNext();
        }

        public Object next() {
            if (!levelTwoIterator.hasNext())
                levelTwoIterator = ((Hashtable) levelOneIterator.next()).values().iterator();

            return levelTwoIterator.next();
        }

        public void remove() {
        }

    }


}
  
  