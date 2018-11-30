package cn.thd;

import cn.thd.db.MSSQLConnectionFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ColorMapping {

    public static final int UNKNOWN = 13;

    private static final String QUERY_SQL = "select * from bearing_kit_production" +
            "                   left join bearing_kit on bearing_kit.id = bearing_kit_id" +
            "        where production = ? and  shaft_extention = ?";

    /**
     * @param production
     * @param shaftExtention
     * @return
     */
    public static int selectColor(String production, String shaftExtention) {

        Object colorCode = null;

        try {
            QueryRunner run = new QueryRunner(MSSQLConnectionFactory.datasource());
            List<Map<String, Object>> result = run.query(QUERY_SQL, new MapListHandler(), production, shaftExtention);

            if (result!=null && result.size()==1 ) {
                Map<String, Object> record = result.get(0);
                colorCode = record.get("color_code");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getIntValue(colorCode, UNKNOWN);
    }

    private static int getIntValue(Object colorCode, int defaultValue){
        if ( colorCode==null ){
            return defaultValue;
        }

        if ( colorCode instanceof Number ){
            return ((Number)colorCode).intValue();
        }

        if ( colorCode instanceof String ){
            return Integer.parseInt((String)colorCode);
        }

        return defaultValue;
    }


    public static void main(String[] args) {
        System.out.println(selectColor("DRS71M4", "IEC14x30"));
    }
}
