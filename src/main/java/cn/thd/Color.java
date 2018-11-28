package cn.thd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Color {

    RED_901000(1),
    YELLOW_901000(2),
    BLUE_901000(3),
    GREEN_901000(4),

    PURPLE_112132(5),
    RED_112132(6),
    YELLOW_112132(7),
    BLUE_112132(8),
    GREEN_112132(9),

    RED_6380(10),
    YELLOW_6380(11),
    BLUE_6380(12),
    GREEN_6380(13),

    UNKOWN(14);

    private static final Map<String, Color> map = new ConcurrentHashMap<>();
    static {
        for (Color color : Color.values()) {
            map.put(color.name().toLowerCase(), color);
        }
    }

    public static Color getColor(String name){
        if ( name==null || name.length() ==0 ){
            return Color.UNKOWN;
        }
        String key = name.trim().toLowerCase();
        if ( map.containsKey(key) ) {
            return map.get(key);
        }

        return Color.UNKOWN;
    }


    private int value;

    Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
