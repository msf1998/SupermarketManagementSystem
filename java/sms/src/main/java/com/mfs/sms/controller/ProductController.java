package com.mfs.sms.controller;

import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    //导入进货单
    @RequestMapping("/leading-in/photo")
    public Result leadingInPhoto(HttpServletRequest request, @RequestParam("file")MultipartFile file,@RequestParam("id") String id){
        try {
            return productService.editProductPhoto(request,file,id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    //导入进货单
    @RequestMapping("/leading-in/purchase-order")
    public Result leadingInPurchaseOrder(HttpServletRequest request, @RequestParam("file")MultipartFile file){
        try {
            return productService.leadingInPurchaseOrder(request,file);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    //导出进货单
    @RequestMapping("/leading-out/purchase-order")
    public Result createPurchaseOrder( HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.createPurchaseOrder(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    //删除库存不足的商品
    @RequestMapping("/delete/less-than/warn-count")
    public Result deleteCountLessThanWarnCountProduct(@RequestBody Product product, HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.deleteCountLessThanWarnCountProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    //修改将要过期的商品
    @RequestMapping("/edit/will/go-bad")
    public Result editWillGoBadProduct(@RequestBody Product product, HttpServletRequest request) {
        try {
            return productService.editWillGoBadProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    //获取将要过期的的商品的列表
    @RequestMapping("/list/will/go-bad")
    public Result listWillGoBadProduct(HttpServletRequest request) {
        try {
            return productService.listWillGoBadProduct(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    //获取库存过少的商品的列表
    @RequestMapping("/list/less-than/warn-count")
    public Result listCountLessThanWarnCountProduct(HttpServletRequest request) {
        try {
            return productService.listCountLessThanWarnCountProduct(new CompareObj("warn_count","count"),request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }

    //增删改查
    @RequestMapping("/add")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result addProduct(@RequestBody  Product product, HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.addProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result deleteProduct(@RequestBody Product product,HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.deleteProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/edit")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result editProduct(@RequestBody Product product,HttpServletRequest request) {
        System.out.println(product);
        try {
            return productService.editProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/list")
    //@CrossOrigin(origins = {"*"},allowCredentials = "true")
    public Result listProducts(@RequestBody Product product, HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.listProducts(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
}
