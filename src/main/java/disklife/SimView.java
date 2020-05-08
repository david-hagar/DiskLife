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

import javax.swing.*;
import java.awt.*;

public class SimView extends JPanel {

    DiskSim diskSim = null;

    public SimView() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDiskSim(DiskSim diskSim) {
        this.diskSim = diskSim;
    }


    public void paint(Graphics g0) {
        super.paint(g0);
        if (diskSim != null)
            diskSim.draw((Graphics2D) g0, this);
        else
            System.out.println("Here");
    }

    private void jbInit() throws Exception {
        this.setBackground(Color.black);
    }


}