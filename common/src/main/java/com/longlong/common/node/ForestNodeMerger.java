
package com.longlong.common.node;

import java.util.List;

/**
 * 森林节点归并类
 */
public class ForestNodeMerger {

    /**
     * 将节点数组归并为一个森林（多棵树）（填充节点的children域）
     * 时间复杂度为O(n^2)
     *
     * @param items 节点域
     * @param <T>   T 泛型标记
     * @return 多棵树的根节点集合
     */
    // 定义一个泛型方法merge，该方法的参数是INode类型的列表
    public static <T extends INode> List<T> merge(List<T> items) {
        // 创建一个ForestNodeManager对象，该对象的参数是INode类型的列表
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items);
        // 遍历INode类型的列表
        items.forEach(forestNode -> {
            // 如果当前节点的父节点id不为0且不为空
            if (forestNode.getParentId() != 0 && forestNode.getParentId() != null) {
                // 从forestNodeManager中获取父节点
                INode node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
                // 如果父节点不为空
                if (node != null) {
                    // 将当前节点添加到父节点的子节点列表中
                    node.getChildren().add(forestNode);

                } else {
                    // 如果父节点为空，则将当前节点的id添加到forestNodeManager中
                    forestNodeManager.addParentId(forestNode.getId());
                }
            }
        });
        // 返回forestNodeManager中的根节点列表
        return forestNodeManager.getRoot();
    }

    /**
     * 将节点数组归并为一个森林（多棵树）（填充节点的children域）
     * 时间复杂度为O(n^2)
     *
     * @param items 节点域
     * @param <T>   T 泛型标记
     * @return 多棵树的根节点集合
     */
    // 定义一个泛型方法merge，该方法的参数是INode类型的列表
    public static <T extends INode> List<T> merge(List<T> items,Integer fatherId) {
        // 创建一个ForestNodeManager对象，该对象的参数是INode类型的列表
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items);
        // 遍历INode类型的列表
        items.forEach(forestNode -> {
            // 如果当前节点的父节点id不为0且不为空
            if (forestNode.getParentId() != fatherId && forestNode.getParentId() != null) {
                // 从forestNodeManager中获取父节点
                INode node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
                // 如果父节点不为空
                if (node != null) {
                    // 将当前节点添加到父节点的子节点列表中
                    node.getChildren().add(forestNode);

                } else {
                    // 如果父节点为空，则将当前节点的id添加到forestNodeManager中
                    forestNodeManager.addParentId(forestNode.getId());
                }
            }
        });
        // 返回forestNodeManager中的根节点列表
        return forestNodeManager.getRoot();
    }





}
