package cn.thd.ui;

import cn.thd.*;
import gnu.io.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerPanel extends Component implements ActionListener, SerialPortEventListener {

    private static final Pattern DIG_PATTERN = Pattern.compile("([0-9]*)");

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private ExecutorService executorService;

    private JTextField orderTextField;
    private JTextField status;
    private JTextArea msg =  new JTextArea(5, 10);
    private JScrollPane scrollPane;

    private InputStream inputStream = null;

    private  OPCContext context;

    public Component make() {

        executorService = Executors.newSingleThreadExecutor();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        orderTextField = new JTextField(30);
        orderTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        orderTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int key = e.getKeyChar();
                if ( key == 10 ){
                    String orderId = "";
                    String recevied = StringUtils.trim(orderTextField.getText());;
                    orderTextField.setText("");
                    Matcher matcher_dig =  DIG_PATTERN.matcher(recevied);
                    if ( matcher_dig.find() ) {
                        orderId = matcher_dig.group(1);
                    }

                    if ( orderId==null || orderId.length()==0 ){
                        return;
                    }

                    msg.append("**********************************");
                    msg.append(DATE_FORMAT.format(new Date()));
                    msg.append("***********************\r\n");

                    msg.append("订单号: " + orderId);
                    msg.append("\r\n");

                    try {
                        orderHandler(orderId);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        msg.append(e1.getMessage());
                        msg.append("\r\n\r\n");
                    } finally {
                        int maxHeight = scrollPane.getVerticalScrollBar().getMaximum();
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

        status =  new JTextField(30);
        status.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        status.setEditable(false);
        status.setText("扫描枪: 未链接");
        mainPanel.add(status);

        context = OPCContext.create();

        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if ( "connect".equalsIgnoreCase(command) ){
            System.out.println("ScannerPanel actionPerformed");
            System.out.println(App.port + ", " + App.baud + ", " + App.databits + ", " + App.stopbits + ", " + App.parity );

            int baud = 9600;
            try {
                baud = Integer.parseInt(App.baud);
            } catch (Exception e){
                System.out.println(e.getMessage() + ": " + App.baud);
            }

            try {
                connect(CommPortIdentifier.getPortIdentifier(App.port), baud);
                status.setText("[扫描枪]已连接: " + App.port);
            } catch (Exception e) {
                e.printStackTrace();
                status.setText("[扫描枪]连接失败: " + e.getMessage());
            }
        }
    }


    public SerialPort connect(CommPortIdentifier portIdentifier, int baud) throws Exception {
        SerialPort serialPort = null;
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open("AppTest", 2000);
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);// serial
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

                        String orderId = "";
                        String recevied = new String(buffer).trim();
                        Matcher matcher_dig =  DIG_PATTERN.matcher(recevied);
                        if ( matcher_dig.find() ) {
                            orderId = matcher_dig.group(1);
                        }
                        if ( orderId==null || orderId.length()==0 ){
                            return;
                        }

                        orderTextField.setText(orderId);

                        msg.append("**********************************");
                        msg.append(DATE_FORMAT.format(new Date()));
                        msg.append("***********************\r\n");
                        msg.append("订单号: " + orderId);
                        msg.append("\r\n");

                        orderHandler(orderId);
                    }

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    msg.append(e1.getMessage());
                    msg.append("\r\n\r\n");
                } finally {
                    int maxHeight = scrollPane.getVerticalScrollBar().getMaximum();
                    scrollPane.getViewport().setViewPosition(new Point(0, maxHeight));
                    scrollPane.updateUI();
                }
                break;
            default:
                break;
        }

    }

    private void orderHandler(String orderId){

        int colorCode = ColorMapping.UNKNOWN;

        try {
            Order result = OrderReader.readInfo(orderId);
            if ( result==null ){
                throw new Exception("订单[" + orderId + "]信息未找到!");
            }

            Map<String, String> attrs = result.getResult();
            for (String name : attrs.keySet()) {
                String value = attrs.get(name);
                System.out.println(String.format("%-32s: %s", name, value));
            }

            msg.append(String.format("数量:   %d",  result.getQuantity()));
            msg.append("\r\n");
            msg.append(String.format("型号:   %20s",  result.getProduction()));
            msg.append("\r\n");
            msg.append(String.format("输出轴: %20s  ====> 轴伸大小: %s",  result.getAbtriebswelle(),  ShaftExtention.parse(result.getAbtriebswelle())));
            msg.append("\r\n");

            colorCode = ColorMapping.selectColor(result.getProduction(), ShaftExtention.parse(result.getAbtriebswelle()));
        } catch (Exception e) {
            e.printStackTrace();
            msg.append(e.getMessage());
            msg.append("\r\n");
        }

        msg.append(">>> 工装颜色选择: " + colorCode);
        msg.append("\r\n");
        msg.append(">>> PLC信号发送 ... ");

        final int  _colorCode = colorCode;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String item_color = PropertiesUtils.getValue("opc.color.itemid", "Channel1.Device1.Color");
                    String item_pulse = PropertiesUtils.getValue("opc.pulse.itemid", "Channel1.Device1.Pulse");

                    context.writeValue(item_color, _colorCode);
                    context.pulseSignal(item_pulse, 1000);
                    msg.append("成功");
                    msg.append("\r\n");
                }catch (Exception e) {
                    e.printStackTrace();
                    msg.append("异常[" + e.getMessage() + "]");
                }
                msg.append("\r\n\r\n");
            }
        });
    }
}
