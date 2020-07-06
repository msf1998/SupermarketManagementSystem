package com.mfs.sms.utils;

import com.mfs.sms.pojo.Product;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ExcelUtil {
    private static List<String> title = Arrays.asList("编号","商品名","生产商","生产日期","保质期","预警库存","数量","进价",
            "售价","类型");
    private ExcelUtil(){}

    public static File write(String path, List<Product> list) {
        File file = new File(path);
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            for (int i = 0;i < title.size(); i ++) {
                sheet.addCell(new Label(i,0,title.get(i)));
            }
            int i = 1;
            for (Product product : list) {

                i ++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }
}
