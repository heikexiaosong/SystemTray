package cn.thd.sew;
/*
 * TrayIconDemo.java
 */

import cn.thd.ui.TabbedPane;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class TrayIconDemo {
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
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }


        JFrame frame = new JFrame("下轴承类型检测 - THD");
        frame.setSize(1000, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTabbedPane tabbedPane = new JTabbedPane();


        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
       // frame.getContentPane().add(label);

        frame.add(new TabbedPane(), BorderLayout.CENTER);

        //Display the window.
        //frame.pack();
        frame.setVisible(true);



        final TrayIcon trayIcon = new TrayIcon(createImage("images/bulb.gif", "tray icon"));
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create a popup menu components
        MenuItem configItem = new MenuItem("参数配置");
        MenuItem exitItem = new MenuItem("退出");
        
        //Add components to popup menu
        final PopupMenu popup = new PopupMenu();
        popup.add(configItem);
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
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });
        
        configItem.addActionListener(new ActionListener() {
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




        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
//                trayIcon.displayMessage("Sun TrayIcon Demo",
//                        "This is an ordinary message", TrayIcon.MessageType.NONE);

                label.setText(label.getText() + "\n" + "hello");

//                JOptionPane.showMessageDialog(null,
//                        "This dialog box is run from the About menu item");
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);
    }
    
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        return (new ImageIcon(path, description)).getImage();
//        URL imageURL = TrayIconDemo.class.getResource(path);
//
//        if (imageURL == null) {
//            System.err.println("Resource not found: " + path);
//            return null;
//        } else {
//            return (new ImageIcon(path, description)).getImage();
//        }
    }
}
