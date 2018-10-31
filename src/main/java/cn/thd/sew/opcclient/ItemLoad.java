package cn.thd.sew.opcclient;

import java.util.List;

/**
 *  获取OPC 服务器中的 特定的 itemid 并存储到 H2 数据库文件中
 */
public class ItemLoad {

  public static void main(String[] args) {

    // 关闭初始化日志信息
    //Logger.getLogger( "org.jinterop" ).setLevel(Level.WARNING);

    OpcClient opcClient = OpcClient.getInstance();
    try {
      opcClient.connect();

      List<String> itemids = opcClient.getItemids("Channel1.Device*.Tag*");

      System.out.println("Get Itemids: " + itemids.size());
      for (String itemid : itemids) {
        System.out.println(itemid);
      }
//      Connection conn = H2Connection.getConnection();
//      for (String itemid : itemids) {
//        DBWriter.saveItemGroup(conn, itemid, "group1");
//      }
//      conn.commit();
//      conn.close();
      System.out.println("Get Itemids finish.");
    } catch (Exception e){
      e.printStackTrace();
    }

  }
}
