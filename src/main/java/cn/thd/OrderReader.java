package cn.thd;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderReader {

    private static  Pattern pattern = Pattern.compile("DR[a-zA-Z]([\\d]*)");

    public static void main(String[] args) throws IOException {


        String s = "DRE132S4/FF/ES7S/V";
        // 把要匹配的字符串写成正则表达式，然后要提取的字符使用括号括起来
        // 在这里，我们要提取最后一个数字，正则规则就是“一个数字加上大于等于0个非数字再加上结束符”


        System.out.println("======");


        String dir = "D:\\JPOS\\data";


        Order result = readInfo(dir, "50332942.txt");

        System.out.println("型号: " + result.getGl_katalogbez_a());
        System.out.println("型号: " + result.getType());
        System.out.println("法兰端: " + result.getAbtriebswelle());
        System.out.println("法兰端: " + result.getaSide());

        System.out.println("工装型号: " + ColorMapping.getMatchColor(result.getType(), result.getaSide()));

        System.out.println(" === ");

        for (String key : result.getResult().keySet()) {
           // System.out.println(key + ": " + result.getResult().get(key));
        }

    }

    public static Order readInfo(String dir, String name) throws IOException {


        String fileName = dir + File.separator + name;

        File file = new File(fileName);
        if ( !file.exists()){
            throw new RuntimeException("文件不存在[" + fileName + "]");
        }

        Map<String, String> result = new HashMap<>();
       List<String> lines = Files.readLines(file, Charsets.UTF_8);


        Order order = new Order();

        System.out.println("文件行数： " + lines.size());
        for (String line : lines) {

            System.out.println("长度： " + line.length());
            result.put("工单数量: ", line.substring(0, 3));
            result.put("工单数量:", line.substring(0, 3));
            result.put("型号:", line.substring(4, 33));

            String type = line.substring(4, 33);
            order.setGl_katalogbez_a(type);
            Matcher matcher = pattern.matcher(type);
            if(matcher.find()) {
                order.setType("DR" + matcher.group(1));
            } else {
                order.setType(type);
            }

            result.put("安装方式:", line.substring(34, 63));
            result.put(":", line.substring(64, 75));
            result.put("输出轴:", line.substring(76, 90));

            String aside = line.substring(76, 90);
            order.setAbtriebswelle(aside);
            if ( aside.contains("28x60mm lg") ){
                order.setaSide(Order.Type.IEC_MOTOR);
            } else if ( aside.contains("pinion") ) {
                order.setaSide(Order.Type.GEARMOTOR);
            }

            result.put("空心轴:", line.substring(91, 105));
            result.put("法兰尺寸:", line.substring(106, 120));
            result.put("润滑剂量:", line.substring(121, 131));
            result.put("1:", line.substring(132, 151));
            result.put("功率:", line.substring(152, 158));
            result.put("工作制:", line.substring(159, 170));
            result.put("电压:", line.substring(171, 177));
            result.put("电流（普通电机及双频电机50Hz下电流1）:", line.substring(178, 186));
            result.put("功率因素:", line.substring(187, 193));
            result.put("制动电压:", line.substring(194, 197));
            result.put("防护等级:", line.substring(198, 200));
            result.put("接线盒角度:", line.substring(201, 206));
            result.put("电机保护:", line.substring(207, 210));
            result.put("频率:", line.substring(211, 217));
            result.put("接线图:", line.substring(218, 221));
            result.put("接线方式:", line.substring(222, 251));
            result.put("绝缘等级:", line.substring(252, 252));
            result.put("转速（各类电机低速）:", line.substring(253, 256));
            result.put("序号:", line.substring(257, 286));
            result.put("2:", line.substring(287, 294));
            result.put("电流（普通电机及双频电机50Hz下电流2）:", line.substring(295, 303));
            result.put("电流（普通电机及双频电机60Hz下电流1）:", line.substring(304, 312));
            result.put("电流（普通电机及双频电机60Hz下电流2）:", line.substring(313, 321));
            result.put("转速（各类电机高速）:", line.substring(322, 325));
            result.put("3:", line.substring(326, 336));
            result.put("功率（双频电机60Hz及双速电机高速下）:", line.substring(337, 342));
            result.put("功率因数(双频高频下或双速电机高速下):", line.substring(343, 348));
            result.put("电压（双频电机50Hz下）:", line.substring(349, 378));
            result.put("电压（双频电机60Hz下）:", line.substring(379, 408));
            result.put("频率（双频电机低频）:", line.substring(409, 414));
            result.put("频率（双频电机高频）:", line.substring(415, 420));
            result.put("电压（宽电压电机电机电压范围）:", line.substring(421, 450));
            result.put("制动电压（宽电压电机电压）:", line.substring(451, 480));

            result.put("Other:", line.substring(481));


            System.out.println(line);

            order.setResult(result);
        }

        return order;
    }


}


