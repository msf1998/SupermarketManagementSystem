package com.mfs.sms.serviceImpl;

import com.mfs.sms.exception.DatabaseUpdateException;
import com.mfs.sms.mapper.ProductMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.CompareObj;
import com.mfs.sms.pojo.Product;
import com.mfs.sms.pojo.Result;
import com.mfs.sms.pojo.User;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.ExcelUtil;
import com.mfs.sms.utils.QCodeUtil;
import com.mfs.sms.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Filter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductService {
    @Value("${web.upload-path}")
    private String rootPath;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    UserService userService;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

    /**
     * 根据id获取商品
     * @param principal 登录主体
     * @param product 查询参数
     * @return Result
    * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result getProduct(Principal principal, Product product){
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }

        Product product1 = productMapper.queryById(product.getId());
        if(product1 == null) {
            return new Result(2,"没有该商品",null,null);
        }
        if (product1.getCount() < product.getCount()) {
            return new Result(2,"库存不足,仅剩" + product1.getCount() + "件",null,null);
        }

        product1.setCount(product.getCount());

        return new Result(1,"查询成功",product1,null);
    }

    //修改商品图片
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editProductPhoto(HttpServletRequest request, MultipartFile file,String id) throws Exception {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        String name = file.getOriginalFilename();
        Pattern compile = Pattern.compile("^.*\\.png$|^.*\\.jpg$");
        Matcher matcher = compile.matcher(name);
        if (!matcher.matches()) {
            return new Result(2,"文件格式不正确",null,null);
        }
        String photoName = UUID.randomUUID().toString() + new Date().getTime() + (name.endsWith(".png") ? ".png" : ".jpg");
        String path = "E:/images/sms/product/" + photoName;
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        OutputStream os  = new FileOutputStream(f);
        InputStream is = file.getInputStream();
        int n = 0;
        byte[] b = new byte[1024];
        while ((n = is.read(b)) > 0) {
            os.write(b,0,n);
        }
        is.close();
        os.close();
        Product product = new Product();
        product.setId(id);
        product.setPhoto(photoName);
        int res = productMapper.update(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> l = productMapper.query(product1);
            return new Result(1,"修改成功",l,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(1,"修改失败",null,null);
        }
    }
    //导入进货单
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result leadingInPurchaseOrder(HttpServletRequest request, MultipartFile file) throws Exception {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        String name = file.getOriginalFilename();
        Pattern compile = Pattern.compile("^.*\\.xls$");
        Matcher matcher = compile.matcher(name);
        if (!matcher.matches()) {
            return new Result(2,"文件格式不正确",null,null);
        }
        String path = "E:/images/sms/product/excel/leading-in/" + userId + new Date().getTime() + ".xls";
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        OutputStream os  = new FileOutputStream(f);
        InputStream is = file.getInputStream();
        int n = 0;
        byte[] b = new byte[1024];
        while ((n = is.read(b)) > 0) {
            os.write(b,0,n);
        }
        is.close();
        os.close();
        List<Map<String, String>> list = ExcelUtil.resd(path);
        for (Map<String,String> m : list) {
            String id = m.get("编号") + new Date().getTime();
            Product product = new Product(id,m.get("商品名"),"default.jpg",m.get("供货商"),
                    sdf.parse(m.get("生产日期")),Integer.parseInt(m.get("保质期(月)")),Integer.parseInt(m.get("提醒(天)")),
                    Integer.parseInt(m.get("数量")),Integer.parseInt(m.get("预警库存")),new Date(),Double.parseDouble(m.get("进价")),
            Double.parseDouble(m.get("售价")),Integer.valueOf(userId),null,Integer.parseInt(m.get("类型编号")),null,QCodeUtil.createQRCode(id),null,null);
            int res = productMapper.add(product);
            if (res != 1) {
                int i = 1 / 0;
            }
        }
        Product product1 = new Product();
        product1.setOrder("name");
        product1.setPage(0);
        List<Product> l = productMapper.query(product1);
        return new Result(1,"导入成功",l,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }

    /**
     * 导出进货单
     * @param principal 已登录的用户主体
     * @param response HttpServletResponse
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public void createPurchaseOrder(Principal principal, HttpServletRequest request, HttpServletResponse response) throws DatabaseUpdateException {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=4");
        }
        if (!user.getRole().getProductUpdate()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=5");
        }

        //创建excel
        List<Product> list = productMapper.queryGreaterThan();
        String path = rootPath + "/product/excel/leading-out/" + username + new Date().getTime() + ".xls";
        String file = ExcelUtil.write(path, list);
        File f = new File(rootPath + "/product/excel/leading-out/" + file);
        if (!f.exists()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=2");
        }

        //将处理过的产品类型改为积压类型
        int res = 1;
        for (Product p : list) {
            p.setTypeId(3);
            res = res * productMapper.update(p);
        }

        //将文件写入到response
        if (res == 1){
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;fileName=" + file);
            InputStream is = null;
            ServletOutputStream os = null;
            try {
                is = new FileInputStream(f);
                os = response.getOutputStream();
                byte[] b = new byte[1024];
                int n = 0;
                while ((n = is.read(b)) > 0) {
                    os.write(b);
                }
             } catch (IOException e) {
                response.setStatus(302);
                response.setHeader("location",request.getContextPath() + "/error?status=3");
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                    f.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            f.delete();
            throw new DatabaseUpdateException("修改产品类型");
        }
    }

    /**
     * @Author lzc
     * */
    //修改快过期的商品
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result editWillGoBadProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        //将修改的商品类型改为促销，并把提醒日期值变为0
        product.setTypeId(2);
        product.setWarnBefore(0);
        int res = productMapper.update(product);
        if (res == 1) {
            List<Product> list = productMapper.queryBySelfLife();
            return new Result(1,"修改成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }
    /**
     * 获取即将过期的产品
     * @param principal 已登陆的用户主体
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listWillGoBadProduct(Principal principal) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        List<Product> list = productMapper.queryBySelfLife();
        return new Result(1,"查询成功",list,null);
    }

    /**
     * 获取库存过少的商品列表
     * @param principal 已登录的用户主体
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listCountLessThanWarnCountProduct(Principal principal) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        List<Product> list = productMapper.queryGreaterThan();
        //System.out.println(list);
        return new Result(1,"查询成功",list,null);
    }


    //增删改查
    /**
     * @Author lzc
     * */
    @Transactional(isolation = Isolation.DEFAULT,timeout = 5)
    public Result addProduct(Product product, HttpServletRequest request) {
        //验证是否登录
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        product.setId(product.getId() + new Date().getTime());
        product.setPhoto("default.jpg");
        product.setParentId(user.getId());
        product.setInTime(new Date());
        //创建商品码
        String qrCode = QCodeUtil.createQRCode(product.getId());
        product.setQCode(qrCode);
        int res = productMapper.add(product);
        if (res == 1) {
            Product product1 = new Product();
            product1.setOrder("name");
            product1.setPage(0);
            List<Product> list = productMapper.query(product1);
            //更新token
            return new Result(1,"添加成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    /**
     * 删除指定商品（可以通过商品id和名称删除）
     * @param principal 已登录的用户主体
     * @param product 要修改的商品
     * @return Result
     **/
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result deleteProduct(Principal principal, Product product) throws DatabaseUpdateException {
        //检查参数
        Result result = delegatingParameterObjectCheck(product, "deleteProduct");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductDelete()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        List<Product> list = productMapper.query(product);
        //删除数据库数据
        int res = productMapper.delete(product);
        if (res >= 1) {
            for (Product p : list) {
                //删除二维码
                File file = new File(rootPath + "/product/qcode/" + p.getQCode());
                if (file.exists()) {
                    file.delete();
                }
                if (!p.getPhoto().equals("default.jpg")) {
                    //删除图片
                    file = new File(rootPath + "/product/" + p.getPhoto());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            return new Result(1,"删除成功",null,null);
        } else {
            throw new DatabaseUpdateException("删除商品失败");
        }
    }

    /**
     * 修改产品信息（至少包含商品id）
     * @param principal 已登录的用户主体
     * @param product 要修改的产品信息
     * @return Result
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result editProduct(Principal principal, Product product) throws DatabaseUpdateException {
        System.out.println(product);
        //参数检查
        Result result = delegatingParameterObjectCheck(product, "editProduct");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductUpdate()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //更新数据库
        int res = productMapper.update(product);
        if (res == 1) {
            return new Result(1,"修改成功",null,null);
        } else {
           throw new DatabaseUpdateException("修改产品信息失败");
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 5)
    public Result listProducts(Product product,HttpServletRequest request) {
        String userId = RequestUtil.getUserId(request);
        User user = userMapper.queryByUsername(userId);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }
        if(product.getPage() != null) {
            product.setPage(product.getPage() * 10);
        }
        List<Product> list = productMapper.query(product);
        return new Result(1,"查询成功",list,CryptUtil.encryptByDES(userId + "##" + new Date().getTime()));
    }

    /**
     * 根据场景进行参数检查
     * @param object 被检查的对象
     * @param context 业务场景
     * @return Result 或 null
     **/
    private Result delegatingParameterObjectCheck(Object object,String context) {
        switch (context) {
            case "editProduct" : {
                Product product = (Product)object;
                if (product.getId() == null) {
                    return new Result(2,"产品id不正确",null,null);
                }
                break;
            }
            case "deleteProduct" : {
                Product product = (Product) object;
                product.setPage(null);
                product.setOrder(null);
                product.setCount(null);
                product.setTypeId(null);
                product.setType(null);
                product.setPhoto(null);
                product.setInTime(null);
                product.setParentId(null);
                product.setParent(null);
                product.setQCode(null);
                product.setWarnBefore(null);
                product.setWarnCount(null);
                product.setManufacturer(null);
                product.setSelfLife(null);
                product.setOutPrice(null);
                product.setProductDate(null);
                if (product.getId() == null && product.getName() == null) {
                    return new Result(2,"必须指定商品id或者商品名称",null,null);
                }
                break;
            }
        }
        return null;
    }
}
