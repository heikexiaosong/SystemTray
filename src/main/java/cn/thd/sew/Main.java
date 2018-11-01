package cn.thd.sew;

import gnu.io.CommPortIdentifier;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws SQLException {

        System.out.println("hello!");

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        } else {
            System.out.println("SystemTray is supported");
        }


        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> portNameList = new ArrayList<>();

        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
            System.out.println(portName);
        }

    }
}
