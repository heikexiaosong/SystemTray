package cn.thd.sew;

import cn.thd.ui.App;
import cn.thd.ui.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class SystemTrayApp {
    public static void main(String[] args) {

        Properties props = System.getProperties();

        String osname = props.getProperty("os.name", "Windows").toLowerCase();
        System.out.println("操作系统的名称：" + osname);


        /* Use an appropriate Look and Feel */
        try {
            //
            if ( osname.contains("windows") ){
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }

            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {

        final JFrame frame = new App();
        frame.setTitle("THD电机轴承颜色检测 - v1.0.0 ");			// 6-21
        frame.setLocation(200, 100);
        frame.setSize(1200,700 );
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(new MainGUI(),BorderLayout.CENTER);

        //Display the window.
        //frame.pack();

        if ( SystemTray.isSupported()) {
            final TrayIcon trayIcon = new TrayIcon(createImage("images/bulb.jpeg", "tray icon"));
            trayIcon.setImageAutoSize(true);


            final SystemTray tray = SystemTray.getSystemTray();

            // Create a popup menu components
            MenuItem mainUI = new MenuItem("显示/隐藏主界面");
            MenuItem exitItem = new MenuItem("退出");

            //Add components to popup menu
            final PopupMenu popup = new PopupMenu();
            popup.add(mainUI);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
                return;
            }

            trayIcon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "THD电机轴承颜色检测");
                }
            });

            mainUI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if( !frame.isVisible() ) {
                        frame.setVisible(true);
                    }
                }
            });

            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });

        } else {
            System.out.println("SystemTray is not supported");
        }


        frame.pack();
        frame.setVisible(true);
    }

    protected static Image createImage(String path, String description) {
        return (new ImageIcon(path, description)).getImage();
    }
}
