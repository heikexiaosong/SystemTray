package cn.thd;

import cn.thd.db.MSSQLConnectionFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OrderReader {

    public static void main(String[] args) throws IOException, ConfigurationException {


//        PropertiesUtils.setValue("db.hostName", "10.9.24.12");
//        PropertiesUtils.setValue("db.database", "MotorResultData");
//        PropertiesUtils.setValue("db.username", "motqa");
//        PropertiesUtils.setValue("db.password", "Motqa2017");

        try {
            Order result = readInfo("4999424");

            for (String key : result.getResult().keySet()) {
                System.out.println(key + ": " + result.getResult().get(key));
            }
            System.out.println(" === ");

            System.out.println("型号: " + result.getProduction());
            System.out.println("输出轴: " + result.getAbtriebswelle());

            System.out.println("工装型号: " + ColorMapping.getMatchColor(result.getProduction(), result.getAbtriebswelle()));

            System.out.println(" === ");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Order readInfo(String _orderId) throws IOException, ConfigurationException, SQLException {

        int orderId = Integer.parseInt(_orderId);

        Order order = new Order();

        QueryRunner run = new QueryRunner(MSSQLConnectionFactory.datasource());
        Map<String, Object> result = run.query("select * from motor_routine_test where assembly_order = ? ", new MapHandler(), orderId);
        if ( result==null || result.size()==0  ){
            throw new RuntimeException("没有找到订单号: " + orderId + " 对应的信息.");
        }

        order.setProduction((String)result.get("production"));

        String line = (String)result.get("notes");
        System.out.println("[notes: " + line.length() + "]: " + line);

        // 解析数据
        Map<String, String> attrMap = new HashMap<>();

        attrMap.put("工单数量", line.substring(0, 3));
        attrMap.put("型号", line.substring(4, 33));
        attrMap.put("安装方式", line.substring(34, 63));
        attrMap.put("GL_UEBERSETZG_GES", line.substring(64, 75));

        // 输出轴
        String aside = line.substring(76, 90);
        order.setAbtriebswelle(aside);
        attrMap.put("输出轴",aside);

        attrMap.put("空心轴", line.substring(91, 105));
        attrMap.put("法兰尺寸", line.substring(106, 120));
        attrMap.put("润滑剂量", line.substring(121, 131));
        attrMap.put("OEL_VISKOSITAET", line.substring(132, 151));
        attrMap.put("功率", line.substring(152, 158));
        attrMap.put("工作制", line.substring(159, 170));
        attrMap.put("电压", line.substring(171, 177));
        attrMap.put("电流（普通电机及双频电机50Hz下电流1）", line.substring(178, 186));
        attrMap.put("功率因素", line.substring(187, 193));
        attrMap.put("制动电压", line.substring(194, 197));
        attrMap.put("防护等级", line.substring(198, 200));
        attrMap.put("接线盒角度", line.substring(201, 206));
        attrMap.put("电机保护", line.substring(207, 210));
        attrMap.put("频率", line.substring(211, 217));
        attrMap.put("接线图", line.substring(218, 221));
        attrMap.put("接线方式", line.substring(222, 251));
        attrMap.put("绝缘等级", line.substring(252, 252));
        attrMap.put("转速（各类电机低速）", line.substring(253, 256));
        attrMap.put("序号", line.substring(257, 286));
        attrMap.put("LAGER_SNR", line.substring(287, 294));
        attrMap.put("电流（普通电机及双频电机50Hz下电流2）", line.substring(295, 303));
        attrMap.put("电流（普通电机及双频电机60Hz下电流1）", line.substring(304, 312));
        attrMap.put("电流（普通电机及双频电机60Hz下电流2）", line.substring(313, 321));
        attrMap.put("转速（各类电机高速）", line.substring(322, 325));
        attrMap.put("GL_TEXT", line.substring(326, 336));
        attrMap.put("功率（双频电机60Hz及双速电机高速下）", line.substring(337, 342));
        attrMap.put("功率因数(双频高频下或双速电机高速下)", line.substring(343, 348));
        attrMap.put("电压（双频电机50Hz下）", line.substring(349, 378));
        attrMap.put("电压（双频电机60Hz下）", line.substring(379, 408));
        attrMap.put("频率（双频电机低频）", line.substring(409, 414));
        attrMap.put("频率（双频电机高频）", line.substring(415, 420));
        attrMap.put("电压（宽电压电机电机电压范围）", line.substring(421, 450));
        attrMap.put("制动电压（宽电压电机电压）", line.substring(451, 480));
        attrMap.put("Other", line.substring(481));
        order.setResult(attrMap);

        return order;
    }


}


