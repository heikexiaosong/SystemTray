package cn.thd;

import java.util.HashMap;
import java.util.Map;

public class Order {

    public enum Type {
        GEARMOTOR, IEC_MOTOR;
    }

    public enum Color {
        Red, Yellow, Blue, Green, Purple;
    }

    private Map<String, String> result = new HashMap<>();

    private String gl_katalogbez_a;

    private String type;

    private String abtriebswelle;

    private Type aSide;

    public String getGl_katalogbez_a() {
        return gl_katalogbez_a;
    }

    public void setGl_katalogbez_a(String gl_katalogbez_a) {
        this.gl_katalogbez_a = gl_katalogbez_a;
    }

    public String getAbtriebswelle() {
        return abtriebswelle;
    }

    public void setAbtriebswelle(String abtriebswelle) {
        this.abtriebswelle = abtriebswelle;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Type getaSide() {
        return aSide;
    }

    public void setaSide(Type aSide) {
        this.aSide = aSide;
    }
}
