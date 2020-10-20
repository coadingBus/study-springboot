package com.site.poi;

import com.site.pojo.Entity;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

public class writeExcel_poi {


    /**
     * 通过上面的入门案例可以看到，POI操作Excel表格封装了几个核心对象：
     * XSSFWorkbook：工作簿   XSSFSheet：工作表   Row：行   Cell：单元格
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(Entity.FORM1);
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获得单元格中的值
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }

    @Test
    public void test01() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("H:\\CSDN\\POI\\统计表03.xls");
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);//获取第1个工作表
        Row row = sheet.getRow(0);//得到一行
        Cell cell = row.getCell(0);//获取第一列
        System.out.println(cell.getStringCellValue());
        fileInputStream.close();
    }

    @Test
    public void test2() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("名称");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");
        //通过输出流将workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream(Entity.PATH + "/5.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
    }

    @Test
    public void test3() throws Exception {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/template.xls";

        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
        HSSFSheet sheet = workbook.getSheet("Sheet1");

        int lastRowIndex = sheet.getLastRowNum();
        System.out.println(lastRowIndex);
        for (int i = 0; i <= lastRowIndex; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }

            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                String cellValue = row.getCell(j).getStringCellValue();
                System.out.println(cellValue);
            }
        }


        bufferedInputStream.close();
    }

//    @Test
//    public void test03() throws IOException {
//        FileInputStream fileInputStream = new FileInputStream("H:\\CSDN\\POI\\java35班.xlsx");
//
//        Workbook workbook = new XSSFWorkbook(fileInputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        Row row = sheet.getRow(0);//获取标题
//        if(row!=null){//如果第一行不为空
//            int cellncount = row.getPhysicalNumberOfCells();//获取第一行的第一列数据
//            for (int cellnum = 0; cellnum < cellncount; cellnum++) {
//                Cell cell = row.getCell(cellnum);
//                if(cell!=null){
//                    CellType cellType = cell.getCellType();
//                    String stringCellValue = cell.getStringCellValue();
//                    System.out.print(stringCellValue+"|");
//                }
//            }
//            System.out.println();
//        }
//        //获取表中的内容
//        int rowcount = sheet.getPhysicalNumberOfRows();
//        for (int rownum = 1; rownum < rowcount; rownum++) {//获取行的数据
//            Row rowData = sheet.getRow(rownum);
//            if(rowData!=null){
//                //读取列
//                int cellcount = row.getPhysicalNumberOfCells();
//                for (int celnum = 0; celnum < cellcount; celnum++) {
////                    System.out.print("【"+rownum+1+"】"+(celnum+1));
//                    Cell cell = rowData.getCell(celnum);
//                    /*判断数据类型*/
//                    if(cell!=null){
//                        CellType cellType = cell.getCellType();
//                        String cellValue="";
//                        switch (cellType){
//                            case HSSFCell.CELL_TYPE_STRING://string类型
//                                //System.out.println("【String】");
//                                cellValue=cell.getStringCellValue();
//                                break;
//                            case HSSFCell.CELL_TYPE_BOOLEAN://布尔类型
//                                //  System.out.println("【boolean】");
//                                cellValue=String.valueOf(cell.getBooleanCellValue());
//                                break;
//                            case HSSFCell.CELL_TYPE_NUMERIC: //数字和日期类型
//                                System.out.println("【NUMBER】");
//                                if(HSSFDateUtil.isCellDateFormatted(cell)){
//                                    //  System.out.println("【日期】");
//                                    Date date=cell.getDateCellValue();
//                                    cellValue=new DateTime(date).toString("yyy-MM-dd");
//                                }else{
//                                    //   System.out.println("【转换为字符串输出】");
//                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                                    cellValue=cell.toString();
//                                }
//                            case HSSFCell.CELL_TYPE_ERROR://布尔类型
//                                //  System.out.println("【数据类型错误】");
//                                cellValue=String.valueOf(cell.getErrorCellValue());
//                                break;
//                        }
//                        System.out.print(cellValue+"\n");
//                    }
//                }
//            }
//
//        }
//
//        fileInputStream.close();
//    }
//
//    //计算公式
//    @Test
//    public void test04() throws IOException {
//        FileInputStream fileInputStream = new FileInputStream("H:\\CSDN\\POI\\统计表07.xlsx");
//        Workbook workbook = new XSSFWorkbook(fileInputStream);
//        Sheet sheet = workbook.getSheetAt(0);//获取第1个工作表
//        Row row = sheet.getRow(4);//得到一行
//        Cell cell = row.getCell(0);
//        //拿到计算公式
//        FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
//        //拿到单元格内容
//        CellType cellType = cell.getCellType();
//        switch (cellType){
//            case Cell.CELL_TYPE_FORMULA://公式
//                String cellFormula = cell.getCellFormula();
//                System.out.println(cellFormula);//输出公式
//                //计算
//                CellValue evaluate = formulaEvaluator.evaluate(cell);
//                String cellValue = evaluate.formatAsString();
//                System.out.println(cellValue);
//        }
//        fileInputStream.close();
//    }
    @Test
    public void test4() {
    }

    @Test
    public void test5() throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\员工信息.xlsx");
        //获得该文件的输入流
        FileInputStream stream = new FileInputStream(file);
        // 多态  抛异常
        Workbook sheets = new XSSFWorkbook(stream);
        //获取一个工作表(sheet页)，下标从0开始
        Sheet sheet = sheets.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {


            // 获取行数
            Row row = sheet.getRow(i);
            // 获取单元格 取值
            String value1 = row.getCell(0).getStringCellValue();
            String value2 = row.getCell(1).getStringCellValue();
            String value3 = row.getCell(2).getStringCellValue();
            String value4 = row.getCell(3).getStringCellValue();
            String value5 = row.getCell(4).getStringCellValue();

            System.out.println(value1);
            System.out.println(value2);
            System.out.println(value3);
            System.out.println(value4);
            System.out.println(value5);
        }


        //关流
        sheets.close();
        stream.close();
    }
}

