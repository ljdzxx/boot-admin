package com.hb0730.boot.admin.project.system.role.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hb0730.boot.admin.commons.annotation.Log;
import com.hb0730.boot.admin.commons.constant.ModuleName;
import com.hb0730.boot.admin.commons.constant.RequestMappingNameConstants;
import com.hb0730.boot.admin.commons.constant.SystemConstants;
import com.hb0730.boot.admin.commons.constant.enums.BusinessTypeEnum;
import com.hb0730.boot.admin.commons.utils.bean.BeanUtils;
import com.hb0730.boot.admin.commons.web.controller.BaseController;
import com.hb0730.boot.admin.commons.web.response.ResponseResult;
import com.hb0730.boot.admin.commons.web.response.Result;
import com.hb0730.boot.admin.project.system.role.model.entity.SystemRoleEntity;
import com.hb0730.boot.admin.project.system.role.model.vo.RoleParams;
import com.hb0730.boot.admin.project.system.role.model.vo.SystemRoleVO;
import com.hb0730.boot.admin.project.system.role.org.model.entity.SystemRoleOrgEntity;
import com.hb0730.boot.admin.project.system.role.org.service.ISystemRoleOrgService;
import com.hb0730.boot.admin.project.system.role.permission.model.entity.SystemRolePermissionEntity;
import com.hb0730.boot.admin.project.system.role.permission.service.ISystemRolePermissionService;
import com.hb0730.boot.admin.project.system.role.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色  前端控制器
 * </p>
 *
 * @author bing_huang
 * @since 2020-03-27
 */
@RestController
@RequestMapping(RequestMappingNameConstants.REQUEST_ROLE)
public class SystemRoleController extends BaseController<RoleParams, SystemRoleVO, Long> {
    @Autowired
    private ISystemRoleService systemRoleService;
    @Autowired
    private ISystemRolePermissionService systemRolePermissionService;
    @Autowired
    private ISystemRoleOrgService systemRoleOrgService;

    /**
     * 获取全部角色
     *
     * @param roleParams 过滤条件
     * @return 角色信息
     */
    @PostMapping("/all")
    public Result<List<SystemRoleVO>> getRoleAll(@RequestBody RoleParams roleParams) {
        List<SystemRoleVO> list = systemRoleService.list(roleParams);
        return ResponseResult.resultSuccess(list);
    }

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param roleParams 过滤条件
     * @return 分页后的角色信息
     */
    @PostMapping("/all/page")
    @PreAuthorize("hasAnyAuthority('role:query','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<Page<SystemRoleVO>> getRolePage(@RequestBody RoleParams roleParams) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SystemRoleVO> page = systemRoleService.page(roleParams);
        return ResponseResult.resultSuccess(page);
    }

    /**
     * <p>
     * 保存
     * </p>
     *
     * @param vo 角色信息
     * @return 是否成功
     */
    @Override
//    @PostMapping("/save")
    @Log(paramsName = {"vo"}, module = ModuleName.ROLE, title = "角色保存", businessType = BusinessTypeEnum.INSERT)
    @PreAuthorize("hasAnyAuthority('role:save','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> save(SystemRoleVO vo) {
        systemRoleService.save(BeanUtils.transformFrom(vo, SystemRoleEntity.class));
        return ResponseResult.resultSuccess("保存成功");
    }

    /**
     * 根据id修改角色
     *
     * @param id id
     * @param vo 角色信息
     * @return 是否成功
     */
    @Override
//    @PostMapping("/update/{id}")
    @Log(paramsName = {"vo"}, module = ModuleName.ROLE, title = "角色修改", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("hasAnyAuthority('role:update','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> updateById(Long id,SystemRoleVO vo) {
        vo.setId(id);
        systemRoleService.updateById(BeanUtils.transformFrom(vo, SystemRoleEntity.class));
        return ResponseResult.resultSuccess("修改成功");
    }

    /**
     * 根据id删除角色
     *
     * @param id id
     * @return 是否成功
     */
    @Override
//    @GetMapping("/delete/{id}")
    @Log(module = ModuleName.ROLE, title = "角色删除", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("hasAnyAuthority('role:delete','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> deleteById(Long id) {
        systemRoleService.removeById(id);
        return ResponseResult.resultSuccess("修改成功");
    }

    /**
     * <p>
     * 角色删除
     * </p>
     *
     * @param id id
     * @return 是否成功
     */
    @Override
//    @PostMapping("/delete")
    @Log(module = ModuleName.ROLE, title = "角色删除", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("hasAnyAuthority('role:delete','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> deleteByIds(List<Long> id) {
        if (!CollectionUtils.isEmpty(id)) {
            systemRoleService.removeByIds(id);
            return ResponseResult.resultSuccess("修改成功");
        }
        return ResponseResult.resultFall("请选择");
    }
    /*******权限***************/
    /**
     * <p>
     * 根据角色获取权限id
     * </p>
     *
     * @param id 角色id
     * @return 权限id
     */
    @GetMapping("/permission/id/{id}")
    public Result<Set<Long>> getPermissionByRoleId(@PathVariable Long id) {
        QueryWrapper<SystemRolePermissionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SystemRolePermissionEntity.IS_ENABLED, SystemConstants.ENABLED);
        queryWrapper.eq(SystemRolePermissionEntity.ROLE_ID, id);
        List<SystemRolePermissionEntity> entity = systemRolePermissionService.list(queryWrapper);
        Set<Long> permissionIds = Sets.newConcurrentHashSet();
        if (!CollectionUtils.isEmpty(entity)) {
            permissionIds = entity.parallelStream().map(SystemRolePermissionEntity::getPermissionId).collect(Collectors.toSet());
        }
        return ResponseResult.resultSuccess(permissionIds);
    }

    /**
     * <p>
     * 获取菜单对应的权限id key-value方式
     * </p>
     *
     * @param id 角色id
     * @return {@code Map<menuId,Set<permissionIds>>}
     */
    @GetMapping("/permission/map/{id}")
    public Result<Map<Long, Set<Long>>> getMenuPermissionMapByRoleId(@PathVariable Long id) {
        Map<Long, Set<Long>> menuPermissionIds = systemRolePermissionService.getPermissionIdsByRoleId(id);
        return ResponseResult.resultSuccess(menuPermissionIds);
    }

    /**
     * <p>
     * 保存(修改)菜单对应的权限id
     * </p>
     *
     * @param id            菜单id
     * @param permissionIds 权限id
     * @return 是否成功
     */
    @PostMapping("/permission/save/{id}")
    @Log(paramsName = {"permissionIds"}, module = ModuleName.ROLE, title = "角色权限更新", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("hasAnyAuthority('role:permission:save','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> savePermissionByRoleId(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        systemRolePermissionService.savePermissionByRoleId(id, permissionIds);
        return ResponseResult.resultSuccess("保存成功");
    }
    /*******数据范围***********/
    /**
     * 根据角色id获取数据范围id
     *
     * @param roleId 角色id
     * @return 数据范围
     */
    @GetMapping("/org/role/{roleId}")
    public Result<List<Long>> getOrgIdByRoleId(@PathVariable Long roleId) {
        QueryWrapper<SystemRoleOrgEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SystemRoleOrgEntity.IS_ENABLED, SystemConstants.ENABLED);
        queryWrapper.eq(SystemRoleOrgEntity.ROLE_ID, roleId);
        List<SystemRoleOrgEntity> list = systemRoleOrgService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseResult.resultSuccess(Lists.newArrayList());
        }
        List<Long> orgIds = list.parallelStream().map(SystemRoleOrgEntity::getOrgId).collect(Collectors.toList());
        return ResponseResult.resultSuccess(orgIds);
    }

    /**
     * <p>
     * 保存数据范围
     * </p>
     *
     * @param roleId 角色id
     * @param orgIds 数据范围id
     * @return 是否成功
     */
    @PostMapping("/org/role/save/{roleId}")
    @Log(paramsName = {"orgIds"}, module = ModuleName.ROLE, title = "角色数据范围更新", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("hasAnyAuthority('role:org:update','ROLE_ADMINISTRATOR','ROLE_ROLE_ADMIN')")
    public Result<String> saveOrgIdByRoleId(@PathVariable Long roleId, @RequestBody List<Long> orgIds) {
        systemRoleOrgService.saveOrgIdsByRoleId(roleId, orgIds);
        return ResponseResult.resultSuccess("保存成功");
    }
}

