package cn.thd.sew.opcclient;

import cn.thd.PropertiesUtils;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * OPC server tag 数据读取
 *
 */
public class Main {

    private static List<String> getItemIds(){
        List<String> result = new ArrayList<>();

        result.add("Random.UInt4");

        result.add("Random.String");

        result.add("Random.Boolean");

        result.add("Square Waves.UInt2");

        return result;
    }

    public static void main(String[] args) throws Exception {

        // 关闭初始化日志信息
        //Logger.getLogger ( "org.jinterop" ).setLevel(Level.WARNING);

        String host = PropertiesUtils.getValue("opc.host", "192.168.30.87");
        String domain = PropertiesUtils.getValue("opc.domain", "");
        String username = PropertiesUtils.getValue("opc.username", "krsoft");
        String password = PropertiesUtils.getValue("opc.password", "gavel777");
        String clsid = PropertiesUtils.getValue("opc.clsid", "B3AF0BF6-4C0C-4804-A122-6F3B160F4397");
        String progid = PropertiesUtils.getValue("opc.progid", "Kepware Communications Server 5.14");

        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(host);
        ci.setDomain(domain);
        ci.setUser(username);
        ci.setPassword(password);

        //ci.setProgId(progid);
        ci.setClsid(clsid); // if ProgId is not working, try it using the Clsid instead

        final Server server = new Server(ci, Executors.newScheduledThreadPool(10));

        try {
            // connect to server
            server.connect();
            // add sync access, poll every 500 ms
            final AccessBase access = new SyncAccess(server, 500);
            access.addItem("Channel1.Device1.DB10", new DataCallback() {
                @Override
                public void changed(Item item, ItemState state) {

                    try {
                        System.out.println(state.getValue().getObjectAsUnsigned().getValue());
                    } catch (JIException e) {
                        e.printStackTrace();
                    }
                }
            });
            // start reading
            access.bind();
            // wait a little bit
            Thread.sleep(100 * 1000);
            // stop reading
            access.unbind();
        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }

}
