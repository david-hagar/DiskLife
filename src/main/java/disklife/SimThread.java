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

public class SimThread extends Thread {

    DiskSim diskSim;
    SimView panel;
    ElapsTimer t = new ElapsTimer(System.out, 20);
    ElapsTimer tot = new ElapsTimer(System.out, 20);

    public SimThread(DiskSim diskSim, SimView panel) {
        this.diskSim = diskSim;
        this.panel = panel;
    }

    public void run() {
        int oCount = 0;

        while (!isInterrupted()) {
            tot.start();
            diskSim.runOneFrame();

            synchronized (diskSim.repaintMutex) {
                t.start();
                panel.repaint();
                try {
                    diskSim.repaintMutex.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                t.stopAndPrint("draw");
            }

            //try { sleep( 500 ); } catch( Exception e ) { e.printStackTrace(); return; }
            //yield();

    /*
    if(diskSim.checkForOverlap())
    {
    oCount++;
    System.out.println("Overlap detected " + oCount);
    //return;
    }
    */

            tot.stopAndPrint("total");
        }

    }


}