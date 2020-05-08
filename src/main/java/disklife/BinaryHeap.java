package disklife;

import java.util.NoSuchElementException;

// BinaryHeap class
//
// CONSTRUCTION: with optional capacity (that defaults to 100)
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( )  --> Return smallest item
// boolean isEmpty( )     --> Return true if empty; else false
// boolean isFull( )      --> Return true if full; else false
// void makeEmpty( )      --> Remove all items
// ******************ERRORS********************************
// Throws Overflow if capacity exceeded

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class BinaryHeap implements Cloneable {
    //HashSet set = new HashSet();

    private static final int DEFAULT_CAPACITY = 100;
    private static final int ABSOLUTE_CAPACITY = 10000000; // 10mil
    private int currentSize;      // Number of elements in heap
    private Comparable[] array; // The heap array

    /**
     * Construct the binary heap.
     */
    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Construct the binary heap.
     *
     * @param capacity the capacity of the binary heap.
     */
    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = new Comparable[capacity + 1];
    }

    public void verifyArrayOld() {
        /*
         for(int i =1; i<=currentSize; i++)
         {
         if( ! set.contains( array[i] ) )
            System.out.println( "###### extra element" + array[i] + "index="+ i );
         // System.out.println( "h=" + array[i] );
         }
         */
/*
  Iterator i = set.iterator();
  while(i.hasNext())
  {
    System.out.println( "s=" + i.next() );
  }
*/
         /*
         BinaryHeap h;
         try {  h = (BinaryHeap) this.clone(); }
         catch( CloneNotSupportedException e ) { e.printStackTrace(); return;}
          while( !h.isEmpty() )
          {
          if( ! set.contains( h.findMin() ) )
            System.out.println( "###### extra element " + h.findMin() );
          System.out.println( "h=" + h.deleteMin() );
          }
          */
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     *
     * @param x the item to insert.
     * @throws Overflow if container is full.
     */
    public void add(Comparable x) {
        //set.add(x);
        //System.out.println( "-- add " + x  );
        //if( isFull( ) )
        //    throw new Overflow( );
        resize();

        // Percolate up
        int hole = ++currentSize;
        for (; hole > 1 && x.compareTo(array[hole / 2]) < 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;

        //verifyArray( );
    }

    public void remove(Comparable x) throws NoSuchElementException {
        //System.out.println( "-- remove " + x  );
        //if( set.contains(x) )
        //   set.remove(x);
        //else
        //   System.out.println( "###### tried to remove nonexistent element" + x );

        for (int i = 1; i <= currentSize; i++)
            if (array[i] == x) {
                array[i] = array[currentSize--];
                percolateDown(i);
                //verifyArray( );
                return;
            }

        System.out.println("### didn't find element to remove" + x);
        throw new NoSuchElementException();
    }

    /**
     * Find the smallest item in the priority queue.
     *
     * @return the smallest item, or null, if empty.
     */
    public Comparable findMin() {
        if (isEmpty())
            return null;
        return array[1];
    }

    /**
     * Remove the smallest item from the priority queue.
     *
     * @return the smallest item, or null, if empty.
     */
    public Comparable deleteMin() {
        //System.out.println( "-- deleteMin " + findMin( ) );
        if (isEmpty())
            return null;

        Comparable minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        //set.remove(minItem);
        //verifyArray( );
        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    /**
     * Test if the priority queue is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Test if the priority queue is logically full.
     *
     * @return true if full, false otherwise.
     */
    public boolean isFull() {
        return currentSize == array.length - 1;
    }

    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
    }

    public void resize() {
        if (isFull()) {
            if (currentSize * 2 > ABSOLUTE_CAPACITY)
                throw new NullPointerException("Binary Heap is too big");
            Comparable[] newArray = new Comparable[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
    }

    /**
     * Internal method to percolate down in the heap.
     *
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        /* 1*/
        int child;
        /* 2*/
        Comparable tmp = array[hole];

        /* 3*/
        for (; hole * 2 <= currentSize; hole = child) {
            /* 4*/
            child = hole * 2;
            /* 5*/
            if (child != currentSize &&
                    /* 6*/                  array[child + 1].compareTo(array[child]) < 0)
                /* 7*/ child++;
            /* 8*/
            if (array[child].compareTo(tmp) < 0)
                /* 9*/ array[hole] = array[child];
            else
                /*10*/              break;
        }
        /*11*/
        array[hole] = tmp;

        //verifyArray( );
    }

    void test() {
        for (int i = 1; i < 20; i++)
            add(new Integer((int) (Math.random() * 10000)));
        Integer i1 = new Integer(333);
        add(i1);
        for (int i = 1; i < 20; i++)
            add(new Integer((int) (Math.random() * 10000)));
        Integer i2 = new Integer(777);
        add(i2);
        for (int i = 1; i < 20; i++)
            add(new Integer((int) (Math.random() * 10000)));

        remove(i1);

        for (int i = 1; i < 20; i++)
            add(new Integer((int) (Math.random() * 10000)));

        remove(i2);
        try {
            remove(i2);
        } catch (NoSuchElementException e) {
            System.out.println("Didn't find i2");
        }

        while (!isEmpty()) {
            //System.out.println( "v=" + deleteMin() );
        }

    }


/*
            // Test program
        public static void main( String [ ] args )
        {
            int numItems = 10000;
            BinaryHeap h = new BinaryHeap( numItems );
            int i = 37;

            try
            {
                for( i = 37; i != 0; i = ( i + 37 ) % numItems )
                    h.insert( new MyInteger( i ) );
                for( i = 1; i < numItems; i++ )
                    if( ((MyInteger)( h.deleteMin( ) )).intValue( ) != i )
                        System.out.println( "Oops! " + i );

                for( i = 37; i != 0; i = ( i + 37 ) % numItems )
                    h.insert( new MyInteger( i ) );
                h.insert( new MyInteger( 0 ) );
                i = 9999999;
                h.insert( new MyInteger( i ) );
                for( i = 1; i <= numItems; i++ )
                    if( ((MyInteger)( h.deleteMin( ) )).intValue( ) != i )
                        System.out.println( "Oops! " + i + " " );
            }
            catch( Overflow e )
              { System.out.println( "Overflow (expected)! " + i  ); }
        }
        */
}
