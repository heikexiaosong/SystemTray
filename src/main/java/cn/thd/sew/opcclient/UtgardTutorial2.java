package cn.thd.sew.opcclient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;

public class UtgardTutorial2 {

    public static void main(String[] args) throws Exception {

        // create connection information
        final ConnectionInformation ci = new ConnectionInformation();

        ci.setHost("127.0.0.1");
        ci.setDomain("");
        ci.setUser("krsoft");
        ci.setPassword("gavel777");
        //ci.setProgId("Kepware Communications Server 5.14");
        ci.setClsid("B3AF0BF6-4C0C-4804-A122-6F3B160F4397"); // if ProgId is not working, try it using the Clsid instead
        final String itemId = "Channel1.Device1.DBW0";




        // create a new server
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            // connect to server
            server.connect();
            // add sync access, poll every 500 ms
            final AccessBase access = new SyncAccess(server, 500);
            access.addItem(itemId, new DataCallback() {
                @Override
                public void changed(Item item, ItemState state) {
                    // also dump value
                    try {

                        System.out.println(state + " #### value: " + state.getValue().getObjectAsUnsigned().getValue());

//                        if (state.getValue().getType() == JIVariant.VT_UI4) {
//                            System.out.println("<<< " + state + " / value = " + state.getValue().getObjectAsUnsigned().getValue());
//                        } else {
//                            System.out.println("<<< " + state + " / value = " + state.getValue().getObject());
//                        }
                    } catch (JIException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Add a new group
            final Group group = server.addGroup("test");
            // Add a new item to the group
            final Item item = group.addItem(itemId);

            // start reading
            access.bind();

            // add a thread for writing a value every 3 seconds
            ExecutorService writeThread = Executors.newSingleThreadExecutor();
            final AtomicInteger i = new AtomicInteger(0);
            writeThread.execute(new Runnable() {
                @Override
                public void run() {
                    final JIVariant value = new JIVariant(i.incrementAndGet());
                    try {
                        System.out.println(">>> " + "writing value " + i.get());
                        item.write(value);
                    } catch (JIException e) {
                        e.printStackTrace();
                    }
                }
            });

            // wait a little bit
            Thread.sleep(20 * 1000);
            writeThread.shutdownNow();
            // stop reading
            access.unbind();
        } catch (final JIException e) {
            e.printStackTrace();
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }
}
