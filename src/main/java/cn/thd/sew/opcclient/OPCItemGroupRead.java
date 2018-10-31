package cn.thd.sew.opcclient;

import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * OPC server Item 分组读取
 */
public class OPCItemGroupRead {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss (SSS)");

    private static final Lock LOCK = new ReentrantLock();
    private static final Condition STOP = LOCK.newCondition();

    public static final Map<Group, Item[]> groupItemMap = new ConcurrentHashMap<Group, Item[]>();

    public static void main(String[] args) throws Exception {
    }
}
