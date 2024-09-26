package com.longlong.common.utils;




import com.longlong.common.node.TreeNodeV2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 29027 待测试
 */
public class TreeUtil {
    /**
     * 数据组合成树桩结构
     *
     * @param treeNodes 集合继承 TreeNode接口进行使用
     * @return 返回树桩结构
     */
    public static <T extends TreeNodeV2> List<T> tree(List<T> treeNodes) {
        List<T> list = new ArrayList<>();
        //传入数据进行循环
        for (T item : treeNodes) {
            //判断父节点是否存在如果等于0则为一级节点
            if (item.getParentId() != null & item.getParentId() == 0) {
                list.add(item);
            }
            //进行子集循环
            for (T itemChildren : treeNodes) {
                //如果id和父级id相同则进行赋值
                if (itemChildren.getParentId().equals(item.getId())) {
                    item.getChildren().add(itemChildren);
                }

            }
            item.setIsChildren(!item.getChildren().isEmpty());
        }
        return list;
    }

    /**
     * 将tree转换成List 清空Children
     *
     * @param treeNodes 集合继承 TreeNode接口进行使用
     * @return 返回List集合
     */
    public static <T extends TreeNodeV2> List<T> treeToList(List<T> treeNodes) {
        return toList(treeNodes).stream().peek(item -> {
            item.getChildren().clear();
        }).collect(Collectors.toList());
    }

    /**
     * 将tree转换成List 不清空Children
     *
     * @param treeNodes 集合继承 TreeNode接口进行使用
     * @return 返回List集合
     */
    public static <T extends TreeNodeV2> List<T> toList(List<T> treeNodes) {
        List<T> list = new ArrayList<>();
        for (T treeNode : treeNodes) {
            list.add(treeNode);
            if (treeNode.getChildren() != null && !treeNode.getChildren().isEmpty()) {
                list.addAll((Collection<? extends T>) toList(treeNode.getChildren()));
            }
        }
        return list;
    }






}
