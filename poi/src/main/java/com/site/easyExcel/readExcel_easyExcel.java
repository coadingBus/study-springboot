package com.site.easyExcel;



/**
 * 字段匹配
 *
 * 多行头
 *
 * 读表头
 *
 * 额外信息（批注、超链接、合并单元格信息读取）
 *
 * 读取公式和单元格类型
 *
 * 不创建对象的读
 *
 * web中的读
 *
 *
 */
public class readExcel_easyExcel {

    ///**
    // * 最简单的读
    // * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
    // * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
    // * <p>3. 直接读即可
    // */
    //@Test
    //public void simpleRead1() {
    //    //有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    //    //写法1：
    //    // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
    //    long currentTimeMillis1 = System.currentTimeMillis();
    //    //参数说明：fileName是文件的路径+文件名，
    //    // DemoData.class是根据Excel表创建的类，会将读取的数据
    //    //new DemoDataListener()是监听器
    //    //实现监听器也很简单，只要继承一下 AnalysisEventListener，重写里面的invoke方法，实现具体的代码逻辑
    //    EasyExcel.read(fileName, Excel2.class, new DemoDataListener()).sheet().headRowNumber(2).doRead();
    //    long currentTimeMillis2 = System.currentTimeMillis();
    //    long count = currentTimeMillis2 - currentTimeMillis1;
    //    //因为是一行行读取数据所以效率上比不上poi，但easyExcel的底层源码也是poi只是对poi进行了进一步的封装
    //    System.out.println("总共时间" + count);
    //}
    //
    //
    //@Test
    //public void simpleRead2() {
    //    // 写法2：
    //    ExcelReader excelReader = null;
    //    try {
    //        excelReader = EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).build();
    //        //这里是读取第一个sheet
    //        ReadSheet readSheet = EasyExcel.readSheet(0).build();
    //        excelReader.read(readSheet);
    //    } finally {
    //        if (excelReader != null) {
    //            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
    //            excelReader.finish();
    //        }
    //    }
    //}
    //
    ///**
    // * 指定列的下标或者列名
    // *
    // * <p>1. 创建excel对应的实体对象,并使用{@link ExcelProperty}注解. 参照{@link IndexOrNameData}
    // * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link IndexOrNameDataListener}
    // * <p>3. 直接读即可
    // */
    //@Test
    //public void indexOrNameRead() {
    //    // 这里默认读取第一个sheet
    //    EasyExcel.read(fileName, IndexOrNameData.class, new DemoDataListener()).sheet().doRead();
    //}
    //
    ///**
    // * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
    // * <p>
    // * 1. 创建excel对应的实体对象 参照{@link DemoData}
    // * <p>
    // * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
    // * <p>
    // * 3. 直接读即可
    // */
    //@Test
    //public void repeatedRead() {
    //
    //    // 读取全部sheet
    //    // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
    //    EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).doReadAll();
    //
    //    // 读取部分sheet
    //    ExcelReader excelReader = null;
    //    try {
    //        excelReader = EasyExcel.read(fileName).build();
    //        // 这里为了简单 所以注册了同样的head和Listener 自己使用功能必须不同的Listener
    //        ReadSheet readSheet1 =
    //                EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
    //        ReadSheet readSheet2 =
    //                EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
    //        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
    //        excelReader.read(readSheet1, readSheet2);
    //    } finally {
    //        if (excelReader != null) {
    //            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
    //            excelReader.finish();
    //        }
    //    }
    //}
    //
    //
    ///**
    // * 多行头
    // *
    // * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
    // * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
    // * <p>3. 设置headRowNumber参数，然后读。 这里要注意headRowNumber如果不指定， 会根据你传入的class的{@link ExcelProperty#value()}里面的表头的数量来决定行数，
    // * 如果不传入class则默认为1.当然你指定了headRowNumber不管是否传入class都是以你传入的为准。
    // */
    //@Test
    //public void complexHeaderRead() {
    //    // 这里 需要指定读用哪个class去读，然后读取第一个sheet
    //    EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet()
    //            // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
    //            .headRowNumber(1).doRead();
    //}


}
