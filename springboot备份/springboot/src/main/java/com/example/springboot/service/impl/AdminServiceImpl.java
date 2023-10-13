package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.AdminDTO;
import com.example.springboot.controller.dto.AdminPasswordDTO;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Menu;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.IMenuService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot.common.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
//        ServiceImpl 类是 Service 接口的实现类，用于实现具体的业务逻辑。ServiceImpl 类需要继承 BaseServiceImpl 类，
//        并且需要注入对应的 Mapper 接口实例。通过继承 BaseServiceImpl 类，ServiceImpl 类会自动获得基本的业务操作方法的实现。
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    AdminMapper adminMapper;

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public AdminDTO login(AdminDTO adminDTO) {
        Admin one = getAdminInfo(adminDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, adminDTO, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            adminDTO.setToken(token);

            String role = one.getRole();

            List<Menu> roleMenus = getRoleMenus(role);

            adminDTO.setMenus(roleMenus);

            return adminDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Admin register(AdminDTO adminDTO) {
        Admin one = getAdminInfo(adminDTO);
        if (one == null) {
            one = new Admin();
            BeanUtil.copyProperties(adminDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private Admin getAdminInfo(AdminDTO adminDTO) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", adminDTO.getUsername());
        queryWrapper.eq("password", adminDTO.getPassword());
        Admin one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    @Override
    public void updatePassword(AdminPasswordDTO adminPasswordDTO) {
        int update = adminMapper.updatePassword(adminPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    @Override
    public Page<Admin> findPage(Page<Admin> page, String username, Integer id) {

        return adminMapper.findPage(page,username,id);

    }

    @Override
    public void batchInsert(List<Admin> admins) {
        baseMapper.batchInsert(admins);
    }

    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

//  查出系统所有菜单
        List<Menu> menus = menuService.findMenus("");

        //筛选当前用户角色菜单
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
