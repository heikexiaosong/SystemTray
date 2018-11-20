package cn.thd;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrderReader {

    public static void main(String[] args) throws IOException {

        String dir = "D:\\JPOS\\data";

        
        readInfo(dir, "50332942.txt");

    }

    private static void readInfo(String dir, String name) throws IOException {


        String fileName = dir + File.separator + name;

        File file = new File(fileName);
        if ( !file.exists()){
            throw new RuntimeException("文件不存在[" + fileName + "]");
        }

       List<String> lines = Files.readLines(file, Charsets.UTF_8);

        System.out.println("文件行数： " + lines.size());
        for (String line : lines) {

            System.out.println("长度： " + line.length());

            System.out.println("工单数量: " + line.substring(0, 3));

            System.out.println("工单数量:" + line.substring(0, 3));
            System.out.println("型号:" + line.substring(4, 33));
            System.out.println("安装方式:" + line.substring(34, 63));
            System.out.println(":" + line.substring(64, 75));
            System.out.println("输出轴:" + line.substring(76, 90));
            System.out.println("空心轴:" + line.substring(91, 105));
            System.out.println("法兰尺寸:" + line.substring(106, 120));
            System.out.println("润滑剂量:" + line.substring(121, 131));
            System.out.println(":" + line.substring(132, 151));
            System.out.println("功率:" + line.substring(152, 158));
            System.out.println("工作制:" + line.substring(159, 170));
            System.out.println("电压:" + line.substring(171, 177));
            System.out.println("电流（普通电机及双频电机50Hz下电流1）:" + line.substring(178, 186));
            System.out.println("功率因素:" + line.substring(187, 193));
            System.out.println("制动电压:" + line.substring(194, 197));
            System.out.println("防护等级:" + line.substring(198, 200));
            System.out.println("接线盒角度:" + line.substring(201, 206));
            System.out.println("电机保护:" + line.substring(207, 210));
            System.out.println("频率:" + line.substring(211, 217));
            System.out.println("接线图:" + line.substring(218, 221));
            System.out.println("接线方式:" + line.substring(222, 251));
            System.out.println("绝缘等级:" + line.substring(252, 252));
            System.out.println("转速（各类电机低速）:" + line.substring(253, 256));
            System.out.println("序号:" + line.substring(257, 286));
            System.out.println(":" + line.substring(287, 294));
            System.out.println("电流（普通电机及双频电机50Hz下电流2）:" + line.substring(295, 303));
            System.out.println("电流（普通电机及双频电机60Hz下电流1）:" + line.substring(304, 312));
            System.out.println("电流（普通电机及双频电机60Hz下电流2）:" + line.substring(313, 321));
            System.out.println("转速（各类电机高速）:" + line.substring(322, 325));
            System.out.println(":" + line.substring(326, 336));
            System.out.println("功率（双频电机60Hz及双速电机高速下）:" + line.substring(337, 342));
            System.out.println("功率因数(双频高频下或双速电机高速下):" + line.substring(343, 348));
            System.out.println("电压（双频电机50Hz下）:" + line.substring(349, 378));
            System.out.println("电压（双频电机60Hz下）:" + line.substring(379, 408));
            System.out.println("频率（双频电机低频）:" + line.substring(409, 414));
            System.out.println("频率（双频电机高频）:" + line.substring(415, 420));
            System.out.println("电压（宽电压电机电机电压范围）:" + line.substring(421, 450));
            System.out.println("制动电压（宽电压电机电压）:" + line.substring(451, 480));

            System.out.println("Other:" + line.substring(481));


            System.out.println(line);
        }
    }


}


