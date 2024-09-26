
package com.longlong.common.mybatis.base;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotEmpty;


import java.util.List;

/**
 * 基础业务接口
 *
 * @param <T>
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 逻辑删除
     *
     * @param ids id集合(逗号分隔)
     * @return boolean
     */
    boolean deleteLogic(@NotEmpty List<Integer> ids);
    /**
     * 逻辑删除
     *
     * @param ids id集合(逗号分隔)
     * @return boolean
     */
    boolean removeByIds(String ids);

    /**
     * 自定义保存 不包含用户信息和更新时间需要手动插入
     * */
    Boolean customSave(T entity);
    /**
     * 自定义更新 不包含用户信息和更新时间需要手动插入
     * */
    Boolean customUpdate(T entity);

}
