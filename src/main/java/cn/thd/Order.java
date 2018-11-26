package cn.thd;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private Map<String, String> result = new HashMap<>();

    private String production;  // 型号

    private String abtriebswelle; // 输出轴

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getAbtriebswelle() {
        return abtriebswelle;
    }

    public void setAbtriebswelle(String abtriebswelle) {
        this.abtriebswelle = abtriebswelle;
    }
}
