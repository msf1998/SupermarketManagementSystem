package com.mfs.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Controller
@RequestMapping("/static/download")
public class DownloadController {

    @RequestMapping("/qcode")
    public String qrCodeDownload(HttpServletResponse response, String fileName) {
        //System.out.println(fileName);
        File file = new File("E:/images/sms/product/qCode/"+fileName);
        response.setContentType("application/octet-stream");
        response.setHeader("content-type","application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            FileInputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            ServletOutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = bis.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
            os.close();
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
