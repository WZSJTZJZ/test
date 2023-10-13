package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.ExcelParser;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.AdminDTO;
import com.example.springboot.controller.dto.AdminPasswordDTO;
import com.example.springboot.entity.Induction;
import com.example.springboot.service.IInductionService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.service.IAdminService;
import com.example.springboot.entity.Admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @Resource
    private IInductionService inductionService;


    @PostMapping("/register")
    public Result register(@RequestBody AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(adminService.register(adminDTO));
    }

    @PostMapping("/login")
    public Result login(@RequestBody AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        AdminDTO dto = adminService.login(adminDTO);
        return Result.success(dto);
    }

    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(adminService.getOne(queryWrapper));
    }

    //修改密码
    @PostMapping("/password")   //    /user/password
    public Result password(@RequestBody AdminPasswordDTO adminPasswordDTO) {
        adminService.updatePassword(adminPasswordDTO);
        return Result.success();
    }


    // 新增用户
    @PostMapping
    public Result save(@RequestBody Admin admin) {
        return Result.success(adminService.saveOrUpdate(admin));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        // 删除与基础信息表关联的入职信息数据
        inductionService.remove(new QueryWrapper<Induction>().eq("admin_id", id));

        // 执行基础信息表的删除操作
        return adminService.removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // 删除与基础信息表关联的入职信息数据
        inductionService.remove(new QueryWrapper<Induction>().in("admin_id", ids));

        // 执行基础信息表的批量删除操作
        return adminService.removeByIds(ids) ? Result.success() : Result.error();
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(adminService.getById(id));
    }

    //    /page?
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") Integer id) {

        Admin currentAdmin = TokenUtils.getCurrentAdmin();
        System.out.println(currentAdmin.getUsername());
        return Result.success(adminService.findPage(new Page<>(pageNum, pageSize), username, id));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Admin> list = adminService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名

        writer.addHeaderAlias("id", "员工号");
        writer.addHeaderAlias("username", "员工名");
        writer.addHeaderAlias("role", "人员类型");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("place", "身份");
        writer.addHeaderAlias("school", "年龄");
        writer.addHeaderAlias("speciality", "性别");
        writer.addHeaderAlias("education", "学历");
        writer.addHeaderAlias("birthday", "生日");
        writer.addHeaderAlias("phone", "联系电话");

        writer.addHeaderAlias("creatTime", "创建时间");


        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);


        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Admin> list = reader.readAll(Admin.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Admin> admins = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Admin admin = new Admin();

            admin.setId(Integer.parseInt(row.get(0).toString()));
            admin.setUsername(row.get(1).toString());
            admin.setPassword(row.get(2).toString());
            admin.setSex(row.get(2).toString());
            admin.setPlace(row.get(3).toString());
            admin.setSchool(row.get(4).toString());
            admin.setSpeciality(row.get(5).toString());
            admin.setEducation(row.get(6).toString());
            admin.setBirthday(row.get(7).toString());
            admin.setPhone(row.get(8).toString());
            admin.setCreatTime(LocalDateTime.now());
            admin.setRole(row.get(10).toString());

            admins.add(admin);
        }

        adminService.saveBatch(admins);
        return Result.success(true);
    }


}

