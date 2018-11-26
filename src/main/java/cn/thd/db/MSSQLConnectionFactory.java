package cn.thd.db;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MSSQLConnectionFactory {

    private static Map<String, HikariDataSource> map = new ConcurrentHashMap<>();

    public static synchronized DataSource datasource(String hostName, int port, String database, String username, String password) {

        String key = String.format("%s_%d_%s", hostName, port, database).toLowerCase();
        if ( map.containsKey(key) ){
            HikariDataSource dataSource = map.get(key);
            if ( dataSource!=null && dataSource.isRunning() ){
                return dataSource;
            }
        }

        HikariDataSource ds = new HikariDataSource();

        String url = String.format("jdbc:sqlserver://%s:%d;database=%s", hostName, port, database);
        System.out.println("url: " + url);

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
