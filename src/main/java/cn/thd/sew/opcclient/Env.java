package cn.thd.sew.opcclient;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;


public class Env {

    public static final String HOST = "10.17.8.188";

    public static final String DOMAIN = "";

    public static final String USER = "sjcj";

    public static final String PASSWORD = "Xxzx2288320";

    public static final String CLSID =  "B3AF0BF6-4C0C-4804-A122-6F3B160F4397"; //"B3AF0BF6-4C0C-4804-A122-6F3B160F4397";

    public static void list() throws JIException, UnknownHostException {

        System.out.println("HOST: " + HOST);

        ServerList serverList = new ServerList(HOST, USER, PASSWORD, "");

        Collection<ClassDetails> classDetails = serverList.listServersWithDetails(new Category[]{Categories.OPCDAServer10, Categories.OPCDAServer20, Categories.OPCDAServer20}, new Category[]{});

        System.out.println("");
        for (ClassDetails classDetail : classDetails) {
            System.out.println(" * ============= *: " + classDetail.getClsId() + " = " + classDetail.getDescription());
        }
        System.out.println("");


    }

    public static void main(String[] args) throws JIException, UnknownHostException {
        list();
    }

}
