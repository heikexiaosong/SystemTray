package cn.thd.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JPanel implements ActionListener {

    private  JTabbedPane tabbedPane;

    private  DevicesConfiguredPanel configuredPanel;

    private  ScannerPanel scannerPanel;

    public MainGUI() {

        tabbedPane = new JTabbedPane();

        configuredPanel = new DevicesConfiguredPanel(this); // 6/15
        tabbedPane.addTab("COM配置", null, configuredPanel.make(), "COM配置");

        scannerPanel = new ScannerPanel();
        tabbedPane.addTab("扫描枪", null, scannerPanel.make(), "扫描枪");

        tabbedPane.setSelectedIndex(0);

        setLayout(new GridLayout(1, 1));
        add(tabbedPane);

        char k = 'a';
        KeyStroke ks = KeyStroke.getKeyStroke(k);
        this.registerKeyboardAction(this, ks, JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    public void actionPerformed(ActionEvent ae) {

        String command = ae.getActionCommand();
        if ( "connect".equalsIgnoreCase(command) ){
            System.out.println("actionPerformed");

            System.out.println(App.port + ", " + App.baud + ", " + App.databits + ", " + App.stopbits + ", " + App.parity );

            tabbedPane.setSelectedIndex(1);

            scannerPanel.actionPerformed(ae);
        }
    }

}
