package cn.thd;

public class ColorMapping {


    public static String getMatchColor(String model, Order.Type type){

        if ( model.equalsIgnoreCase("DR71") ){
            if ( type.equals(Order.Type.GEARMOTOR) ) {
                return "6303";
            }
            if ( type.equals(Order.Type.IEC_MOTOR) ) {
                return "6204";
            }
        }

        if ( model.equalsIgnoreCase("DR80") ){
            if ( type.equals(Order.Type.GEARMOTOR) ) {
                return "6304";
            }
            if ( type.equals(Order.Type.IEC_MOTOR) ) {
                return "6205";
            }
        }

        if ( model.equalsIgnoreCase("DR90")
                || model.equalsIgnoreCase("DR100") ){
            return "6306";
        }

        if ( model.equalsIgnoreCase("DR112")
                || model.equalsIgnoreCase("DR132") ){
            return "6308";
        }

        throw  new RuntimeException("找不到对应的工装型号.[型号： " +  model + "， 类型" +  type + "]");
    }
}
