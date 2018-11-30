package cn.thd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaftExtention {

    private static final Pattern RZ_PATTERN = Pattern.compile("([0-9]*).*pinion");

    private static final Pattern IEC_PATTERN = Pattern.compile("([0-9x]*)");

    /**
     *   轴伸大小
     *
     *   shaft_extension中有 pinion时对应RZ, 无pinon时对应IEC
     *
     *   10mm pinion sha  ==>  RZ10
     *   14x30mm lg.      ==>  IEC14x30
     *
     * @param abtriebswelle 输出轴型号
     *
     * @return
     */
    public static String parse(String abtriebswelle){

        if ( abtriebswelle==null || abtriebswelle.trim().length()==0 ){
            return abtriebswelle;
        }

        Matcher matcher_rz =  RZ_PATTERN.matcher(abtriebswelle);
        if ( matcher_rz.find() ) {
            return "RZ" + matcher_rz.group(1);
        }


        Matcher matcher_iec =  IEC_PATTERN.matcher(abtriebswelle);
        if ( matcher_iec.find() ) {
            return "IEC" + matcher_iec.group(1);
        }

        return abtriebswelle;
    }

    public static void main(String[] args) {

        System.out.println(parse("10mm pinion sha"));   // RZ10


        System.out.println(parse("14x30mm lg."));       // IEC14x30

    }

}
