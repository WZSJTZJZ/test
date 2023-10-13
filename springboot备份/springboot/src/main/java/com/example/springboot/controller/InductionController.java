package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.IAdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;

import com.example.springboot.service.IInductionService;
import com.example.springboot.entity.Induction;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author li
 * @since 2023-07-28
 */
@RestController
@RequestMapping("/induction")
public class InductionController {

    @Resource
    private IInductionService inductionService;


    @Resource
    private IAdminService adminService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Induction induction) {

        return inductionService.saveOrUpdate(induction) ? Result.success() : Result.error();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {

        return inductionService.removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        return inductionService.removeByIds(ids) ? Result.success() : Result.error();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(inductionService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(inductionService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String username,
                           @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        System.out.println(username);
        Page<Induction> page = inductionService.findePage(new Page<>(pageNum, pageSize), username);
        return Result.success(page);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Induction> list = inductionService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名

        writer.addHeaderAlias("grade", "职系");
        writer.addHeaderAlias("adminId", "员工号");
        writer.addHeaderAlias("admin", "员工名");
        writer.addHeaderAlias("estate", "级别");
        writer.addHeaderAlias("position", "籍贯");
        writer.addHeaderAlias("worker", "工作区分");
        writer.addHeaderAlias("site", "工作地点");
        writer.addHeaderAlias("company", "公司");
        writer.addHeaderAlias("department", "所属部门");
        writer.addHeaderAlias("principal", "教育负责人");
        writer.addHeaderAlias("entry", "入职日");
        writer.addHeaderAlias("internship", "实习期间");
        writer.addHeaderAlias("trial", "试用期间");
        writer.addHeaderAlias("modify", "变更原因");
        writer.addHeaderAlias("difference", "在职区分");
        writer.addHeaderAlias("dimission", "离职日");


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
        List<Induction> inductions = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Induction induction = new Induction();

            induction.setGrade(row.get(0).toString());
            induction.setAdminId(Integer.valueOf(row.get(1).toString()));
            induction.setAdmin(row.get(2).toString());
            induction.setEstate(row.get(3).toString());
            induction.setPosition(row.get(4).toString());
            induction.setWorker(row.get(5).toString());
            induction.setSite(row.get(6).toString());
            induction.setCompany(row.get(7).toString());
            induction.setDepartment(row.get(8).toString());
            induction.setPrincipal(row.get(9).toString());
            induction.setEntry(row.get(10).toString());
            induction.setInternship(row.get(11).toString());
            induction.setTrial(row.get(12).toString());
            induction.setModify(row.get(13).toString());
            induction.setDifference(row.get(14).toString());
            induction.setDimission(row.get(15).toString());


            inductions.add(induction);
        }

        inductionService.saveBatch(inductions);
        return Result.success(true);
    }

}

