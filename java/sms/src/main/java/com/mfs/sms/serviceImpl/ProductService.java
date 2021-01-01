package com.mfs.sms.serviceImpl;

import com.mfs.sms.exception.CreateQCodeException;
import com.mfs.sms.exception.DatabaseUpdateException;
import com.mfs.sms.mapper.ProductMapper;
import com.mfs.sms.mapper.TypeMapper;
import com.mfs.sms.mapper.UserMapper;
import com.mfs.sms.pojo.*;
import com.mfs.sms.utils.CryptUtil;
import com.mfs.sms.utils.ExcelUtil;
import com.mfs.sms.utils.QCodeUtil;
import com.mfs.sms.utils.RequestUtil;
import com.mfs.sms.utils.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductService {
    @Value("${web.upload-path}")
    private String rootPath;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private LogUtil logUtil;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");


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
            return;
        }
        if (!user.getRole().getProductUpdate()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=5");
            return;
        }

        //创建excel
        List<Product> list = productMapper.queryGreaterThan();
        String path = rootPath + "/product/excel/leading-out/" + username + new Date().getTime() + ".xls";
        String file = ExcelUtil.write(path, list);
        File f = new File(rootPath + "/product/excel/leading-out/" + file);
        if (!f.exists()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error?status=2");
            return;
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
                    os.write(b,0,n);
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
     * 下载商品二维码
     *
     * */
    public void downloadQCode(Principal principal,HttpServletRequest request,HttpServletResponse response, String name) {
        //参数检查
        Result result = delegatingParameterObjectCheck(name, "downloadQCode");
        if (result != null) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error");
            return;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error");
            return;
        }
        if (!user.getRole().getProductSelect()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error");
            return;
        }

        File file = new File(rootPath + "/product/qcode/" + name);
        if (!file.exists()) {
            response.setStatus(302);
            response.setHeader("location",request.getContextPath() + "/error");
            return;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;fileName=" + file.getName());
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = is.read(b)) > 0) {
                os.write(b,0,n);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加商品
     * @param principal 已登录的用户主体
     * @param product 要添加的商品
     * @return Result
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ,timeout = 20)
    public Result addProduct(Principal principal,Product product) throws CreateQCodeException {
        //参数检查
        Result result = delegatingParameterObjectCheck(product, "addProduct");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //向数据库中添加数据
        product.setId(product.getId() + new Date().getTime());
        product.setPhoto("default.jpg");
        product.setParentId(user.getId());
        product.setInTime(new Date());
        String qCode = UUID.randomUUID().toString() + ".png";
        product.setQCode(qCode);
        int res = productMapper.add(product);
        if (res == 1) {
            //创建商品码
            boolean qrCode = QCodeUtil.createQRCode(product.getId(),rootPath + "/product/qcode/" + qCode,"png");
            if (!qrCode) {
                throw new CreateQCodeException("创建二维码失败");
            }
            return new Result(1,"添加成功",null,null);
        } else {
            return new Result(2,"添加失败",null,null);
        }
    }

    /**
     * 导入进货单
     * @param principal 已登录的用户主体
     * @param file 进货单
     * @return Result
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ,timeout = 20)
    public Result leadingInPurchaseOrder(Principal principal, MultipartFile file) throws ParseException, DatabaseUpdateException, CreateQCodeException {
        //参数检查
        Result result = delegatingParameterObjectCheck(file, "leadingInPurchaseOrder");
        if (result != null) {
            return result;
        }

        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductInsert()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //暂存excel文件方便解析
        OutputStream os = null;
        InputStream is = null;
        String path = rootPath + "/product/excel/leading-in/" + username + new Date().getTime() + ".xls";
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            os = new FileOutputStream(f);
            is = file.getInputStream();
            int n = 0;
            byte[] b = new byte[1024];
            while ((n = is.read(b)) > 0) {
                os.write(b,0,n);
            }
        } catch (IOException e) {
            if (f.exists()) {
                f.delete();
            }
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //收集创建成功的二维码，发生失败时方便删除
        List<String> qcodes = new ArrayList<>();
        try {
            //解析excel,解析完成后删除
            List<Map<String, String>> list = ExcelUtil.read(path);
            if (f.exists()) {
                f.delete();
            }

            //检查excel文件中的数据
            List<Product> products = new ArrayList<>();
            for (Map<String,String> m : list) {
                Product product = new Product(m.get("编号"), m.get("商品名"), "default.jpg", m.get("供货商"),
                        sdf.parse(m.get("生产日期")), Integer.parseInt(m.get("保质期(月;无保质期,请填-1)")), Integer.parseInt(m.get("提醒(天)")),
                        Integer.parseInt(m.get("数量")), Integer.parseInt(m.get("预警库存")), new Date(), Double.parseDouble(m.get("进价")),
                        Double.parseDouble(m.get("售价")), Integer.valueOf(user.getId()), null, Integer.parseInt(m.get("类型编号")),
                        null, "", null, null);
                Result result1 = delegatingParameterObjectCheck(product, "addProduct");
                if (result1 != null) {
                    return result1;
                }
                products.add(product);
            }

            //向数据库中添加
            for (Product p : products) {
                p.setId(p.getId() + p.getInTime().getTime());
                String qCode = UUID.randomUUID().toString() + ".png";
                p.setQCode(qCode);
                int res = productMapper.add(p);
                if (res == 1) {
                    //创建商品码
                    String qcode = rootPath + "/product/qcode/" + qCode;
                    boolean qrCode = QCodeUtil.createQRCode(p.getId(),qcode,"png");
                    if (!qrCode) {
                        throw new CreateQCodeException("创建二维码失败");
                    }
                    qcodes.add(qcode);
                } else {
                    throw new DatabaseUpdateException("添加商品失败");
                }
            }
        } catch (Exception e) {
            for (String s : qcodes) {
                f = new File(s);
                if (f.exists()) {
                    f.delete();
                }
            }
            throw e;
        }
        return new Result(1,"导入成功",null,null);
    }

    /**
     * 删除指定商品（可以通过商品id和名称删除）
     * @param principal 已登录的用户主体
     * @param product 要修改的商品
     * @return Result
     **/
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
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

    /**
     * 修改快过期的商品(额外会将商品的类型改为促销类型)
     * @param principal 已登录的用户主体
     * @param product 要修改的商品
     * @return Result
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public Result editWillGoBadProduct(Principal principal,Product product) {
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

        //将修改的商品类型改为促销
        product.setTypeId(2);
        int res = productMapper.update(product);
        if (res == 1) {
            return new Result(1,"修改成功",null,null);
        } else {
            return new Result(2,"修改失败",null,null);
        }
    }

    /**
     * 修改商品图片
     * 获取数据库中商品的旧数据 -> 修改数据库商品信息 -> 新的图片保存到磁盘 -> 删除旧的商品图片
     * 假设有连个线程（A，B）正在按照此执行步骤修改同一个商品的图片:
     * 数据库中保存的旧的图片为O.jpg，A线程要将商品图片修改为A.jpg,B线程要将商品图片修改为B.jpg
     *
     * 用问题的执行顺序：
     * 执行顺序一：线程A获取到旧的商品图片为O.jpg -> 线程B也获取到旧的商品图片为O.jpg -> 线程A将数据库中的商品图片修改为A.jpg
     *              -> 线程A将图片A.jpg保存到磁盘 -> 线程A删除旧的图片O.jpg -> 线程B将数据库中商品图片修改为B.jpg
     *              -> 线程B将图片B.jpg保存到磁盘 -> 线程B删除O.jpg失败
     *              最终状态：数据库库中商品的图片为B.jpg,磁盘中保存着A.jpg和B.jpg。磁盘中有无用数据A.jpg,浪费了磁盘空间
     * 执行顺序二：线程A获取到旧的商品图片为O.jpg -> 线程A将数据库中的商品图片修改为A.jpg -> 线程B也获取到旧的商品图片为A.jpg
     *              -> 线程B将数据库中商品图片修改为B.jpg -> 线程B将图片B.jpg保存到磁盘 -> 线程B删除A.jpg失败
     *              -> 线程A将图片A.jpg保存到磁盘 -> 线程A删除旧的图片O.jpg
     *              最终状态：数据库库中商品的图片为B.jpg,磁盘中保存着A.jpg和B.jpg。磁盘中有无用数据A.jpg,浪费了磁盘空间
     * 解决方法：
     * 解决方案一：额外的辅助程序定期检查数据库和磁盘的差异，删除磁盘中的无用图片。但需要开发额外的清理模块
     * 解决方案二：使用synchronized或显示锁等同步化处理方式保证同一时刻只有一个线程使用该方法。但是降低了效率
     * @param principal 已登录的用户主体
     * @param file 用户上传的图片
     * @param id 要修改的商品的id
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result editProductPhoto(Principal principal, MultipartFile file,String id) throws IOException {
        //检查参数
        Result result = delegatingParameterObjectCheck(file, "editProductPhoto");
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

        //先查出要修改的商品保存修改前的记录
        Product p = productMapper.queryById(id);
        if (p == null) {
            return new Result(2,"商品不存在",null,null);
        }

        //修改数据库数据，方便错误回滚
        String name = file.getOriginalFilename();
        String photoName = UUID.randomUUID().toString() + new Date().getTime() + (name.endsWith(".png") ? ".png" : ".jpg");
        Product product = new Product();
        product.setId(id);
        product.setPhoto(photoName);
        int res = productMapper.update(product);
        if (res == 1) {
            //将图片保存到磁盘
            String path = rootPath + "/product/" + photoName;
            OutputStream os  = null;
            InputStream is = null;
            try {
                File f = new File(path);
                if (!f.exists()) {
                    f.createNewFile();
                }
                os = new FileOutputStream(f);
                is = file.getInputStream();
                int n = 0;
                byte[] b = new byte[1024];
                while ((n = is.read(b)) > 0) {
                    os.write(b,0,n);
                }
            } catch (IOException e) {
                //抛出异常回滚数据库操作
                throw e;
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //删除旧的商品图片
            if (!p.getPhoto().equals("default.jpg")) {
                File f = new File(rootPath + "/product/" + p.getPhoto());
                if (f.exists()) {
                    if (!f.delete()) {
                        logUtil.log("删除商品图片失败",this.getClass());
                    }
                }
            }
            return new Result(1,"修改成功",null,null);
        } else {
            return new Result(1,"修改失败",null,null);
        }
    }

    /**
     * 获取商品（分页）
     * @param principal 已登录的用户主体
     * @param product 用于分页的话应该至少包含page属性
     * @return Result
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,timeout = 20)
    public Result listProducts(Principal principal, Product product) {
        //鉴权
        String username = principal.getName();
        User user = userService.quicklyGetUserByUsername(username);
        if (user == null) {
            return new Result(4,"用户不存在",null,null);
        }
        if (!user.getRole().getProductSelect()) {
            return new Result(5,"抱歉,您没有该权限",null,null);
        }

        //将页数换算成起始位置
        if(product.getPage() != null) {
            product.setPage(product.getPage() * 10);
        }
        List<Product> list = productMapper.query(product);
        return new Result(1,"查询成功",list,null);
    }

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
                    return new Result(2,"商品id不正确",null,null);
                }
                if (product.getName() != null && product.getName().length() > 32) {
                    return new Result(2,"商品名称过长",null,null);
                }
                if (product.getManufacturer() != null && product.getName().length() > 32) {
                    return new Result(2,"供货商不合法",null,null);
                }
                product.setPhoto(null);
                if (product.getProductDate() != null && product.getProductDate().compareTo(new Date()) > 0 ) {
                    return new Result(2,"生产日期不合法",null,null);
                }
                if (product.getSelfLife() != null && product.getSelfLife() != -1 && product.getSelfLife() < 1) {
                    return new Result(2,"保质期不合法",null,null);
                }
                if (product.getWarnBefore() != null && product.getWarnBefore() < 0) {
                    return new Result(2,"提醒时间不合法",null,null);
                }
                if (product.getCount() != null && product.getCount() < 0) {
                    return new Result(2,"库存数量不合法",null,null);
                }
                product.setInTime(null);
                if (product.getInPrice() != null && product.getInPrice() < 0) {
                    return new Result(2,"进价不合法",null,null);
                }
                if (product.getOutPrice() != null && product.getOutPrice() < 0) {
                    return new Result(2,"售价不合法",null,null);
                }
                product.setParentId(null);
                if (product.getTypeId() != null) {
                    Type type = typeMapper.queryById(product.getTypeId());
                    if (type == null) {
                        return new Result(2,"类型不合法",null,null);
                    }
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
            case "downloadQCode" : {
                String name = (String) object;
                if (name == null || !(name.endsWith(".png") || name.endsWith(".jpg"))) {
                    return new Result(2,"文件名不合法",null,null);
                }
                break;
            }
            case "editProductPhoto" : {
                MultipartFile file = (MultipartFile)object;
                if (file == null || !(file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png"))) {
                    return new Result(2,"仅支持jpg和png格式的图片",null,null);
                }
                break;
            }
            case "addProduct" : {
                Product product = (Product) object;
                if (product.getId() == null || product.getId().length() > 20) {
                    return new Result(2,"商品编号不合法",null,null);
                }
                if (product.getName() == null || product.getName().length() > 32) {
                    return new Result(2,"商品名称不合法",null,null);
                }
                if (product.getManufacturer() == null || product.getManufacturer().length() > 32) {
                    return new Result(2,"供货商不合法",null,null);
                }
                if (product.getTypeId() == null) {
                    return new Result(2,"商品类型不合法",null,null);
                }
                Type type = typeMapper.queryById(product.getTypeId());
                if (type == null) {
                    return new Result(2,"商品类型不合法",null,null);
                }
                if (product.getProductDate() == null || product.getProductDate().compareTo(new Date()) > 0) {
                    return new Result(2,"生产日期不合法",null,null);
                }
                if (product.getSelfLife() == null || (product.getSelfLife() <= 0 && product.getSelfLife() != -1)) {
                    return new Result(2,"保质期不合法",null,null);
                }
                if (product.getWarnBefore() == null || product.getWarnBefore() < 0) {
                    return new Result(2,"提醒时间不合法",null,null);
                }
                if (product.getCount() == null || product.getCount() < 1) {
                    return new Result(2,"库存数量不合法",null,null);
                }
                if (product.getWarnCount() == null || product.getWarnCount() < 0) {
                    return new Result(2,"最小库存不合法",null,null);
                }
                if (product.getInPrice() == null || product.getInPrice() < 0) {
                    return new Result(2,"进价不合法",null,null);
                }
                if (product.getOutPrice() == null || product.getOutPrice() < 0) {
                    return new Result(2,"售价不合法",null,null);
                }
                break;
            }
        }
        return null;
    }
}
