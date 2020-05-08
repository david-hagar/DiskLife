package disklife;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c)
 * Company:
 *
 * @author
 * @version 1.0
 */

import java.io.PrintStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class MeasureQueue {

    LinkedList queue = new LinkedList();
    int size;
    int printCount = 0;

    public MeasureQueue(int size) {
        this.size = size;
        for (int i = 0; i < size; i++)
            queue.addLast(new Float(0.0f));
    }

    void add(float value) {
        queue.removeFirst();
        queue.addLast(new Float(value));
    }

    void printStats(String tag, PrintStream out) {
        if (printCount++ != size) {
            return;
        }

        printCount = 0;

        Float max = (Float) Collections.max(queue);
        Float min = (Float) Collections.min(queue);

        float total = 0.0f;
        Iterator i = queue.iterator();
        while (i.hasNext()) {
            total += ((Float) i.next()).floatValue();
        }

        out.println(tag + " ave= " + total / size + " min=" + min.floatValue() + " max=" + max.floatValue());

    }


}