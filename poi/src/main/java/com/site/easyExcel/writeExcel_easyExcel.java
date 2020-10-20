package com.site.easyExcel;


import com.site.pojo.DemoData;
import com.site.pojo.Entity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 案例采用了https://www.yuque.com/easyexcel/doc/easyexcel
 * <p>
 * 03年的Excel表格最多只有65536行，文件名结尾为 .xls
 * 而07年以候表格的行数就没有了限制，文件名结尾为 .xlsx
 */
public class writeExcel_easyExcel {


    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }


    ///**
    // * 最简单的写
    // * <p>1. 创建excel对应的实体对象
    // * <p>2. 直接写即可
    // * <p>3. 它是根据list写入Excel
    // */
    //@Test
    //public void simpleWrite1() {
    //    String fileName = Entity.PATH + "/test.xlsx";
    //    // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    //    // 如果这里想使用03 (是一种数据类型)则 传入excelType参数即可
    //    //参数说明 fileName（文件路径+名字），DemoData.class（生成规则，会按照这个类生成数据）
    //    // "模板"（生成表的名字），data()(list集合，里面是DemoData对象)
    //    EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
    //}

    @Test
    public void simpleWrite2() {

    }

}
