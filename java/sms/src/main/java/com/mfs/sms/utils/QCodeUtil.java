package com.mfs.sms.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class QCodeUtil {
    private static int width =  300;
    private static int height = 300;
    private static String format = "png";
    private static String path = "E:/images/sms/product/qcode/";

    public static String createQRCode(String contents){
        HashMap map = new HashMap();
        map.put(EncodeHintType.CHARACTER_SET,"utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN,5);
        try {
            BitMatrix bm = new MultiFormatWriter().encode(contents, BarcodeFormat.AZTEC.QR_CODE, width, height);
            String name = UUID.randomUUID().toString()+"."+format;
            String filePath = path + "" + name;
            File file = new File(filePath);
            Path path = file.toPath();
            MatrixToImageWriter.writeToPath(bm,format,path);
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
