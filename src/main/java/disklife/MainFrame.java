package disklife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    JPanel contentPane;
    JMenuBar menuBar1 = new JMenuBar();
    JMenu menuFile = new JMenu();
    JMenuItem menuFileExit = new JMenuItem();
    JMenu menuHelp = new JMenu();
    JMenuItem menuHelpAbout = new JMenuItem();
    JToolBar toolBar = new JToolBar();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    BorderLayout borderLayout1 = new BorderLayout();
    SimView drawArea = new SimView();
    JMenu jMenu1 = new JMenu();
    JMenuItem jMenuItem1 = new JMenuItem();


    DiskSim diskSim = new DiskSim();
    SimThread simThread = new SimThread(diskSim, drawArea);
    JButton runButton = new JButton();
    JButton stepButton = new JButton();
    JButton step10Button = new JButton();
    JToggleButton antiAliasButton = new JToggleButton();

    //Construct the frame
    public MainFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawArea.setDiskSim(diskSim);
    }

    //Component initialization
    private void jbInit() throws Exception {
        image1 = new ImageIcon(MainFrame.class.getResource("closeFile.gif"));
        image2 = new ImageIcon(MainFrame.class.getResource("closeFile.gif"));
        image3 = new ImageIcon(MainFrame.class.getResource("help.gif"));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(1200, 1200));
        this.setTitle("Disk Life");
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileExit_actionPerformed(e);
            }
        });
        menuHelp.setText("Help");
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                helpAbout_actionPerformed(e);
            }
        });
        jButton1.setIcon(image1);
        jButton1.setToolTipText("Open File");
        jButton2.setIcon(image2);
        jButton2.setToolTipText("Close File");
        jButton3.setIcon(image3);
        jButton3.setToolTipText("Help");
        jMenu1.setText("Run");
        jMenuItem1.setText("Start");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuItem1_actionPerformed(e);
            }
        });
        runButton.setToolTipText("Help");
        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runButton_actionPerformed(e);
            }
        });
        stepButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stepButton_actionPerformed(e);
            }
        });
        stepButton.setText("Step");
        stepButton.setToolTipText("Help");
        step10Button.setToolTipText("Help");
        step10Button.setText("Step 10");
        step10Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                step10Button_actionPerformed(e);
            }
        });
        antiAliasButton.setMargin(new Insets(0, 2, 0, 2));
        antiAliasButton.setText("AntiAlias");
        antiAliasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                antiAliasButton_actionPerformed(e);
            }
        });
        toolBar.add(jButton1);
        toolBar.add(jButton2);
        toolBar.add(jButton3);
        toolBar.add(runButton, null);
        toolBar.add(stepButton, null);
        toolBar.add(step10Button, null);
        toolBar.add(antiAliasButton, null);
        contentPane.add(drawArea, BorderLayout.CENTER);
        menuFile.add(menuFileExit);
        menuHelp.add(menuHelpAbout);
        menuBar1.add(menuFile);
        menuBar1.add(jMenu1);
        menuBar1.add(menuHelp);
        this.setJMenuBar(menuBar1);
        contentPane.add(toolBar, BorderLayout.NORTH);
        jMenu1.add(jMenuItem1);
    }

    //File | Exit action performed
    public void fileExit_actionPerformed(ActionEvent e) {
        DiskLog.close();
        System.exit(0);
    }

    //Help | About action performed
    public void helpAbout_actionPerformed(ActionEvent e) {
        MainFrame_AboutBox dlg = new MainFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.show();
    }

    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            fileExit_actionPerformed(null);
        }
    }

    void jMenuItem1_actionPerformed(ActionEvent e) {

        diskSim.draw(drawArea);
        simThread.start();

    }

    void runButton_actionPerformed(ActionEvent e) {

        if (!simThread.isAlive())
            simThread = new SimThread(diskSim, drawArea);
        simThread.start();
    }

    void stepButton_actionPerformed(ActionEvent e) {
        diskSim.runOneFrame();
        drawArea.repaint();
    }

    void step10Button_actionPerformed(ActionEvent e) {

        for (int i = 0; i < 1000; i++)
            diskSim.runOneFrame();

        drawArea.repaint();
    }

    void antiAliasButton_actionPerformed(ActionEvent e) {
        diskSim.antiAlias = ((JToggleButton) e.getSource()).isSelected();
    }
}