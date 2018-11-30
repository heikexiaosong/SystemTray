package cn.thd.ui;

import cn.thd.PropertiesUtils;
import cn.thd.db.MSSQLConnectionFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class App extends JFrame {		// 6/21 whole gui is now JFrame

    public static String port;
    public static String baud;
    public static String databits;
    public static String stopbits;
    public static String parity;

	public static final String version = "v1.0.0";          // 7/12

	public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new App();

                frame.setTitle("THD电机轴承颜色检测 - v1.0.0 ");			// 6-21
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new MainGUI(),BorderLayout.CENTER);
                frame.pack();											// 6-21
                frame.setSize(850,700 );
                frame.setVisible(true);
            }
        });
	    
	    //initDB();




        //readDBInfo();


    }

    /**
     * init db
     */
    private static void initDB() {
        boolean init = false;
        try {
            DataSource dataSource = MSSQLConnectionFactory.datasource();
            Connection conn = dataSource.getConnection();

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, "%", "thd_configuration", new String[]{"TABLE"});
            while ( resultSet.next() ){
                String tablename = resultSet.getString("TABLE_NAME");
                if ( tablename.equalsIgnoreCase("thd_configuration") ){
                    init = true;
                    break;
                }
            }
            System.out.println("thd_configuration exist: " + init);
            if ( !init ) {
                String initSql = Files.toString(new File("db.sql"), Charsets.UTF_8);
                System.out.println("Create Table SQL: \n" + initSql);

                Statement statement = conn.createStatement();
                statement.executeUpdate(initSql);

                List<String> lines = Files.readLines(new File("data.sql"), Charsets.UTF_8);
                for (String line : lines) {
                    if ( line!=null && line.length() > 0 ){
                        try {
                            statement.executeUpdate(line);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException("数据库连接异常: " + e.getMessage());
        } catch (IOException e) {
            throw  new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private static void readDBInfo() {
        try {
            DataSource dataSource = MSSQLConnectionFactory.datasource();
            Connection conn = dataSource.getConnection();

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, "%", null, new String[]{"TABLE"});
            while ( resultSet.next() ){
                String tablename = resultSet.getString("TABLE_NAME");
                String schema = resultSet.getString("TABLE_SCHEM");
                System.out.println(tablename);

                ResultSet columns =  conn.getMetaData().getColumns(null, schema, tablename.toUpperCase(), null);
                while ( columns.next() ) {

                    System.out.println("\t" + columns.getString("COLUMN_NAME") + "; "  + columns.getString("TYPE_NAME"));
                }

                System.out.println("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException("数据库连接异常: " + e.getMessage());
        }
    }

}


    

