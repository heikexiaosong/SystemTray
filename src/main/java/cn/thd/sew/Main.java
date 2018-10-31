package cn.thd.sew;

import java.awt.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        System.out.println("hello!");

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        } else {
            System.out.println("SystemTray is supported");
        }



    }
}
