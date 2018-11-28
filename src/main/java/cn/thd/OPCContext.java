package cn.thd;

import org.apache.commons.configuration.ConfigurationException;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;

public class OPCContext {

    private static OPCContext instance = new OPCContext();

    public static OPCContext create() throws ConfigurationException, JIException, UnknownHostException {

        String host = PropertiesUtils.getValue("opc.host", "127.0.0.1");
        String domain = PropertiesUtils.getValue("opc.domain", "");
        String username = PropertiesUtils.getValue("opc.username", "krsoft");
        String password = PropertiesUtils.getValue("opc.password", "gavel777");
        String clsid = PropertiesUtils.getValue("opc.clsid", "B3AF0BF6-4C0C-4804-A122-6F3B160F4397");
        String progid = PropertiesUtils.getValue("opc.progid", "Kepware Communications Server 5.14");


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

        return instance;
    }

    public static void main(String[] args) {
        try {
            OPCContext.create();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
