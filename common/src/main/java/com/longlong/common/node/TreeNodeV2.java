package com.longlong.common.node;

import java.util.List;
/**
 * 待测试
 * */
public interface TreeNodeV2 {
    /**
     * 主键
     *
     * @return Integer
     */
    Integer getId();

    /**
     * 父主键
     *
     * @return Integer
     */
    Integer getParentId();

    /**
     * 子孙节点
     *
     * @return List
     */
    List<TreeNodeV2> getChildren();

    /*是否有下一级*/
    Boolean setIsChildren(Boolean b);
}
