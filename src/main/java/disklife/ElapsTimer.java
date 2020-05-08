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

import java.io.PrintStream;

public class ElapsTimer {
    static public boolean disable = true;
    long startTime;
    long stopTime;
    MeasureQueue m;
    PrintStream out;

    public ElapsTimer(PrintStream os, int queueSize) {
        m = new MeasureQueue(queueSize);
        out = os;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stopAndPrint(String tag) {
        if (disable)
            return;
        stopTime = System.currentTimeMillis();
        float time = (float) ((stopTime - startTime) / 1000.0);
        m.add(time);
        //out.print( tag + " " );
        m.printStats(tag, out);
    }
}