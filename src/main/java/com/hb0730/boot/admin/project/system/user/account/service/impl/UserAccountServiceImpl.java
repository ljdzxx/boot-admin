package com.hb0730.boot.admin.project.system.user.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hb0730.boot.admin.domain.service.impl.SuperBaseServiceImpl;
import com.hb0730.boot.admin.exceptions.BusinessException;
import com.hb0730.boot.admin.project.system.user.account.mapper.IUserAccountMapper;
import com.hb0730.boot.admin.project.system.user.account.model.entity.UserAccountEntity;
import com.hb0730.boot.admin.project.system.user.account.model.vo.UserAccountParams;
import com.hb0730.boot.admin.project.system.user.account.model.vo.UserAccountVO;
import com.hb0730.boot.admin.project.system.user.account.service.IUserAccountService;
import com.hb0730.commons.lang.StringUtils;
import com.hb0730.commons.spring.ValidatorUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户账号  服务实现类
 * </p>
 *
 * @author bing_huang
 * @since 3.0.0
 */
@Service
public class UserAccountServiceImpl
        extends SuperBaseServiceImpl<Long, UserAccountParams, UserAccountVO, UserAccountEntity, IUserAccountMapper> implements IUserAccountService {

    @Override
    public boolean save(@NonNull UserAccountVO vo) {
        ValidatorUtils.validate(vo);
        String username = vo.getUsername();
        Long userId = vo.getUserId();
        UserAccountEntity entity = userAccountByUserId(userId);
        if (null != entity) {
            throw new BusinessException("用户id已绑定账号,请勿重新绑定");
        }
        entity = userAccountByUsername(username);
        if (null != entity) {
            throw new BusinessException("用户账号已存在，请重新设置");
        }
        entity = vo.convertTo();

        return super.save(entity);
    }

    @Override
    @Nullable
    public UserAccountEntity userAccountByUserId(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        QueryWrapper<UserAccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserAccountEntity.USER_ID, id);
        return super.getOne(queryWrapper, false);
    }


    @Override
    @Nullable
    public UserAccountEntity userAccountByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        QueryWrapper<UserAccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserAccountEntity.USERNAME, username);
        return super.getOne(queryWrapper, false);
    }
}