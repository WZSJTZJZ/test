package com.example.springboot.common;

import com.example.springboot.entity.Admin;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelParser {

    public static List<Admin> parse(MultipartFile file) throws IOException {
        List<Admin> adminList = new ArrayList<>();

        // 创建工作簿
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        // 获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 迭代行
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 跳过表头行
            if (row.getRowNum() == 0) {
                continue;
            }

            Admin admin = new Admin();
            admin.setId((int) row.getCell(0).getNumericCellValue());
            admin.setUsername(row.getCell(1).getStringCellValue());
            admin.setPassword(row.getCell(2).getStringCellValue());
            admin.setSex(row.getCell(3).getStringCellValue());
            admin.setPlace(row.getCell(4).getStringCellValue());
            admin.setSchool(row.getCell(5).getStringCellValue());
            admin.setSpeciality(row.getCell(6).getStringCellValue());
            admin.setEducation(row.getCell(7).getStringCellValue());
            admin.setBirthday(row.getCell(8).getStringCellValue());
            admin.setPhone(row.getCell(9).getStringCellValue());
            admin.setCreatTime(LocalDateTime.now()); // 设置当前时间或使用其他逻辑来设置时间值
            admin.setRole(row.getCell(11).getStringCellValue());

            adminList.add(admin);
        }

        // 关闭工作簿
        workbook.close();

        return adminList;
    }
}