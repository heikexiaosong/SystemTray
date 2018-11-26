package cn.thd;

import cn.thd.db.MSSQLConnectionFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ColorMapping {

    public static String getMatchColor(String production, String abtriebswelle)  {

        String color = null;
        try {
           String hostName = PropertiesUtils.getValue("db.hostName", "10.9.24.12");
           String database = PropertiesUtils.getValue("db.database", "MotorResultData");
           String username = PropertiesUtils.getValue("db.username", "motqa");
           String password = PropertiesUtils.getValue("db.password", "Motqa2017");

           QueryRunner run = new QueryRunner( MSSQLConnectionFactory.datasource(hostName, 1433, database, username, password));

           Map<String, Object> result = run.query("select * from CONFIGURATION where PRODUCTION = ? and ABTRIEBSWELLE = ? ", new MapHandler(), production, abtriebswelle);

           if ( result==null || result.size()==0 || StringUtils.isBlank((String)result.get("COLOR"))){
               throw  new RuntimeException("找不到对应的工装型号.[型号： " +  production + "， 输出轴" +  abtriebswelle + "]");
           }

            return  (String)result.get("COLOR");
       } catch (SQLException e) {
           e.printStackTrace();
           throw  new RuntimeException("数据库连接异常: " + e.getMessage());
       } catch (ConfigurationException e) {
           e.printStackTrace();
            throw  new RuntimeException("读取配置文件[config.properties]异常: " + e.getMessage());
       }
    }
}
