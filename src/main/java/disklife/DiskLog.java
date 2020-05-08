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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DiskLog {

    static public boolean onlyLogError = true;
    static PrintStream w = null;

    public DiskLog() {
    }

    static public void init(String file) {
        try {
            w = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            System.out.println("DiskLog FileNotFoundException file=" + file);
            e.printStackTrace();
        }

    }

    static public void println(String s) {
        if (w != null && !onlyLogError)
            w.println(s);
    }

    static public void printlnErr(String s) {
        if (w != null) {
            w.println(s);
            System.out.println(s);
        }
    }

    static void close() {
        w.close();
    }


}