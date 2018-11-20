package cn.thd.ui;

import gnu.io.CommPortIdentifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class DevicesConfiguredPanel extends Component{

	private ActionListener listener;

	private JComboBox portComboBox;		// 端口号
	private JComboBox baudComboBox;			// 波特率
	private JTextField databitsTextField;	// 数据位
	private JTextField stopbitsTextField;	// 停止位
	private JTextField parityTextField;		// 数据校验

	public DevicesConfiguredPanel(ActionListener listener) {
		this.listener = listener;
	}

	public Component make() {
		
		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel mainPanel = new JPanel(false);
		panel.add(mainPanel);
		mainPanel.setLayout(new GridLayout(5, 2));

		mainPanel.add(new JLabel("端口号: "));
		//portTextField = new JTextField(30);

		portComboBox = new JComboBox();
		portComboBox.setSize(new Dimension(30, 5));

		Enumeration<CommPortIdentifier> list =   CommPortIdentifier.getPortIdentifiers();
		while (list.hasMoreElements()){
			CommPortIdentifier commPortIdentifier = list.nextElement();
			portComboBox.addItem(commPortIdentifier.getName());
		}

		mainPanel.add(portComboBox);


		mainPanel.add(new JLabel("波特率: "));
		baudComboBox=new JComboBox();
		baudComboBox.addItem("1200");
		baudComboBox.addItem("1800");
		baudComboBox.addItem("2400");
		baudComboBox.addItem("4800");
		baudComboBox.addItem("7200");
		baudComboBox.addItem("9600");
		baudComboBox.addItem("14400");
		baudComboBox.addItem("19200");
		baudComboBox.addItem("34800");
		baudComboBox.addItem("57600");
		baudComboBox.addItem("115200");
		baudComboBox.addItem("128000");
		baudComboBox.addItem("134400");
		baudComboBox.addItem("161280");
		baudComboBox.setSelectedIndex(5);
		mainPanel.add(baudComboBox);

		mainPanel.add(new JLabel("数据位: "));
		databitsTextField = new JTextField("8", 30);
		databitsTextField.setSize(new Dimension(10	, 5));
		mainPanel.add(databitsTextField);

		mainPanel.add(new JLabel("停止位: "));
		stopbitsTextField = new JTextField("1",30);
		mainPanel.add(stopbitsTextField);

		mainPanel.add(new JLabel("数据校验: "));
		parityTextField = new JTextField("NONE",30);
		mainPanel.add(parityTextField);


		JButton button = new JButton("连接");
		button.setActionCommand("connect");
		button.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("");

				App.port = (String)portComboBox.getSelectedItem();
				App.baud = (String)baudComboBox.getSelectedItem();
				App.databits = databitsTextField.getText();
				App.stopbits = stopbitsTextField.getText();
				App.parity = parityTextField.getText();

				System.out.println(App.port + ", " + App.baud + ", " + App.databits + ", " + App.stopbits + ", " + App.parity );

				listener.actionPerformed(e);

			}
		});
		panel.add(button);

		return panel;
	}
}
