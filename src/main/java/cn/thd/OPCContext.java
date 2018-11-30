package cn.thd;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return new OPCContext(null);
    }

    public static final OPCContext create() {
        return LazyHolder.INSTANCE;
    }



    public static void serverList() throws ConfigurationException, JIException, UnknownHostException {

        String host = PropertiesUtils.getValue("opc.host", "10.0.2.15");
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


    /**
     *  Item 产生 脉冲信号  岩石 delay ms
     *
     * @param itemId
     * @param delay
     * @throws Exception
     */
    public void pulseSignal(String itemId, int delay) throws Exception {

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

            try {
                item.write(new JIVariant(1));
                System.out.println(Calendar.getInstance().getTimeInMillis() + ": " +  itemId +  " >>> writing value: 1");

                Thread.sleep(delay);

                item.write(new JIVariant(0));
                System.out.println(Calendar.getInstance().getTimeInMillis() + ": " +  itemId +  " >>> writing value: 0");

            } catch (JIException e) {
                e.printStackTrace();
            }

        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(),  _server.getErrorMessage(e.getErrorCode())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void monitor(final String itemId, final int interval, final int delay) throws Exception {


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

        final  ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<?>  future = Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // add sync access, poll every 500 ms
                    final AccessBase access = new SyncAccess(_server, interval);
                    access.addItem(itemId, new DataCallback() {
                        @Override
                        public void changed(Item item, ItemState state) {

                            try {
                                System.out.println(Calendar.getInstance().getTimeInMillis() + ": " + item.getId() + " >>> Value:"  + state.getValue().getObjectAsUnsigned().getValue());
                            } catch (JIException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    access.bind();
                    Thread.sleep(delay);
                    access.unbind();
                } catch (final Exception e) {
                    e.printStackTrace();
                } finally {
                    executorService.shutdownNow();
                }
            }
        });

    }





    public static void main(String[] args) {

        String item_color = PropertiesUtils.getValue("opc.color.itemid", "Channel1.Device1.Color");

        String item_pulse = PropertiesUtils.getValue("opc.pulse.itemid", "Channel1.Device1.Pulse");

        try {
            OPCContext.serverList();

            final  OPCContext  context = OPCContext.create();

            context.monitor(item_pulse, 200, 3000);

            context.pulseSignal(item_pulse, 1000);



        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
