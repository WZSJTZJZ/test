package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.PageResult;
import com.example.springboot.common.Result;
import com.example.springboot.entity.EmployeeChange;
import com.example.springboot.service.EmployeeChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeChangeController {
    @Autowired
    private EmployeeChangeService employeeChangeService;

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String grade) {

        Page<EmployeeChange> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<EmployeeChange> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(EmployeeChange::getEmployeeName, grade);

        IPage<EmployeeChange> result = employeeChangeService.page(page, lambdaQueryWrapper);
        System.out.println("total==" + result.getTotal());
        System.out.println("name" + grade);

        PageResult<EmployeeChange> pageResult = new PageResult<>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());

        return Result.success(pageResult);
    }

    @PostMapping("/add")
    public Result addEmployeeChange(@RequestBody EmployeeChange employeeChange) {
        employeeChange.setId(null);
        return employeeChangeService.save(employeeChange) ? Result.success() : Result.error();
    }


//    @PutMapping("/")
//    public boolean updateEmployeeChange(@RequestBody EmployeeChange employeeChange) {
//        return employeeChangeService.updateById(employeeChange);
//    }

    @DeleteMapping("/{id}")
    public Result deleteEmployeeChange(@PathVariable("id") Integer id) {
        System.out.println("id=\t" + id);
        return employeeChangeService.removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return employeeChangeService.removeByIds(ids) ? Result.success() : Result.error();
    }

    @GetMapping("/export")
    public void export(@RequestParam("ids") List<Integer> ids, HttpServletResponse response) throws Exception {
        // 根据前端传递的 id 集合从数据库查询对应的数据
        List<EmployeeChange> list = employeeChangeService.listByIds(ids);
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("id", "记录ID");
        writer.addHeaderAlias("employeeId", "员工工号");
        writer.addHeaderAlias("employeeName", "员工姓名");
        writer.addHeaderAlias("changeReason", "变更原因");
        writer.addHeaderAlias("changeDate", "变更日期");
        writer.addHeaderAlias("entryDate", "入职日期");
        writer.addHeaderAlias("regularDate", "转正日期");
        writer.addHeaderAlias("jobLevel", "职级");
        writer.addHeaderAlias("employmentStatus", "就业状态");
        writer.addHeaderAlias("workLocation", "工作地点");
        writer.addHeaderAlias("department", "部门");
        writer.addHeaderAlias("resignationDate", "离职日期");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工变更记录", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


}