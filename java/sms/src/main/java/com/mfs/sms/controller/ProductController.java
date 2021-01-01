package com.mfs.sms.controller;

import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.SpringCglibInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RequestMapping("/api/product")
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;


    /**
     * 导出进货单
     * */
    @RequestMapping("/purchase-order/download")
    public void createPurchaseOrder(HttpServletResponse response,HttpServletRequest request, Principal principal) {
        try {
            productService.createPurchaseOrder(principal,request,response);
        } catch (Exception e) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=3");
            e.printStackTrace();
        }
    }


    /**
     * 下载商品二维码
     * */
    @RequestMapping("/qcode/download")
    public void downloadQCode(String name, Principal principal,HttpServletRequest request, HttpServletResponse response) {
        try {
            productService.downloadQCode(principal,request, response,name);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error");
            return;
        }
    }

    /**
     * 添加商品
     * */
    @RequestMapping("/add")
    @ResponseBody
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addProduct(Product product, Principal principal) {
        //System.out.println(product);
        try {
            return productService.addProduct(principal,product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 导入进货单来添加商品
     * */
    @RequestMapping("/add/excel")
    @ResponseBody
    public Result leadingInPurchaseOrder(@RequestParam("file")MultipartFile file,Principal principal){
        try {
            return productService.leadingInPurchaseOrder(principal,file);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 删除指定商品
     * */
    @RequestMapping("/delete")
    @ResponseBody
    public Result deleteProduct(Product product,Principal principal) {
        //System.out.println(product);
        try {
            return productService.deleteProduct(principal, product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改指定商品
     * */
    @RequestMapping("/edit")
    @ResponseBody
    public Result editProduct(Product product,Principal principal) {
        //System.out.println(product);
        try {
            return productService.editProduct(principal, product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改商品图片
     **/
    @RequestMapping("/edit/photo")
    @ResponseBody
    public Result leadingInPhoto(@RequestParam("file")MultipartFile file,@RequestParam("id") String id,Principal principal){
        try {
            return productService.editProductPhoto(principal,file,id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 修改将要过期的商品
     * */
    @RequestMapping("/edit/bad")
    @ResponseBody
    public Result editWillGoBadProduct(Product product,Principal principal) {
        try {
            return productService.editWillGoBadProduct(principal,product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }

    /**
    * 获取商品（分页）
    * */
    @RequestMapping("/list")
    @ResponseBody
    public Result listProducts(Principal principal, Product product) {
        //System.out.println(product);
        try {
            return productService.listProducts(principal, product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 根据id获取商品
     * */
    @RequestMapping("/get")
    @ResponseBody
    public Result getProduct(Product product, Principal principal) {
        try {
            return productService.getProduct(principal,product);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }

    /**
     * 获取将要变质的商品
     * */
    @RequestMapping("/list/bad")
    @ResponseBody
    public Result listWillGoBadProduct(Principal principal) {
        try {
            return productService.listWillGoBadProduct(principal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }

    /**
     * 获取库存过少的商品的列表
     * */
    @RequestMapping("/list/less")
    @ResponseBody
    public Result listCountLessThanWarnCountProduct(Principal principal) {
        try {
            return productService.listCountLessThanWarnCountProduct(principal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
}
