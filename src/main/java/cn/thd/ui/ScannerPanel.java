package cn.thd.ui;

import cn.thd.sew.Main;
import gnu.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScannerPanel extends Component implements ActionListener, SerialPortEventListener {

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private JTextField orderTextField;
    private JTextArea msg =  new JTextArea(5, 10);

    private InputStream inputStream = null;

    public Component make() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        orderTextField = new JTextField(30);
        orderTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        mainPanel.add(orderTextField);

        msg.setLineWrap(true);
        // 添加到内容面板
        mainPanel.add(msg);

        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if ( "connect".equalsIgnoreCase(command) ){
            System.out.println("ScannerPanel actionPerformed");

            System.out.println(App.port + ", " + App.baud + ", " + App.databits + ", " + App.stopbits + ", " + App.parity );

            try {
                connect(CommPortIdentifier.getPortIdentifier(App.port));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public SerialPort connect(CommPortIdentifier portIdentifier) throws Exception {
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

                inputStream = serialPort.getInputStream();
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            }
        }
        return serialPort;

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
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // receive the data
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);

                        for (byte b : buffer) {

                            System.out.print((char)b + ",");
                        }
                        orderTextField.setText(new String(buffer));

                        msg.append(DATE_FORMAT.format(new Date()) + ": ");
                        msg.append(new String(buffer));
                        msg.append("\r\n");
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
