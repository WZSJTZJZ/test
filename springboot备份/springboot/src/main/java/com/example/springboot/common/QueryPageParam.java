package com.example.springboot.common;

import lombok.Data;

import java.util.HashMap;

/**
 * @author 袁俊辉
 * @version 1.0
 */
@Data
public class QueryPageParam {
    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;
    private int pageSize = PAGE_SIZE;
    private int PageNum = PAGE_NUM;
    private HashMap param = new HashMap<>();

}
