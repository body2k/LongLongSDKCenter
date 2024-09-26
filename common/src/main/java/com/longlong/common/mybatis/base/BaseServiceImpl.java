
package com.longlong.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.common.constant.CloudConstant;
import com.longlong.common.mybatis.Sql;
import com.longlong.common.mybatis.TableJoin;
import com.longlong.common.secure.CloudUser;
import com.longlong.common.secure.utils.SecureUtil;
import com.longlong.common.utils.DateUtil;

import com.longlong.common.utils.Func;
import com.longlong.common.utils.SpringUtil;
import com.longlong.common.utils.StringUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.sql.DataSource;
import jakarta.validation.constraints.NotEmpty;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {
    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        CloudUser user = SecureUtil.getUser();
        for (T entity : entityList) {
            if (user != null) {
                //  if (StringUtils.isNotBlank(user.getTenantId())) entity.setTenantId(user.getTenantId());
                entity.setCreateUser(user.getUserId());
                entity.setUpdateUser(user.getUserId());
            }
            if (entity.getStatus() == null) {
                entity.setStatus(CloudConstant.DB_STATUS_NORMAL);
            }
            Date now = DateUtil.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
        }
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public boolean save(T entity) {
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            //   if (StringUtils.isNotBlank(user.getTenantId())) entity.setTenantId(user.getTenantId());
            entity.setCreateUser(user.getUserId());
            entity.setUpdateUser(user.getUserId());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(CloudConstant.DB_STATUS_NORMAL);
        }
        Date now = DateUtil.now();
        entity.setUpdateTime(now);
        entity.setCreateTime(now);
        entity.setIsDeleted(CloudConstant.DB_NOT_DELETED);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            entity.setUpdateUser(user.getUserId());
        }
        Date now = DateUtil.now();
        entity.setUpdateTime(now);
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(@NotEmpty List<Integer> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean removeByIds(String ids) {
        return super.removeByIds(Func.toIntList(ids));
    }

    @Override
    public Boolean customSave(T entity) {
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(CloudConstant.DB_NOT_DELETED);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(CloudConstant.DB_STATUS_NORMAL);
        }
        return super.save(entity);
    }

    @Override
    public Boolean customUpdate(T entity) {
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(CloudConstant.DB_NOT_DELETED);
        }
        if (entity.getStatus() == null) {
            entity.setStatus(CloudConstant.DB_STATUS_NORMAL);
        }
        return super.updateById(entity);
    }

    /**
     * 根据 entity 条件，查询全部记录 支持连表  仆从
     *
     * @param masterQueryWrapper 实体对象封装操作类（可以为 null）
     */
    public List<?> superSelectList(@Param(Constants.WRAPPER) Wrapper<? extends BaseEntity> masterQueryWrapper, QueryWrapper<? extends BaseEntity>... servant) {
        //获取数据源
//        DataSource dataSource = SpringUtil.getDataSource();
//        QueryWrapper queryWrapper = new QueryWrapper();
//        //    queryWrapper.select("ha_user_role.name");
//        String sqlSelect = queryWrapper.getSqlSelect();
//        System.out.println(sqlSelect);
//        UserRole userRole = new UserRole();
//        Field[] declaredFields = userRole.getClass().getDeclaredFields();
//        String masterTableName = userRole.getClass().getAnnotation(TableName.class).value();
//        // + masterTableName + Sql.ASTERISK + Sql.FROM + masterTableName + " "
//        StringBuffer runSql = new StringBuffer(Sql.SELECT);
//
//        StringBuffer content = new StringBuffer();
//        if (StringUtil.isNotBlank(queryWrapper.getSqlSelect())) {
//            runSql.append(sqlSelect);
//        } else {
//            runSql.append(masterTableName).append(Sql.ASTERISK);
//        }
//        for (Field declaredField : declaredFields) {
//            TableJoin annotation = declaredField.getAnnotation(TableJoin.class);
//            if (annotation != null) {
//                String servant = annotation.tableName()
//                        .getAnnotation(TableName.class).value();
//                content.append(annotation.join().getValue())
//                        .append(Sql.BLACK)
//                        .append(servant)
//                        .append(Sql.ON)
//                        .append(masterTableName)
//                        .append(Sql.DOT)
//                        .append(annotation.name())
//                        .append(Sql.EQUALS)
//                        .append(servant)
//                        .append(Sql.DOT)
//                        .append(annotation.fieldName())
//                        .append("\n");
//                if (StringUtil.isBlank(queryWrapper.getSqlSelect())) {
//                    runSql.append(Sql.COMMA).append(servant).append(Sql.ASTERISK);
//                }
//
//            }
//
//
//        }
//        runSql.append(Sql.BLACK).append(Sql.FROM).append(masterTableName).append("\n").append(content);
//        System.out.println(runSql);
//        DataSource dataSource = SpringUtil.getDataSource();
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        Class<? extends HashMap> aClass = new HashMap<String, Object>().getClass();
//        List<? extends HashMap> hashMaps = jdbcTemplate.queryForList(runSql.toString(), aClass);
//        System.out.println(hashMaps);
        return null;
    }

}
