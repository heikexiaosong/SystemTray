package cn.thd.ui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ColorMapConfiguredPanel extends Component implements TableModelListener {

	private ActionListener listener;


	public ColorMapConfiguredPanel(ActionListener listener) {
		this.listener = listener;
	}

	public Component make() {

		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


		String[] columnNames = {"ID", "型号", "输出轴", "颜色代码"};

		Object[][] data = {
				{"Kathy", "Smith",
						"Snowboarding", new Integer(5)},
				{"John", "Doe",
						"Rowing", new Integer(3)},
				{"Sue", "Black",
						"Knitting", new Integer(2)},
				{"Jane", "White",
						"Speed reading", new Integer(20)},
				{"Joe", "Brown",
						"Pool", new Integer(10)}
		};

		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(this);

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		panel.add(scrollPane);

		return panel;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		TableModel model = (TableModel)e.getSource();
		String columnName = model.getColumnName(column);
		Object data = model.getValueAt(row, column);

	}
}
