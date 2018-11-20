package cn.thd.sew;

import gnu.io.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("hello!");

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        } else {
            System.out.println("SystemTray is supported");
        }


        connect(CommPortIdentifier.getPortIdentifier("COM3"));

    }


    public static SerialPort connect(CommPortIdentifier portIdentifier) throws Exception {
        SerialPort serialPort = null;
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open("AppTest", 2000);
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);// serial
                serialPort.setRTS(true);
                serialPort.setDTR(true);

                serialPort.setInputBufferSize(4096);
                serialPort.setOutputBufferSize(4096);

                InputStream inputStream = serialPort.getInputStream();
                serialPort.addEventListener(new SerialReader(inputStream));
                serialPort.notifyOnDataAvailable(true);
            }
        }
        return serialPort;

    }

    public static class SerialReader implements SerialPortEventListener {
        private InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void serialEvent(SerialPortEvent arg0) {
            System.out.print(arg0.getEventType() + " => ");

            switch (arg0.getEventType()) {
                case SerialPortEvent.BI:	//通讯中断
                    System.out.println("通讯中断");
                    break;
                case SerialPortEvent.OE:	//溢位错误
                    System.out.println("溢位错误");
                    break;
                case SerialPortEvent.FE:	//帧错误
                    System.out.println("帧错误");
                    break;
                case SerialPortEvent.PE:	//奇偶校验错误
                    System.out.println("奇偶校验错误");
                    break;
                case SerialPortEvent.CD:	//载波检测
                    System.out.println("载波检测");
                    break;
                case SerialPortEvent.CTS:	//清除发送
                    System.out.println("清除发送");
                    break;
                case SerialPortEvent.DSR:	//数据设备准备好
                    System.out.println("数据设备准备好");
                    break;
                case SerialPortEvent.RI:	//响铃侦测
                    System.out.println("响铃侦测");
                    break;
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:	//输出缓冲区已清空
                    System.out.println("输出缓冲区已清空");
                    break;
                case SerialPortEvent.DATA_AVAILABLE:	//有数据到达
                    System.out.println("有数据到达");

                    byte[] buffer = new byte[1024];
                    try {
                        //// the thread need to sleep for completed

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // receive the data
                        if (in.available() > 0) {
                            in.read(buffer);

                            for (byte b : buffer) {

                                System.out.print((char)b + ",");
                            }

                            System.out.print(new String(buffer));
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }

    }

}
