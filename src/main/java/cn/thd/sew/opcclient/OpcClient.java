package cn.thd.sew.opcclient;

import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.browser.BaseBrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class OpcClient {

  private boolean isConnect = false;

  private Server server;

  private List<String> itemids = new ArrayList<>();

  private OpcClient() {
  }

  private static class LazyHolder {
    private static final OpcClient INSTANCE = buildOpcClient(Env.HOST, Env.USER, Env.PASSWORD, Env.CLSID);
  }

  public static final OpcClient getInstance() {
    return LazyHolder.INSTANCE;
  }

  private OpcClient(Server server) {
    this.server = server;
  }

  public static OpcClient buildOpcClient(String host, String user, String password, String clsid) {
    final ConnectionInformation ci = new ConnectionInformation();
    ci.setHost(host);
    //ci.setDomain(Env.DOMAIN);
    ci.setUser(user);
    ci.setPassword(password);
    ci.setClsid(clsid);
    final Server _server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
    return new OpcClient(_server);
  }


  /**
   * 登陆 OPC Server
   */
  public void connect() throws Exception {
    if (server == null) {
      throw new Exception("OPC Server Info not init.");
    }
    if ( isConnect ){
      return;
    }
    server.connect();
    isConnect = true;

  }

  public List<String> getItemids(final String filterCriteria) throws Exception {
    if (server == null) {
      throw new Exception("OPC Server Info not init.");
    }

    final BaseBrowser flatBrowser = server.getFlatBrowser();
    itemids.clear();
    if (flatBrowser != null) {
      for (final String item : server.getFlatBrowser().browse(filterCriteria)) {
        itemids.add(item);
      }
    }

    return itemids;
  }

  public Server getServer() {
    return server;
  }
}
