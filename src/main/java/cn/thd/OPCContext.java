package cn.thd;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.Executors;

public class OPCContext {

    private final Server _server;

    private OPCContext(Server server){
        this._server = server;
    }

    private static class LazyHolder {

        private static final OPCContext INSTANCE = buildOpcContext();
    }

    public static OPCContext buildOpcContext() {
        try {
            String host = PropertiesUtils.getValue("opc.host", "127.0.0.1");
            String domain = PropertiesUtils.getValue("opc.domain", "");
            String username = PropertiesUtils.getValue("opc.username", "krsoft");
            String password = PropertiesUtils.getValue("opc.password", "gavel777");
            String clsid = PropertiesUtils.getValue("opc.clsid", "B3AF0BF6-4C0C-4804-A122-6F3B160F4397");

            final ConnectionInformation connInfo = new ConnectionInformation(host, domain, clsid, username, password);
            return new OPCContext(new Server(connInfo, Executors.newScheduledThreadPool(10)));
        } catch (ConfigurationException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return new OPCContext(null);
    }

    public static final OPCContext create() {
        return LazyHolder.INSTANCE;
    }



    public static void serverList() throws ConfigurationException, JIException, UnknownHostException {

        String host = PropertiesUtils.getValue("opc.host", "192.168.30.87");
        String domain = PropertiesUtils.getValue("opc.domain", "");
        String username = PropertiesUtils.getValue("opc.username", "krsoft");
        String password = PropertiesUtils.getValue("opc.password", "gavel777");
//        String clsid = PropertiesUtils.getValue("opc.clsid", "B3AF0BF6-4C0C-4804-A122-6F3B160F4397");
//        String progid = PropertiesUtils.getValue("opc.progid", "Kepware Communications Server 5.14");


        System.out.println("**********************************************");
        System.out.println("OPC.HOST: " + host);
        ServerList serverList = new ServerList(host, username, password, domain);

        Category[] implemented = {Categories.OPCDAServer10, Categories.OPCDAServer20, Categories.OPCDAServer20};
        Collection<ClassDetails> classDetails = serverList.listServersWithDetails(implemented, new Category[]{});

        System.out.println("OPC.Server Num: " + (classDetails==null ? 0 : classDetails.size()));
        for (ClassDetails classDetail : classDetails) {
            System.out.println("\t" + classDetail.getDescription() + "[clsid:" + classDetail.getClsId() + "][progid:" + classDetail.getDescription() + "]");
        }
        System.out.println("**********************************************");
    }

    public Item readValue(String itemId) throws Exception {

        if (StringUtils.isBlank(itemId)){
            throw new RuntimeException("ItemId 不能为空!");
        }

        if ( _server == null ){
            throw new RuntimeException("OPC Server 不能为空!");
        }

        try {
            if ( !_server.isConnected() ) {
                _server.connect();
            }
        } catch (AlreadyConnectedException e) {
        }


        try {

            Group group = null;
            try {
                group = _server.findGroup("read_group");
                group.clear();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("分组[read_group]不存在, 重新创建!");
                group = _server.addGroup("read_group");
            }

            if ( !group.isActive() ){
                group.setActive(true);
            }

            return group.addItem(itemId);
        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(),  _server.getErrorMessage(e.getErrorCode())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeValue(String itemId, int value) throws Exception {

        if (StringUtils.isBlank(itemId)){
            throw new RuntimeException("ItemId 不能为空!");
        }

        if ( _server == null ){
            throw new RuntimeException("OPC Server 不能为空!");
        }

        try {
            if ( !_server.isConnected() ) {
                _server.connect();
            }
        } catch (AlreadyConnectedException e) {
        }


        try {

            Group group = null;
            try {
                group = _server.findGroup("write_group");
                group.clear();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("分组[write_group]不存在, 重新创建!");
                group = _server.addGroup("write_group");
            }

            if ( !group.isActive() ){
                group.setActive(true);
            }

            final Item item = group.addItem(itemId);

            final JIVariant _value = new JIVariant(value);
            try {
                System.out.println( itemId +  " >>> writing value: " + value);
                item.write(_value);
            } catch (JIException e) {
                e.printStackTrace();
            }

        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(),  _server.getErrorMessage(e.getErrorCode())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {

        String itemId = "Channel1.Device1.DB10";

        try {
            OPCContext.serverList();

            OPCContext  context = OPCContext.create();

            Item item = context.readValue(itemId);

            System.out.println(itemId + " >>> read value: " + item.read(true).getValue().getObjectAsUnsigned().getValue());


            context.writeValue(itemId, 10);


            item = context.readValue(itemId);

            System.out.println(itemId + " >>> read value: " + item.read(true).getValue().getObjectAsUnsigned().getValue());


            context.writeValue(itemId, 20);


            item = context.readValue(itemId);

            System.out.println(itemId + " >>> read value: " + item.read(true).getValue().getObjectAsUnsigned().getValue());


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
