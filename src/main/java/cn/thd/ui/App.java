package cn.thd.ui;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {		// 6/21 whole gui is now JFrame

    public static String port;
    public static String baud;
    public static String databits;
    public static String stopbits;
    public static String parity;

	public static final String version = "v1.0.0";          // 7/12

	public static void main(String[] args) {

        JFrame frame = new App();

        frame.setTitle("THD电机下轴承型号检测 - v1.0.0 ");			// 6-21
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainGUI(),BorderLayout.CENTER);
        frame.pack();											// 6-21
        frame.setSize(850,700 );
        frame.setVisible(true);

    }

}


    

