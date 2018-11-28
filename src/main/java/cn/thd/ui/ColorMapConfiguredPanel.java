package cn.thd.ui;

import cn.thd.PropertiesUtils;
import cn.thd.db.MSSQLConnectionFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ColorMapConfiguredPanel extends Component implements TableModelListener {

	private ActionListener listener;


	public ColorMapConfiguredPanel(ActionListener listener) {
		this.listener = listener;
	}

	public Component make() {

		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


		String[] columnNames = {"ID", "型号", "输出轴", "颜色代码"};

		Object[][] data = new Object[0][];


		try {
			QueryRunner run = new QueryRunner( MSSQLConnectionFactory.datasource());

            java.util.List<Object[]> list  = run.query("select * from thd_configuration ", new ArrayListHandler());

            data = list.toArray(new Object[list.size()][]);

		} catch (SQLException e) {
			e.printStackTrace();
			throw  new RuntimeException("数据库连接异常: " + e.getMessage());
		}



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
