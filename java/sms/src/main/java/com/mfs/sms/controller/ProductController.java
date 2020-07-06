package com.mfs.sms.controller;

import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/leading-out/purchase-order")
    public Result createPurchaseOrder(@RequestBody Product product, HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.createPurchaseOrder(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);
        }
    }
    @RequestMapping("/delete/count")
    public Result deleteCountProduct(@RequestBody Product product, HttpServletRequest request) {
        //System.out.println(product);
        try {
            return productService.deleteCountProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    @RequestMapping("/edit/selflife")
    public Result selfLifeWarn(@RequestBody Product product, HttpServletRequest request) {
        try {
            return productService.editSelfLifeProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    @RequestMapping("/selflife")
    public Result selfLifeWarn(HttpServletRequest request) {
        try {
            return productService.selfLifeWarn(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
    @RequestMapping("/count")
    public Result countWarn(HttpServletRequest request) {
        try {
            return productService.countWarn(new CompareObj("warn_count","count"),request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
    }
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
