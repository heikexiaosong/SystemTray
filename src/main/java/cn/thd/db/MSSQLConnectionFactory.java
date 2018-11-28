package cn.thd.db;

import cn.thd.PropertiesUtils;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MSSQLConnectionFactory {

    private static Map<String, HikariDataSource> map = new ConcurrentHashMap<>();

    public static synchronized DataSource datasource() {

        String hostName = PropertiesUtils.getValue("db.hostName", "10.9.24.12");
        String database = PropertiesUtils.getValue("db.database", "MotorResultData");
        String username = PropertiesUtils.getValue("db.username", "motqa");
        String password = PropertiesUtils.getValue("db.password", "Motqa2017");


        String key = String.format("%s_%d_%s", hostName, 1433, database).toLowerCase();
        if ( map.containsKey(key) ){
            HikariDataSource dataSource = map.get(key);
            if ( dataSource!=null && dataSource.isRunning() ){
                return dataSource;
            }
        }

        String url = String.format("jdbc:sqlserver://%s:%d;database=%s", hostName, 1433, database);
        System.out.println("url: " + url);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        // HikariCP提供的优化设置
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        map.put(key, ds);

        return ds;
    }




}
