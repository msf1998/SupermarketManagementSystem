package com.mfs.sms.controller;

import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.serviceImpl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @Author lzc
     * */
    //修改将要过期的商品
    @RequestMapping("/edit/will/go-bad")
    public Result editWillGoBadProduct(@RequestBody Product product, HttpServletRequest request) {
        try {
            return productService.editWillGoBadProduct(product,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(3,"服务器异常",null,null);        }
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

    //增删改查
    /**
     * @Author lzc
     * */
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
}
