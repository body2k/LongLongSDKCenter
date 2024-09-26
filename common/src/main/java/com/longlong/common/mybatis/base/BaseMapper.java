package com.longlong.common.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.longlong.common.constant.CloudConstant;
import com.longlong.common.secure.CloudUser;
import com.longlong.common.secure.utils.SecureUtil;
import com.longlong.common.utils.DateUtil;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    com.baomidou.mybatisplus.core.mapper.BaseMapper<T> getBaseMapper();
    default int insert(T entity) {

        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            entity.setCreateUser(user.getUserId());
            entity.setUpdateUser(user.getUserId());
            if (entity.getStatus() == null) {
                entity.setStatus(CloudConstant.DB_STATUS_NORMAL);
            }
            entity.setIsDeleted(CloudConstant.DB_NOT_DELETED);
        }

        return getBaseMapper().insert(entity);
    }

    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     */
    default   int updateById(@Param(Constants.ENTITY) T entity){
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            entity.setUpdateUser(user.getUserId());
        }
        return getBaseMapper().updateById(entity);
    };

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象 (set 条件值,可以为 null)
     * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    default  int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper){
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            entity.setUpdateUser(user.getUserId());
        }
        return getBaseMapper().update(entity, updateWrapper);
    };

    ;
}
