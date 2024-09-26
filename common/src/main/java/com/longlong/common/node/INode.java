
package com.longlong.common.node;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public interface INode  {


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
    List<INode> getChildren();

}
