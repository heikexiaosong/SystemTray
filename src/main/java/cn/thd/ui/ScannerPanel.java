package cn.thd.ui;

import cn.thd.ColorMapping;
import cn.thd.Order;
import cn.thd.OrderReader;
import gnu.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScannerPanel extends Component implements ActionListener, SerialPortEventListener {

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private JTextField orderTextField;
    private JTextArea msg =  new JTextArea(5, 10);
    private JScrollPane scrollPane;

    private InputStream inputStream = null;

    public Component make() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        orderTextField = new JTextField(30);
        orderTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        orderTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int key = e.getKeyChar();
                System.out.println(key);
                if ( key == 10 ){
                    msg.append("**********************************");
                    msg.append(DATE_FORMAT.format(new Date()));
                    msg.append("***********************\r\n");

                    try {
                        Order result = OrderReader.readInfo(orderTextField.getText());
                        msg.append("订单号: " + orderTextField.getText());
                        msg.append("\r\n");
                        msg.append("型号: " + result.getProduction());
                        msg.append("\r\n");
                        msg.append("输出轴: " + result.getAbtriebswelle());
                        msg.append("\r\n");

                        msg.append("工装颜色: " + ColorMapping.getMatchColor(result.getProduction(), result.getAbtriebswelle()));
                        msg.append("\r\n");

                        for (String name : result.getResult().keySet()) {
                            System.out.println(name + ": " + result.getResult().get(name));

                            msg.append(name + ": " + result.getResult().get(name));
                            msg.append("\r\n");
                        }
                        msg.append("\r\n\r\n");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        msg.append(e1.getMessage());
                        msg.append("\r\n\r\n");
                    } finally {
                        int maxHeight = scrollPane.getVerticalScrollBar().getMaximum();
//                System.out.println(vscrollHeight);
                        scrollPane.getViewport().setViewPosition(new Point(0, maxHeight));
                        scrollPane.updateUI();
                    }
                }
            }
        });

        mainPanel.add(orderTextField);



        msg.setLineWrap(true);
        scrollPane = new JScrollPane(msg);
        // 添加到内容面板
        mainPanel.add(scrollPane);

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

                        msg.append("**********************************");
                        msg.append(DATE_FORMAT.format(new Date()));
                        msg.append("***********************\r\n");

                        msg.append("订单号: " + orderTextField.getText());
                        msg.append("\r\n");

                        Order result = OrderReader.readInfo(orderTextField.getText());
                        msg.append("型号: " + result.getProduction());
                        msg.append("\r\n");
                        msg.append("输出轴: " + result.getAbtriebswelle());
                        msg.append("\r\n");

                        msg.append("工装颜色: " + ColorMapping.getMatchColor(result.getProduction(), result.getAbtriebswelle()));
                        msg.append("\r\n");

                        for (String name : result.getResult().keySet()) {
                            System.out.println(name + ": " + result.getResult().get(name));

                            msg.append(name + ": " + result.getResult().get(name));
                            msg.append("\r\n");
                        }
                        msg.append("\r\n\r\n");
                    }

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    msg.append(e1.getMessage());
                    msg.append("\r\n\r\n");
                } finally {
                    int maxHeight = scrollPane.getVerticalScrollBar().getMaximum();
//                System.out.println(vscrollHeight);
                    scrollPane.getViewport().setViewPosition(new Point(0, maxHeight));
                    scrollPane.updateUI();
                }
                break;
            default:
                break;
        }

    }
}
