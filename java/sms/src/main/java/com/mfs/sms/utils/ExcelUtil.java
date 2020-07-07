package com.mfs.sms.utils;

import com.mfs.sms.pojo.Product;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    private static List<String> title = Arrays.asList("商品名", "类型编号","供货商","保质期(月)","提醒(天)","进价","售价","编号","生产日期","数量","预警库存");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private ExcelUtil(){}

    public static String write(String path, List<Product> list) {
        File file  = new File(path);
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            for (int i = 0;i < title.size(); i ++) {
                sheet.addCell(new Label(i,0,title.get(i)));
            }
            int i = 1;
            for (Product product : list) {
                sheet.addCell(new Label(0,i,product.getName()));
                sheet.addCell(new Label(1,i,product.getTypeId().toString()));
                sheet.addCell(new Label(2,i,product.getManufacturer()));
                sheet.addCell(new Label(3,i,sdf.format(product.getProductDate())));
                sheet.addCell(new Label(4,i,product.getSelfLife().toString()));
                sheet.addCell(new Label(5,i,product.getWarnBefore().toString()));
                sheet.addCell(new Label(6,i,product.getInPrice().toString()));
                sheet.addCell(new Label(7,i,product.getOutPrice().toString()));
                i ++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getName();

    }

    public static List<Map<String,String>> resd(String path) {
        List<Map<String,String>> list = new ArrayList<>();
        try {
            File file = new File(path);
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet[] sheets = workbook.getSheets();
            if (sheets != null) {
                Sheet sheet = sheets[0];
                int rows = sheet.getRows();
                int columns = sheet.getColumns();
                //System.out.println(columns);
                for (int i = 1; i < rows; i ++) {
                    Map<String,String> map = new HashMap<>();
                    for (int j = 0; j < columns; j ++) {
                        String title = sheet.getCell(j,0).getContents();
                        String content = sheet.getCell(j,i).getContents();
                        //System.out.println(title);
                        if (title == null || title.equals("")) {
                            break;
                        }
                        map.put(title,content);
                    }
                    list.add(map);
                }
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
